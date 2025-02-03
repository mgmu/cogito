package cogito.view;

public interface Observer {

    /**
     * Updates this Observer with the specified object.
     *
     * @param object An object, not null, must be an instance of Graph.
     * @throws NullPointerException if object is null.
     * @throws IllegalArgumentException if object is not a Graph.
     */
    public void updateWithData(Object object);
}
