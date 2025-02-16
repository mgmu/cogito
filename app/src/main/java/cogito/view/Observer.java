package cogito.view;

public interface Observer {

    /**
     * Updates this Observer with the specified object.
     *
     * @param object An object, not null.
     * @throws NullPointerException if object is null.
     */
    public void updateWithData(Object object);
}
