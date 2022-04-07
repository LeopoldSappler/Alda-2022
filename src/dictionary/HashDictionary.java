package dictionary;


import java.util.Iterator;

public class HashDictionary<K, V> implements Dictionary <K,V> {

    private static int loadFactor = 2;

    static private class Entry<K, V> {
        public K key;
        public Node<K, V> first;

        public Entry(K k, Node<K, V> v) {
            key = k;
            first = v;
        }
    }

    static private class Node<K, V> {
        public K key;
        public V value;
        public Node<K, V> next;

        public Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    // list
    private Entry<K, V>[] dictionary;
    private int size;


    public HashDictionary(int i) {
        // allocate list with i elements
        dictionary = new Entry[i];
        size = i;
    }

    public static int nextPrime(int num) {
        num++;
        for (int i = 2; i < num; i++) {
            if (num % i == 0) {
                num++;
                i = 2;
            } else {
                continue;
            }
        }
        return num;
    }


    public V search(K key) {

        int pos = key.hashCode() % size;
        if (pos < 0) pos += size;
        if (dictionary[pos] == null) {
            return null;
        } else {
            Node<K, V> first = dictionary[pos].first;
            while (first.next != null && !first.key.equals(key)) {
                first = first.next;
            }
            if (first != null)
                return first.value;
            else return null;
        }
    }

    private void resizeArray() {
        // create temp array (one additional element needed)
        Dictionary.Entry<K, V>[] tempEntryArray = new Dictionary.Entry[size * loadFactor + 1];

        // reuse iterator
        int tempEntriesPos = 0;
        for (Iterator<Dictionary.Entry<K, V>> it = iterator(); it.hasNext(); ) {
            tempEntryArray[tempEntriesPos++] = it.next();
        }

        // clean up
        int newSize = nextPrime(size * 2);
        this.dictionary = new Entry[newSize];
        this.size = newSize;

        // reuse insert
        for (int i = 0; i < tempEntriesPos; i++) {
            Dictionary.Entry<K, V> kvEntry = tempEntryArray[i];
            insert(kvEntry.getKey(), kvEntry.getValue());
        }
    }

    public V insert(K key, V value) {

        int pos = key.hashCode() % size;
        if (pos < 0) pos += size;
        if (dictionary[pos] == null) {
            // add
            dictionary[pos] = new Entry<K, V>(key, new Node<K, V>(key, value, null));
            return null;
        } else {


            Node<K, V> first = dictionary[pos].first;

            int load = 0;
            Node<K, V> last = null;
            while (first != null) {
                if (first.key.equals(key)) {
                    V old = first.value;
                    // update value
                    first.value = value;
                    return old;
                }
                last = first;
                first = first.next;
                load++;
            }
            last.next = new Node<K, V>(key, value, null);
            if (load > loadFactor) {
                //System.out.println("resize");
                resizeArray();
            }
        }

        return null;
    }

    public V remove(K key) {
        // compute position in array
        int pos = key.hashCode() % size;
        // fix position
        if (pos < 0) pos += size;
        if (dictionary[pos] == null) {
            // key not found in array
            return null;
        } else {

            // handle first value
            if (dictionary[pos].first.key.equals(key)) {
                Node<K, V> first = dictionary[pos].first;
                if (dictionary[pos].first.next != null) {
                    dictionary[pos].first = dictionary[pos].first.next;
                } else {
                    dictionary[pos] = null;
                }
                return first.value;

            } else {
                Node<K, V> last = dictionary[pos].first;
                Node<K, V> currentNode = dictionary[pos].first.next;


                while (currentNode != null) {
                    if (currentNode.key.equals(key)) {
                        last.next = currentNode.next;
                        return currentNode.value;
                    }
                    last = currentNode;
                    currentNode = currentNode.next;
                }
            }
        }
        return null;
    }
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<Dictionary.Entry<K, V>> iterator() {

        return new Iterator<>() {
            int dictPos = 0;
            Node<K, V> currentElement;

            @Override
            public boolean hasNext() {
                int pos = dictPos;
                Node<K, V> element = currentElement;

                if (element != null && element.next != null) {
                    return true;
                } else {
                    while (pos < size) {
                        if (dictionary[pos] != null && dictionary[pos].first != null) {
                            return true;
                        }
                        pos++;
                        //xxxxxxxx
                    }

                }
                return false;
            }

            @Override
            public Dictionary.Entry<K, V> next() {
                if (currentElement != null && currentElement.next != null) {
                    currentElement = currentElement.next;
                    return new Dictionary.Entry<>(currentElement.key, currentElement.value);
                }


                while (dictPos < size) {
                    if (dictionary[dictPos] != null && dictionary[dictPos].first != null) {
                        currentElement = dictionary[dictPos].first;
                        dictPos++;
                        return new Dictionary.Entry<>(currentElement.key, currentElement.value);
                    }
                    dictPos++;
                }
                return null;
            }
        };
    }
}
