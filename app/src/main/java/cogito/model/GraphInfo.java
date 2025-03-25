package cogito.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Encapsulates the name and the UUID of a graph.
 */
public record GraphInfo(String name, UUID identifier) {

    /**
     * Creates a new graph info object with given name and identifier.
     *
     * @param name The name of a graph, not null.
     * @param identifier The identifier of a graph, not null.
     * @throws NullPointerException if name or identifer are null.
     */
    public GraphInfo {
        Objects.requireNonNull(name, "Name can not be null.");
        Objects.requireNonNull(identifier, "Identifier can not be null.");
    }

    @Override
    public String toString() {
        return this.name;
    }
}
