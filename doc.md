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

## Installers
### Windows
In a Windows command prompt, in the directory that contains the `.jar` file:

```bash
jpackage --input . \
         --name "Cogito" \
         --main-jar app.jar \
         --main-class cogito.Main \
         --type exe \
         --win-dir-chooser \
         --win-menu \
         --win-shortcut
         --app-version <VERSION>
```

Options:

- `--input .`: assembles everyting in the current directory.

- `--name "Cogito": names the executable "Cogito".

- `--main-jar app.jar`: specifies the name of `.jar` file to assemble.

- `--main-class cogito.Main`: specifies the name of main class.

- `--type exe`: creates a `.exe` executable.

- `--win-dir-chooser`: lets the user specify the installation directory.

- `--win-menu`: adds a shortcut to Start applications.

- `--win-shortcut`: creates a desktop shortcut.

- `--app-version <VERSION>`: sets the app version to `<VERSION>`

### Debian-based distributions

```bash
jpackage --input . \
         --name "Cogito" \
         --main-jar app.jar \
         --main-class cogito.Main \
         --type deb \
         --app-version <VERSION>
```