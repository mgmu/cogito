# Technical documentation
## Graph files (`.gr`)
A graph file is a text file that specifies the structure of a directed graph as
follows:  
- The file starts with the name of the graph followed by the `\n` character.  
- The name is followed by the adjacency list that represents the graph, one line
per node in the graph. Each node is followed by the list of its neighbors.

Each node in the graph file is represented by its UUID. The list of neighbors of
a node consists of a comma-separated list of UUIDs, terminated by a new-line
character (`\n`).

Example of a graph file:
```
graph name
UUID1,UUID2,UUID3
UUID2
UUID3,UUID1
```
