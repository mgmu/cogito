package cogito.model;

import java.util.Objects;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import cogito.view.Observer;

/**
 * Encapsulates textual information related to a title, at a certain position in
 * the graph space.
 *
 * The title of a Node is mandatory, must be not empty and have a length of at
 * most 100 characters. A Node can encapsulate at most 5000 characters of
 * textual information.
 */
public class Node implements Observable {

    // The title, length inside [1; 100]
    private String title;

    // The textual information stored in this Node
    private String information;

    // The x coordinate of this Node in the graph space
    private int x;

    // The y coordinate of this Node in the graph space
    private int y;

    // The observers subscribed to this Node updates
    private final List<Observer> observers;

    // The universally unique identifier of this Node
    private final UUID identifier;

    // Minimal title length 
    private static final int MIN_TITLE_LEN = 1;
    
    // Maximal title length
    private static final int MAX_TITLE_LEN = 100;

    // Minimal textual information length
    private static final int MIN_TEXT_LEN = 0;

    // Maximal textual information length
    private static final int MAX_TEXT_LEN = 5000;

    // Error messages
    private static final String SHORT_TITLE_ERROR =
        "Node title can not be empty";
    private static final String LONG_TITLE_ERROR =
        "Node title can not be longer than 100 characters";
    private static final String NULL_TITLE_ERROR = "Node title can not be null";
    private static final String NULL_TEXT_ERROR =
        "Textual information can not be null";
    private static final String LONG_TEXT_ERROR =
        "Text length can not exceed 5000 characters";
    private static final String NULL_OBSERVER_ERROR =
        "Observer can not be null";
    private static final String ALREADY_SUBSCRIBED_ERROR =
        "Observer already subscribed";
    private static final String ABSENT_OBSERVER_ERROR =
        "Observer not subscribed";

    /**
     * Creates a Node of given title with no text information, at (0, 0).
     *
     * @param title A string of length greater or equal to 1 and inferior or
     *        equal to 100, not null.
     * @throws NullPointerException if title is null.
     * @throws IllegalArgumentException if title length is less than 1 or
     *          greater than 100.
     */
    public Node(String title) {
        checkTitleValidity(title);
        this.title = title;
        this.information = "";
        this.observers = new ArrayList<>();
        this.identifier = UUID.randomUUID();
        this.x = 0;
        this.y = 0;
    }

    /**
     * Creates a Node of given title with text information, at (x, y).
     *
     * @param title A string of length greater or equal to 1 and inferior or
     *        equal to 100, not null.
     * @param x The x coordinate of this Node in the graph space.
     * @param y The y coordinate of this Node in the graph space.
     * @throws NullPointerException if title is null.
     * @throws IllegalArgumentException if title length is less than 1 or
     *         greater than 100.
     */
    public Node(String title, int x, int y) {
        this(title);
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a Node of given title and text information.
     *
     * @param title A string of length greater or equal to 1 and inferior or
     *        equal to 100, not null.
     * @param information A string of length of at most 5000 characters, not
     *        null.
     * @throws NullPointerException if title or information is null.
     * @throws IllegalArgumentException if title length is less than 1 or
     *         greater than 100 or if information length is greater than 5000
     *         characters.
     */
    public Node(String title, String information) {
        this(title);
        checkInformationValidity(information);
        this.information = information;
    }

    /**
     * Creates a Node of given title, information, position and identifier.
     *
     * @param title The title of the node.
     * @param information The information of the node.
     * @param x The x position in the graph space of the node.
     * @param y The y position in the grpah space of the node.
     * @param id The UUID of the node.
     */
    public Node(String title, String information, int x, int y, UUID id) {
        this.title = title;
        this.information = information;
        this.x = x;
        this.y = y;
        this.identifier = id;
        this.observers = new ArrayList<>();
    }

    /**
     * Returns the title of this Node.
     *
     * @return The title, a string of length greater than 0 and less or equal
     *         than 100 characters.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the new title.
     *
     * @param newTitle A string of length greater or equal to 1 and inferior or
     *        equal to 100, not null.
     * @throws NullPointerException if newTitle is null.
     * @throws IllegalArgumentException if newTitle length is less than 1 or
     *         greater than 100.
     */
    public void setTitle(String newTitle) {
        checkTitleValidity(newTitle);
        this.title = newTitle;
    }

    /**
     * Returns the textual information encapsulated by this Node.
     *
     * @return The information stored in this Node as a string.
     */
    public String getInformation() {
        return this.information;
    }

    /**
     * Sets the new value of the information that this Node holds.
     *
     * @param newInformation The new information, limited to 5000 chars, not
     *        null.
     * @throws NullPointerException if newInformation is null.
     * @throws IllegalArgumentException if newInformation length is greater than
     *         5000 chars.
     */
    public void setInformation(String newInformation) {
        checkInformationValidity(newInformation);
        this.information = newInformation;
    }

    @Override
    public void subscribe(Observer observer) {
        Objects.requireNonNull(observer, NULL_OBSERVER_ERROR);
        if (observers.contains(observer))
            throw new IllegalArgumentException(ALREADY_SUBSCRIBED_ERROR);
        this.observers.add(observer);
    }

    @Override
    public void unsubscribe(Observer observer) {
        Objects.requireNonNull(observer, NULL_OBSERVER_ERROR);
        this.observers.remove(observer);
    }

    @Override
    public void update(Observer observer) {
        Objects.requireNonNull(observer, NULL_OBSERVER_ERROR);
        if (!this.observers.contains(observer))
            throw new IllegalArgumentException(ABSENT_OBSERVER_ERROR);
        observer.updateWithData(this);
    }

    @Override
    public void updateObservers() {
        for (Observer obs: this.observers)
            obs.updateWithData(this);
    }

    /**
     * Returns the UUID of this Node.
     *
     * @return A randomly generated universally unique identifier.
     */
    public UUID getUuid() {
        return this.identifier;
    }

    /**
     * Returns the x coordinate of this Node in the graph space.
     *
     * @return the x coordiante of this Node in the graph space.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Sets the x coordinate of this Node in the graph space to newX.
     *
     * @param newX The new x coordinate of this Node in the graph space.
     */
    public void setX(int newX) {
        this.x = newX;
    }

    /**
     * Returns the y coordinate of this Node in the graph space.
     *
     * @return the y coordiante of this Node in the graph space.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Sets the y coordinate of this Node in the graph space to newX.
     *
     * @param newY The new y coordinate of this Node in the graph space.
     */
    public void setY(int newY) {
        this.y = newY;
    }

    /**
     * Computes the distance between this node's position and the given
     * position, rounded to the -2 power of 10.
     *
     * @param x The x coordinate of the other position.
     * @param y The y coordinate of the other position.
     * @return The distance in double precision between the two locations.
     */
    public double distanceFrom(int x, int y) {
        double dist = Math.sqrt(Math.pow(x - this.x, 2)
                + Math.pow(y - this.y, 2));
        return (int)(100 * dist) / 100.0;
    }

    // Checks that title length is in bounds and not null.
    private void checkTitleValidity(String title) {
        checkStringValidity(title, MIN_TITLE_LEN, MAX_TITLE_LEN,
                NULL_TITLE_ERROR, SHORT_TITLE_ERROR, LONG_TITLE_ERROR);
    }

    // Checks that information length is in bounds and not null.
    private void checkInformationValidity(String information) {
        checkStringValidity(information, MIN_TEXT_LEN, MAX_TEXT_LEN,
                NULL_TEXT_ERROR, null, LONG_TEXT_ERROR);
    }

    // Checks that str length is greater than or equal to lb, less than or equal
    // to ub and not null. If null, throws a NPE with errorMsgWhenNull. If out
    // of bounds, throws a IAE with corresponding error message.
    private void checkStringValidity(String str, int lb, int ub,
            String errorMsgWhenNull, String errorMsgWhenTooShort,
            String errorMsgWhenTooLong) {
        Objects.requireNonNull(str, errorMsgWhenNull);
        int length = str.length();
        if (length < lb)
            throw new IllegalArgumentException(errorMsgWhenTooShort);
        if (length > ub)
            throw new IllegalArgumentException(errorMsgWhenTooLong);
    }

    /**
     * Returns the position of this Node as a String.
     *
     * @return A string of this format "X,Y".
     */
    public String getPositionAsString() {
        return this.x + "," + this.y;
    }
}
