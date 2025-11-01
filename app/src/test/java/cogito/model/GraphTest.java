package cogito.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import cogito.TestUtils;
import java.awt.Rectangle;

class GraphTest {
    Graph sut;

    @BeforeEach
    void createNewGraph() {
        sut = new Graph();
    }

    @Test
    void containsWithNullThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("Node must not be null",
                () -> sut.contains(null));
    }

    @Test
    void containsWithAbsentNodeReturnsFalse() {
        assertFalse(sut.contains(new Node("test")));
    }

    @Test
    void addWithNullThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("Node must not be null",
                () -> sut.add(null));
    }

    @Test
    void sizeOfNewGraphIs0() {
        assertEquals(0, sut.size());
    }

    @Test
    void removeWithNullNodeThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("Node must not be null",
                () -> sut.remove(null));
    }

    @Test
    void removeWithAbsentNodeThrowsIAE() {
        TestUtils.assertThrowsIAEWithMsg("Node not in graph",
                () -> sut.remove(new Node("test")));
    }

    @Test
    void graphContainsCorrectNodesAfterRemoval() {
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
        TestUtils.assertThrowsNPEWithMsg("Node must not be null",
                () -> sut.link(null, new Node("test")));
    }

    @Test
    void linkThrowsNPEIfDstIsNull() {
        TestUtils.assertThrowsNPEWithMsg("Node must not be null",
                () -> sut.link(new Node("test"), null));
    }

    @Test
    void linkThrowsIAEIfSrcIsAbsent() {
        TestUtils.assertThrowsIAEWithMsg("Node not in graph",
                () -> sut.link(new Node("test"), new Node("test")));
    }

    @Test
    void getNodesLinkedToWithNullThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("Node must not be null",
                () -> sut.getNodesLinkedTo(null));
    }

    @Test
    void getNodesLinkedToAbsentNodeThrowsIAE() {
        TestUtils.assertThrowsIAEWithMsg("Node not in graph",
                () -> sut.getNodesLinkedTo(new Node("absent")));
    }


    @Test
    void getNodesLinkedToNodeWith1LinkReturnsAListOfSize1() {
        Node n1 = new Node("test");
        Node n2 = new Node("test");
        sut.add(n1);
        sut.add(n2);
        sut.link(n1, n2);
        assertEquals(1, sut.getNodesLinkedTo(n1).size());
    }

    @Test
    void getNodesLinkedToNodeInCycleReturnsCorrectNeighborList() {
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
        assertNotNull(sut.getUuid());
    }

    @Test
    void updateNullObserverThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("Observer can not be null",
                () -> sut.update(null));
    }

    @Test
    void subscribeWithNullThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("Observer can not be null",
                () -> sut.subscribe(null));
    }

    @Test
    void unsubscribeWithNullThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("Observer can not be null",
                () -> sut.unsubscribe(null));
    }

    @Test
    void getNodesReturnsEmptyListOnEmptyGraph() {
        assertTrue(sut.getNodes().isEmpty());
    }

    @Test
    void getNodesOnGraphWithTwoNodesReturnsListOfSizeTwo() {
        Node n1 = new Node("test");
        Node n2 = new Node("test");
        sut.add(n1);
        sut.add(n2);
        Set<Node> nodes = sut.getNodes();
        assertEquals(2, nodes.size());
        assertTrue(nodes.contains(n1));
        assertTrue(nodes.contains(n2));
    }

    @Test
    void getNodeAtWithNegativeRadiusThrowsIAE() {
        TestUtils.assertThrowsIAEWithMsg(
          "Radius must be greater than or equal to 0",
          () -> sut.getNodeAt(0, 0, -1)
        );
    }

    @Test
    void getNodeAtWithValidLocationReturnsNull() {
        assertNull(sut.getNodeAt(0, 0, 0));
    }

    @Test
    void getNameOfUnamedGraphReturnsUuid() {
        assertEquals(sut.getUuid().toString(), sut.getName());
    }

    @Test
    void newGraphWithNullNameThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg(
          "Name must be not null",
          () -> new Graph(null)
        );
    }

    @Test
    void newGraphWithEmpyNameThrowsIAE() {
        TestUtils.assertThrowsIAEWithMsg(
          "Name must not be empty",
          () -> new Graph("")
        );
    }

    @Test
    void newGraphWithNameLongerThan100CharsThrowsIAE() {
        TestUtils.assertThrowsIAEWithMsg(
          "Name length must be less or equal to 100",
          () -> {
              String name = "";
              for (int i = 0; i <= 100; i++)
                  name += "a";
              new Graph(name);
          }
        );
    }

    @Test
    void unlinkWithNullSrcThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg(
          "Source must be not null",
          () -> sut.unlink(null, null)
        );
    }

    @Test
    void unlinkWithNullDstThrowsNPE() {
        Node node = new Node("test");
        sut.add(node);
        TestUtils.assertThrowsNPEWithMsg(
          "Destination must be not null",
          () -> sut.unlink(node, null)
        );
    }

    @Test
    void unlinkWithAbsentSrcThrowsIAE() {
        TestUtils.assertThrowsIAEWithMsg(
          "Node not in graph",
          () -> sut.unlink(new Node("4"), new Node("2"))
        );
    }

    @Test
    void unlinkWithAbsentDstThrowsIAE() {
        Node node = new Node("42");
        sut.add(node);
        TestUtils.assertThrowsIAEWithMsg(
          "Node not in graph",
          () -> sut.unlink(node, new Node("2"))
        );
    }

    @Test
    void unlinkWithSameNodeThrowsIAE() {
        Node node = new Node("42");
        sut.add(node);
        TestUtils.assertThrowsIAEWithMsg(
          "Node can not be unlinked of itself",
          () -> sut.unlink(node, node)
        );
    }

    @Test
    void unlinkSrcFromDstRemovesNeighbor() {
        Node n1 = new Node("42");
        Node n2 = new Node("what is the question?");
        sut.add(n1);
        sut.add(n2);
        sut.link(n1, n2);
        sut.unlink(n1, n2);
        assertTrue(sut.getNodesLinkedTo(n1).isEmpty());
    }

    @Nested
    class AfterAddingANode{
        Node node;

        @BeforeEach
        void addANode() {
            node = new Node("test");
            sut.add(node);
        }

        @Test
        void addTwiceThrowsIAE() {
            TestUtils.assertThrowsIAEWithMsg("Node already in graph",
                    () -> sut.add(node));
        }

        @Test
        void sizeOfGraphWith1NodeIs1() {
            assertEquals(1, sut.size());
        }

        @Test
        void sizeOfGraphWith1NodeAfterRemovalIs0() {
            assertEquals(1, sut.size());
            sut.remove(node);
            assertEquals(0, sut.size());
        }

        @Test
        void linkThrowsIAEIfDstIsAbsent() {
            TestUtils.assertThrowsIAEWithMsg("Node not in graph",
                    () -> sut.link(node, new Node("test")));
        }

        @Test
        void linkThrowsIAEIfSrcAndDstAreEqual() {
            TestUtils.assertThrowsIAEWithMsg(
              "Node can not be linked to itself",
              () -> sut.link(node, node)
            );
        }

        @Test
        void getNodesLinkedToNodeWithNoLinksReturnsTheEmptyList() {
            assertTrue(sut.getNodesLinkedTo(node).isEmpty());
        }

        @Test
        void getNodeAtExactLocationOfNodeReturnsNode() {
            Node other = sut.getNodeAt(0, 0, 0);
            assertEquals(node, other);
        }

        @Test
        void getNodeAtVoidLocationReturnsNull() {
            Node other1 = new Node("title", 15, 15);
            assertNull(sut.getNodeAt(50, 50, 1));
        }
    }

    @Nested
    class GetNodesInRectangle {

        @Test
        void WhenRectIsNullThenThrowsNPE() {
            TestUtils.assertThrowsNPEWithMsg(
              "Rectangle cannot be null",
              () -> sut.getNodesInRectangle(null)
            );
        }

        @Test
        void WhenRectIsPointThenReturnsEmptySet() {
            Set<Node> res = sut.getNodesInRectangle(new Rectangle(0, 0, 0, 0));
            assertTrue(res.isEmpty());
        }

        @Test
        void WhenRectCoversAllGraphReturnsAllNodesOfGraph() {
            Set<Node> res = sut.getNodesInRectangle(
              new Rectangle(0, 0, 1000, 1000)
            );
            assertEquals(sut.getNodes(), res);
        }

        @Test
        void WhenRectCoversAllComplexGraphReturnsAllNodesOfGraph() {
            sut.add(new Node("n1", 1, 2));
            sut.add(new Node("n2", 3, 4));
            sut.add(new Node("n3", 5, 6));
            sut.add(new Node("n4", 7, 8));
            Set<Node> res = sut.getNodesInRectangle(new Rectangle(0, 0, 9, 9));
            assertEquals(sut.getNodes(), res);
        }

        @Test
        void WhenRectIsAtPositionOfNodeReturnsEmptySet() {
            sut.add(new Node("n1", 0, 0));
            Set<Node> res = sut.getNodesInRectangle(new Rectangle(0, 0, 0, 0));
            assertTrue(res.isEmpty());
        }
    }

    @Test
    void getSubGraphInRectangleWithNullRectangleThrowsNPE() {
        TestUtils.assertThrowsNPEWithMsg("Rectangle cannot be null",
                () -> sut.getSubGraphInRectangle(null));
    }

    @Test
    void getSubGraphOnEmptyGraphReturnsEmptyMap() {
        Map<Node, ArrayList<Node>> adj = sut.getSubGraphInRectangle(
          new Rectangle());
        assertTrue(adj.isEmpty());
    }

    @Test
    void getSubGraphWithComplexGraph() {
        // outside initially
        Node n1 = new Node("n1", 1000, -500);
        Node n2 = new Node("n2", 1500, -300);

        // inside initially
        Node n3 = new Node("n3", 500, 500);
        Node n4 = new Node("n4", 1250, 750);

        sut.add(n1);
        sut.add(n2);
        sut.add(n3);
        sut.add(n4);

        /*
          n1    n2
          |  \  +
          +   + |
          n3 -> n4
         */
        sut.link(n3, n4);
        sut.link(n4, n2);
        sut.link(n1, n3);
        sut.link(n1, n4);

        Rectangle r1 = new Rectangle(0, 0, 1920, 1080);
        Map<Node, ArrayList<Node>> subgraph = sut.getSubGraphInRectangle(r1);
        for (Node node: subgraph.keySet())
            System.out.println(node.getTitle());
        assertEquals(3, subgraph.keySet().size());
    }

    @Nested
    class NamedGraph {

        @BeforeEach
        void createNamedGraph() {
            sut = new Graph("test");
        }

        @Test
        void getNameReturnsTest() {
            assertEquals("test", sut.getName());
        }
    }
}
