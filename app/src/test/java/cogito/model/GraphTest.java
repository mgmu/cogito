package cogito.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

class GraphTest {

    @Test
    void containsWithNullThrowsNPE() {
        Graph sut = new Graph();
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> sut.contains(null));
        assertEquals("Node must not be null", npe.getMessage());
    }

    @Test
    void containsWithAbsentNodeReturnsFalse() {
        Graph sut = new Graph();
        assertFalse(sut.contains(new Node("test")));
    }

    @Test
    void addWithNullThrowsNPE() {
        Graph sut = new Graph();
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> sut.add(null));
        assertEquals("Node must not be null", npe.getMessage());
    }

    @Test
    void addTwiceThrowsIAE() {
        Graph sut = new Graph();
        Node node = new Node("test");
        sut.add(node);
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> sut.add(node)
        );
        assertEquals("Node already in graph", iae.getMessage());
    }

    @Test
    void sizeOfNewGraphIs0() {
        Graph sut = new Graph();
        assertEquals(0, sut.size());
    }

    @Test
    void sizeOfGraphWith1NodeIs1() {
        Graph sut = new Graph();
        sut.add(new Node("test"));
        assertEquals(1, sut.size());
    }

    @Test
    void removeWithNullNodeThrowsNPE() {
        Graph sut = new Graph();
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> sut.remove(null));
        assertEquals("Node must not be null", npe.getMessage());
    }

    @Test
    void removeWithAbsentNodeThrowsIAE() {
        Graph sut = new Graph();
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> sut.remove(new Node("test"))
        );
        assertEquals("Node not in graph", iae.getMessage());
    }

    @Test
    void sizeOfGraphWith1NodeAfterRemovalIs0() {
        Graph sut = new Graph();
        Node node = new Node("test");
        sut.add(node);
        assertEquals(1, sut.size());
        sut.remove(node);
        assertEquals(0, sut.size());
    }

    @Test
    void graphContainsCorrectNodesAfterRemoval() {
        Graph sut = new Graph();
        Node n1 = new Node("1");
        Node n2 = new Node("2");
        Node n3 = new Node("3");
        sut.add(n1);
        sut.add(n2);
        sut.add(n3);
        assertEquals(3, sut.size());
        assertTrue(sut.contains(n1));
        assertTrue(sut.contains(n2));
        assertTrue(sut.contains(n3));
        sut.remove(n2);
        assertEquals(2, sut.size());
        assertTrue(sut.contains(n1));
        assertTrue(sut.contains(n3));
        assertFalse(sut.contains(n2));
    }

    @Test
    void linkThrowsNPEIfSrcIsNull() {
        Graph sut = new Graph();
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> sut.link(null, new Node("test")));
        assertEquals("Node must not be null", npe.getMessage());
    }

    @Test
    void linkThrowsNPEIfDstIsNull() {
        Graph sut = new Graph();
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> sut.link(new Node("test"), null));
        assertEquals("Node must not be null", npe.getMessage());
    }

    @Test
    void linkThrowsIAEIfSrcIsAbsent() {
        Graph sut = new Graph();
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> sut.link(new Node("test"), new Node("test"))
        );
        assertEquals("Node not in graph", iae.getMessage());
    }

    @Test
    void linkThrowsIAEIfDstIsAbsent() {
        Graph sut = new Graph();
        Node node = new Node("test");
        sut.add(node);
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> sut.link(node, new Node("test"))
        );
        assertEquals("Node not in graph", iae.getMessage());
    }

    @Test
    void linkThrowsIAEIfSrcAndDstAreEqual() {
        Graph sut = new Graph();
        Node node = new Node("test");
        sut.add(node);
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> sut.link(node, node)
        );
        assertEquals("Node can not be linked to itself", iae.getMessage());
    }

    @Test
    void getNodesLinkedToWithNullThrowsNPE() {
        Graph sut = new Graph();
        NullPointerException npe = assertThrows(NullPointerException.class,
                () -> sut.getNodesLinkedTo(null));
        assertEquals("Node must not be null", npe.getMessage());
    }

    @Test
    void getNodesLinkedToAbsentNodeThrowsIAE() {
        Graph sut = new Graph();
        IllegalArgumentException iae = assertThrows(
          IllegalArgumentException.class,
          () -> sut.getNodesLinkedTo(new Node("absent"))
        );
        assertEquals("Node not in graph", iae.getMessage());
    }

    @Test
    void getNodesLinkedToNodeWithNoLinksReturnsTheEmptyList() {
        Graph sut = new Graph();
        Node node = new Node("test");
        sut.add(node);
        assertTrue(sut.getNodesLinkedTo(node).isEmpty());
    }

    @Test
    void getNodesLinkedToNodeWith1LinkReturnsAListOfSize1() {
        Graph sut = new Graph();
        Node n1 = new Node("test");
        Node n2 = new Node("test");
        sut.add(n1);
        sut.add(n2);
        sut.link(n1, n2);
        assertEquals(1, sut.getNodesLinkedTo(n1).size());
    }

    @Test
    void getNodesLinkedToNodeInCycleReturnsCorrectNeighborList() {
        Graph sut = new Graph();
        Node n1 = new Node("test");
        Node n2 = new Node("test");
        Node n3 = new Node("test");
        sut.add(n1);
        sut.add(n2);
        sut.add(n3);
        sut.link(n1, n2);
        sut.link(n2, n3);
        sut.link(n3, n1);
        List<Node> n1Neighbors = new ArrayList<>();
        n1Neighbors.add(n2);
        List<Node> n2Neighbors = new ArrayList<>();
        n2Neighbors.add(n3);
        List<Node> n3Neighbors = new ArrayList<>();
        n3Neighbors.add(n1);
        assertEquals(n1Neighbors, sut.getNodesLinkedTo(n1));
        assertEquals(n2Neighbors, sut.getNodesLinkedTo(n2));
        assertEquals(n3Neighbors, sut.getNodesLinkedTo(n3));
    }

    @Test
    void graphHasAUuidAfterCreation() {
        Graph sut = new Graph();
        assertNotNull(sut.getUuid());
    }
}
