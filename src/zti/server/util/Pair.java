package zti.server.util;

import java.util.Map.Entry;

/**
 * Prosta klasa reprezentujaca pare klucz-wartosc
 * @author Mateusz Winiarski
 * @param <K> typ klucza
 * @param <V> typ wartosci
 */
public class Pair<K, V> implements Entry<K, V> {
    /**
     * Konstruktor
     * @param key klucz
     */
    public Pair(final K key) {
        this.key = key;
    }
    
    /**
     * Konstruktor
     * @param key klucz
     * @param value wartosc
     */
    public Pair(final K key, final V value) {
        this.key = key;
        this.value = value;
    }
    
    /**
     * @return klucz
     * @see java.util.Map.Entry#getKey()
     */
    public K getKey() {
        return key;
    }
    
    /**
     * @return wartosc
     * @see java.util.Map.Entry#getValue()
     */
    public V getValue() {
        return value;
    }
    
    /**
     * @param value nowa wartosc
     * @see java.util.Map.Entry#setValue(java.lang.Object)
     */
    public V setValue(final V value) {
        final V oldValue = this.value;
        this.value = value;
        return oldValue;
    }
    
    @Override
    public boolean equals(Object other) {
    	if (other == null) return false;
    	if (other == this) return true;
    	if (!(other instanceof  Pair<?,?>)) return false;
    	
    	Pair otherPair = (Pair)other;
    	if (!key.equals(otherPair.key)) return false;
    	if (!value.equals(otherPair.value)) return false;
    	
    	return true;
    }

    private final K key;
    private V value;
}