package dictionary;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedArrayDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    private static final int DEF_CAPACITY = 16;
    private int size;
    private Entry<K, V>[] data;

    @SuppressWarnings("unchecked")
    public SortedArrayDictionary() {
        this.size = 0;
        data = new Entry[DEF_CAPACITY];
    }

    private int searchKey(K key) {
        //TODO: Binäre Suche
        for (int i = 0; i < size; i++) {
            if (data[i].getKey().equals(key)) {
                return i;
            }
        }
        return -1;
    }

    public V search(K key) {
        int i = searchKey(key);
        if (i >= 0)
            return data[i].getValue();
        else
            return null;
    }

    public V remove(K key) {
        int i = searchKey(key);
        if (i == -1)
            return null;
        V r = data[i].getValue();
        if (size - 1 - i >= 0) System.arraycopy(data, i + 1, data, i, size - 1 - i);
        data[--size] = null;
        return r;
    }

    public V insert(K key, V value) {
        int i = searchKey(key);

        if (i != -1) {
            V r = data[i].getValue();
            data[i].setValue(value);
            return r;
        }

        if (data.length == size) {
            data = Arrays.copyOf(data, 2 * size);
        }
        int j = size - 1;
        while (j >= 0 && key.compareTo(data[j].getKey()) < 0) {
            data[j + 1] = data[j];
            j--;
        }
        data[j + 1] = new Entry<>(key, value);
        size++;
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Dictionary.Entry<K, V>> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size && data[index] != null;
            }

            @Override
            public Dictionary.Entry<K, V> next() {
                if (hasNext())
                    return data[index++];
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
