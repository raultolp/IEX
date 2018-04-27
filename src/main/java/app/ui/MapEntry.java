package app.ui;

import java.util.Objects;

//https://stackoverflow.com/questions/38487797/javafx-populate-tableview-with-an-observablemap-that-has-a-custom-class-for-its


public final class MapEntry<K, V> {

    private final K key;
    private final V value;

    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        // check equality only based on keys
        if (obj instanceof MapEntry) {
            MapEntry<?, ?> other = (MapEntry<?, ?>) obj;
            return Objects.equals(key, other.key);
        } else {
            return false;
        }
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

}
