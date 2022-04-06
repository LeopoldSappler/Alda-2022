package dictionary;

import java.util.ArrayList;
import java.util.Iterator;

public class HashDictionaryTest<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    @Override
    public V insert(K key, V value) {
        return null;
    }

    @Override
    public V search(K key) {
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }

    private static class HashNode<K, V> {
        K key;
        V value;
        final int hashAdress;

        public HashNode(K key, V value, int hashAdress) {
            this.key = key;
            this.value = value;
            this.hashAdress = hashAdress;
        }
    }

    private ArrayList<HashNode<K, V>> arrayList;
    private int capacity;
    private int size;

    public HashDictionaryTest() {
        arrayList = new ArrayList<>();
        capacity = 10;
        size = 0;

        // Create empty chains
        for (int i = 0; i < capacity; i++)
            arrayList.add(null);
    }
}
