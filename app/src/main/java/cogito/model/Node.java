package cogito.model;

import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import cogito.view.Observer;

/**
 * Encapsulates text entries related to a title.
 *
 * The title of a Node is mandatory, must be not empty and have a length of at
 * most 100 characters. A Node can encapsulate at most 500 entries, each of them
 * must be not empty and have a length of at most 500 characters.
 */
public class Node implements Observable {

    // The title, length inside [1; 100]
    private String title;

    // The entries, size inside [0; 500], each entry length inside [1; 500]
    private final List<String> entries;

    // The observers subscribed to this Node updates
    private final List<Observer> observers;

    // The universally unique identifier of this Node
    private final UUID identifier;

    // Minimal title length 
    private static final int MIN_TITLE_LEN = 1;
    
    // Maximal title length
    private static final int MAX_TITLE_LEN = 100;

    // Minimal entry length
    private static final int MIN_ENTRY_LEN = 1;

    // Maximal entry length
    private static final int MAX_ENTRY_LEN = 500;

    // Maximal number of entries per node
    private static final int MAX_NB_ENTRIES = 500;

    // Error message to show when title is too short
    private static final String SHORT_TITLE_ERROR =
        "Node title can not be empty";

    // Error message to show when title is too long
    private static final String LONG_TITLE_ERROR =
        "Node title can not be longer than 100 characters";

    // Error message to show when title is null
    private static final String NULL_TITLE_ERROR = "Node title can not be null";

    // Error message to show when the list of entries contains a null element
    private static final String NULL_LIST_ENTRY_ERROR =
        "New node can not be initialized with null list";

    // Error message to show when a null entry addition is attempted
    private static final String NULL_ENTRY_ERROR = "Entry can not be null";

    // Error message to show when an entry is added at full capacity
    private static final String MAX_NB_ENTRIES_ERROR =
        "Node can contain at most 500 entries";

    // Error message to show when an entry is too short
    private static final String SHORT_ENTRY_ERROR = "Entry can not be empty";

    // Error message to show when an entry is too long
    private static final String LONG_ENTRY_ERROR =
        "Entry length can not exceed 500 chars";

    // Error message to show when an observer is null
    private static final String NULL_OBSERVER_ERROR =
        "Observer can not be null";

    // Error message to show when an observer is subscribed twice
    private static final String ALREADY_SUBSCRIBED_ERROR =
        "Observer already subscribed";

    // Error message to show when an observer is not subscribed
    private static final String ABSENT_OBSERVER_ERROR =
        "Observer not subscribed";

    /**
     * Creates a Node of given title with no entries.
     *
     * @param title A string of length greater or equal to 1 and inferior or
     *        equal to 100, not null.
     * @throws NullPointerException if title is null.
     * @throws IllegalArgumentException if title length is less than 1 or
               greater than 100.
     */
    public Node(String title) {
        checkTitleValidity(title);
        this.title = title;
        this.entries = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.identifier = UUID.randomUUID();
    }

    /**
     * Creates a Node of given title and entries.
     *
     * @param title A string of length greater or equal to 1 and inferior or
     *        equal to 100, not null.
     * @param entries A list that contains at most 500 strings, each of length
     *        greater than 0 and less than or equal to 500.
     * @throws NullPointerException if title or entries is null, or if an entry
     *         is null.
     * @throws IllegalArgumentException if title length is less than 1 or
     *         greater than 100, or if at least one entry is empty or has a
     *         length greater than 500 chars, or if the list of entries has more
     *         than 500 elements.
     */
    public Node(String title, List<String> entries) {
        this(title);
        Objects.requireNonNull(entries, NULL_LIST_ENTRY_ERROR);
        if (entries.size() > MAX_NB_ENTRIES)
            throw new IllegalArgumentException(MAX_NB_ENTRIES_ERROR);
        for (String entry: entries)
            checkEntryValidity(entry);
        this.entries.addAll(entries);
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
     * Sets a new title.
     *
     * @param newTitle A string of length greater or equal to 1 and inferior or
     *        equal to 100, not null.
     * @throws NullPointerException if newTitle is null.
     * @throws IllegalArgumentException if newTitle length is less than 1 or
     *         greater than 100.
     */
    public void setTitle(String newTitle) {
        checkTitleValidity(newTitle);
        if (!this.title.equals(newTitle)) {
            this.title = newTitle;
            this.updateObservers();
        }
    }

    /**
     * Returns the entries of this Node.
     *
     * @return A list of strings of at most 500 elements, each string of length
     *         greater than 0 and less or equal to 500.
     */
    public List<String> getEntries() {
        List<String> copyEntries = new ArrayList<>();
        copyEntries.addAll(this.entries);
        return copyEntries;
    }

    /**
     * Adds an entry to this Node.
     *
     * @param newEntry A string of length greater than 0 and less or equal to
     *        500, not null.
     * @throws NullPointerException if newEntry is null.
     * @throws IllegalArgumentException if newEntry has not a length greater
     *         than 0 and less or equal 500.
     */
    public void add(String newEntry) {
        checkEntryValidity(newEntry);
        this.entries.add(newEntry);
        this.updateObservers();
    }

    /**
     * Removes the entry at given index.
     *
     * The first entry of this Node is at index 0.
     *
     * @param index An integer greater than 0 and less than getEntries().size().
     * @throws IllegalArgumentException if index is out of bounds.
     */
    public void remove(int index) {
        if (index < 0 || index >= this.entries.size())
            throw new IllegalArgumentException("Invalid entry index");
        this.entries.remove(index);
        this.updateObservers();
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
        observer.update(this);
    }

    @Override
    public void updateObservers() {
        for (Observer obs: this.observers)
            obs.update(this);
    }

    /**
     * Returns the UUID of this Node.
     *
     * @return A randomly generated universally unique identifier.
     */
    public UUID getUuid() {
        return this.identifier;
    }

    // Checks that newEntry length is in bounds and not null.
    private void checkEntryValidity(String newEntry) {
        checkStringValidity(newEntry, MIN_ENTRY_LEN, MAX_ENTRY_LEN,
                NULL_ENTRY_ERROR, SHORT_ENTRY_ERROR, LONG_ENTRY_ERROR);
    }

    // Checks that title length is in bounds and not null.
    private void checkTitleValidity(String title) {
        checkStringValidity(title, MIN_TITLE_LEN, MAX_TITLE_LEN,
                NULL_TITLE_ERROR, SHORT_TITLE_ERROR, LONG_TITLE_ERROR);
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
}
