package cogito.util;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.CopyOption;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.FileVisitResult;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import cogito.model.Graph;
import cogito.model.GraphInfo;
import cogito.model.Node;

/**
 * Encapsulates convenient methods for storing data of the application.
 */
public class DataManager {
    
    private static final String HOME_DIR = System.getProperty("user.home");
    private static final String INSTAL_DIR_NAME = ".cogito";
    private static final String GRAPHS_DIR_NAME = "graphs";
    private static final Path INSTAL_DIR = Paths.get(HOME_DIR, INSTAL_DIR_NAME);
    private static final Path GRAPHS_DIR = INSTAL_DIR.resolve(GRAPHS_DIR_NAME);
    private static final Charset CHARSET = StandardCharsets.UTF_16;

    /**
     * Returns the path of the installation directory.
     *
     * @return A Path denoting the installation directory.
     */
    public static Path getInstalDir() {
        return INSTAL_DIR;
    }

    /**
     * Returns the path of the graphs directory.
     *
     * @return A Path denoting the graphs directory.
     */
    public static Path getGraphsDir() {
        return GRAPHS_DIR;
    }

    /**
     * Saves model locally, in the graph directory.
     *
     * @param model The graph model to save.
     * @throws IOException if an I/O error occurred.
     */
    public static void saveGraph(Graph model) throws IOException {
        String graphUuid = model.getUuid().toString();
        Path graphModelDir = GRAPHS_DIR.resolve(graphUuid);
        if (Files.exists(graphModelDir)) {
            String tmpDirName = System.getProperty("java.io.tmpdir");
            Path graphSaveDir = Paths.get(tmpDirName, graphUuid);
            copyFolder(graphModelDir, graphSaveDir);
            deleteFolder(graphModelDir);
            Files.createDirectory(graphModelDir);
            boolean succ = writeGraphData(
              graphModelDir,
              CHARSET,
              model
            );
            if (!succ) {
                deleteFolder(graphModelDir);
                copyFolder(graphSaveDir, graphModelDir);
            }
            deleteFolder(graphSaveDir);
        } else {
            Files.createDirectory(graphModelDir);
            writeGraphData(
              graphModelDir,
              CHARSET,
              model
            );
        }
    }

    // Writes .gr file and node directories
    private static boolean writeGraphData(
      Path dir,
      Charset charset,
      Graph model
    ) throws IOException {
        // create gr file
        Path grFile = dir.resolve(model.getUuid().toString() + ".gr");
        if (!writeToFile(grFile, charset, model.toString()))
            return false;

        // create node dir for each node
        Set<Node> nodes = model.getNodes();
        for (Node node: nodes) {
            String nodeUuid = node.getUuid().toString();
            Path nodeDir = dir.resolve(nodeUuid);
            Files.createDirectory(nodeDir);
            String data = node.getTitle();
            if (!createAndWriteToFile(nodeDir, charset, "title", data))
                return false;
            data = node.getInformation();
            if (!createAndWriteToFile(nodeDir, charset, "info", data))
                return false;
            data = node.getPositionAsString() + "\n";
            if (!createAndWriteToFile(nodeDir, charset, "position", data))
                return false;
        }
        return true;
    }

    private static boolean createAndWriteToFile(
      Path dir,
      Charset charset,
      String filename,
      String data
    ) throws IOException {
        Path path = dir.resolve(filename);
        Files.createFile(path);
        if (!writeToFile(path, charset, data))
            return false;
        return true;
    }

    private static boolean writeToFile(
      Path file,
      Charset charset,
      String data
    ) {
        boolean res = true;
        try {
            BufferedWriter writer = Files.newBufferedWriter(file, charset);
            writer.write(data, 0, data.length());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            res = false;
        } finally {
            return res;
        }        
    }

    // courtesy of https://stackoverflow.com/a/60621544
    private static void copyFolder(
      Path source,
      Path target,
      CopyOption... options
    ) throws IOException {
        Files.walkFileTree(
          source,
          new SimpleFileVisitor<Path>() {
              @Override
              public FileVisitResult preVisitDirectory(Path dir,
                      BasicFileAttributes attrs) throws IOException {
                  Files.createDirectories(
                    target.resolve(source.relativize(dir).toString())
                  );
                  return FileVisitResult.CONTINUE;
              }

              @Override
              public FileVisitResult visitFile(Path file,
                      BasicFileAttributes attrs) throws IOException {
                  Files.copy(
                    file,
                    target.resolve(source.relativize(file).toString()),
                    options
                  );
                  return FileVisitResult.CONTINUE;
              }
          }
        );
    }

    // deletes a file, in case of a directory, recursively deletes its content
    private static void deleteFolder(Path source) throws IOException {
        Files.walkFileTree(
          source,
          new SimpleFileVisitor<Path>() {
              @Override
              public FileVisitResult postVisitDirectory(Path dir,
                      IOException ioe) throws IOException {
                  Files.delete(dir);
                  return FileVisitResult.CONTINUE;
              }

              @Override
              public FileVisitResult visitFile(Path file,
                      BasicFileAttributes attrs) throws IOException {
                  Files.delete(file);
                  return FileVisitResult.CONTINUE;
              }
          }
        );
    }

    /**
     * Returns the list of saved graph informations.
     *
     * Searches for graphs saved locally and returns their names and
     * identifiers.
     *
     * @return A list of graph information objects.
     * @throws Exception if an I/O error occurred, in which case it is an
     *         IOException, or if an error occurred while iterating over a
     *         directory, in which case it is a DirectoryIteratorException.
     */
    public static List<GraphInfo> getSavedGraphInfos() throws Exception {
        Exception thrown = null;
        List<GraphInfo> graphInfos = new ArrayList<>();
        try (
          DirectoryStream<Path> stream = Files.newDirectoryStream(GRAPHS_DIR)
        ) {
            for (Path path: stream) {
                File file = path.toFile();
                if (file.isDirectory()) { // ignore other files
                    Path grFile = path.resolve(file.getName() + ".gr");
                    try (
                      BufferedReader reader = Files.newBufferedReader(
                        grFile,
                        CHARSET
                      )
                    ) {
                        String name = reader.readLine();
                        GraphInfo gi = new GraphInfo(
                          name,
                          UUID.fromString(file.getName())
                        );
                        graphInfos.add(gi);
                    } catch (IOException ioe) {
                        thrown = ioe;
                        break;
                    }
                }
            }
        } catch (IOException | DirectoryIteratorException x) {
            thrown = x;
        }
        if (thrown != null)
            throw thrown;
        return graphInfos;
    }

    /**
     * Returns the graph of given identifier from local storage.
     *
     * @param identifier The identifier of the graph to return.
     * @return The graph of given identifier.
     * @throws IOException if an I/O error occurred.
     */
    public static Graph loadGraph(UUID identifier) throws IOException {
        String id = identifier.toString();
        String modelName = null;
        Path modelDir = GRAPHS_DIR.resolve(id);
        Path modelGrFile = modelDir.resolve(id + ".gr");
        Map<String, List<String>> tmpAdj = new HashMap<>();

        // read .gr file
        BufferedReader reader = Files.newBufferedReader(
          modelGrFile,
          CHARSET
        );
        modelName = reader.readLine();
        // throw x if name is null
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] content = line.split(",");
            // check that content has at least size one
            List<String> neighbors = new ArrayList<>();
            if (content.length > 1) {
                for (int i = 1; i < content.length; i++)
                    neighbors.add(content[i]);
            }
            tmpAdj.put(content[0], neighbors);
        }
        reader.close();

        // load nodes
        Graph model = new Graph(modelName, identifier);
        for (String nodeUuid: tmpAdj.keySet()) {
            Node curr = model.getNode(nodeUuid);
            if (curr == null) {
                curr = loadNode(modelDir, nodeUuid);
                model.add(curr);
            }
            for (String neighborUuid: tmpAdj.get(nodeUuid)) {
                Node neighbor = model.getNode(neighborUuid);
                if (neighbor == null) {
                    neighbor = loadNode(modelDir, neighborUuid);
                    model.add(neighbor);
                }
                model.link(curr, neighbor);
            }
        }
        return model;
    }

    private static Node loadNode(
      Path graphPath,
      String identifier
    ) throws IOException {
        Path nodeDir = graphPath.resolve(identifier);
        
        // read title
        Path nodeTitleFile = nodeDir.resolve("title");
        String title = readUTF16FileContent(nodeTitleFile, 100);

        // read information
        Path nodeInfoFile = nodeDir.resolve("info");
        String information = readUTF16FileContent(nodeInfoFile, 5000);
            
        // read position
        Path nodePosFile = nodeDir.resolve("position");
        int[] pos = readPositionFile(nodePosFile);

        return new Node(
          title,
          information,
          pos[0],
          pos[1],
          UUID.fromString(identifier)
        );
    }

    private static String readUTF16FileContent(
      Path path,
      int len
    ) throws IOException {
        BufferedReader reader = Files.newBufferedReader(
          path,
          CHARSET
        );
        char[] buf = new char[len];
        reader.read(buf, 0, len);
        reader.close();
        String data = new String(buf).trim();
        return data;
    }

    private static int[] readPositionFile(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(
          path,
          CHARSET
        );
        int[] pos = new int[2];
        String line = reader.readLine();
        if (line == null)
            return null; // improve this
        String[] split = line.split(",");
        pos[0] = Integer.parseInt(split[0]);
        pos[1] = Integer.parseInt(split[1]);
        return pos;
    }
}
