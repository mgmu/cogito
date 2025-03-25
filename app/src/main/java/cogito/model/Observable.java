package cogito.model;

import cogito.view.Observer;

/**
 * An observable objects accepts other objects to be updated when its state
 * changes.
 */
public interface Observable {

    /**
     * Subscribes the specified Observer to this Observable's observers.
     *
     * If observer is already subscribed, throws an IllegalArgumentException.
     *
     * @param observer The Observer to add, not null.
     * @throws NullPointerException if observer is null.
     * @throws IllegalArgumentException if observer is already subscribed to
     *         this Observable.
     */
    public void subscribe(Observer observer);

    /**
     * Unsubscribes the specified Observer from this Observable's observers.
     *
     * @param observer The Observer  to remove, not null.
     * @throws NullPointerException if observer is null.
     */
    public void unsubscribe(Observer observer);

    /**
     * Updates the specified Observer.
     *
     * The other observers, if there is any, of this Observable are not updated.
     *
     * @param observer The Observer to update, not null, must currently observe
     *        this Observable.
     * @throws NullPointerException if observer is null.
     * @throws IllegalArgumentException if observer is not currently observing
     *         this Observable.
     */
    public void update(Observer observer);

    /**
     * Updates all the observers of this Observable.
     */
    public void updateObservers();
}
