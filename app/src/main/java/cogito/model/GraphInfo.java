package cogito.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Encapsulates the name and the UUID of a graph.
 */
public record GraphInfo(String name, UUID identifier) {

    public GraphInfo {
        Objects.requireNonNull(name, "Name can not be null.");
        Objects.requireNonNull(identifier, "Identifier can not be null.");
    }

    @Override
    public String toString() {
        return this.name;
    }
}
