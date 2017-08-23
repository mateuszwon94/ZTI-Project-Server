package zti.server.util;

import java.util.Map.Entry;

public class Pair<K, V> implements Entry<K, V> {
    public Pair(final K key) {
        this.key = key;
    }
    
    public Pair(final K key, final V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() {
        return key;
    }
    
    public V getValue() {
        return value;
    }
    
    public V setValue(final V value) {
        final V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    private final K key;
    private V value;
}