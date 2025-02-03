package cogito.util;

import java.util.Objects;

/**
 * A convenience class to represent key-value pairs.
 */
public class Pair<K, V> {

    // The key of this Pair.
    private K key;

    // The value of this Pair.
    private V value;

    // Error message to show if a null key is given.
    private static final String NULL_KEY_ERROR = "Key can not be null";

    // Error message to show if a null value is given.
    private static final String NULL_VALUE_ERROR = "Value can not be null";

    /**
     * Creates a new pair with given key and value.
     *
     * @param key The key of the pair, not null.
     * @param value The value of the pair, not null.
     * @throws NullPointerException if key or value are null.
     */
    public Pair(K key, V value) {
        this.key = Objects.requireNonNull(key, NULL_KEY_ERROR);
        this.value = Objects.requireNonNull(value, NULL_VALUE_ERROR);
    }

    /**
     * Returns the key of this pair.
     *
     * @return the key of this pair.
     */
    public K getKey() {
        return this.key;
    }

    /**
     * Returns the value of this pair.
     *
     * @return the value of this pair.
     */
    public V getValue() {
        return this.value;
    }

    /**
     * Compares this Pair with another Object for equality.
     *
     * Two pairs are equal if and only if their keys are equal and their values
     * are equal.
     *
     * @param object An Object to test for equality.
     * @return true if and only if object is not null, is a Pair, their keys are
     *         equal and their values are equal.
     */
    @Override public boolean equals(Object object) {
        if (object == this)
            return true;
        if (object == null)
            return false;
        if (!(object instanceof Pair))
            return false;
        Pair pair = (Pair)object;
        return pair.key.equals(this.key) && pair.value.equals(this.value);
    }
}
