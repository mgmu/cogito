package cogito.util;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.CopyOption;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import cogito.model.Graph;
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
     */
    public static void saveGraph(Graph model) throws IOException {
        Charset charset = StandardCharsets.US_ASCII;
        String graphUuid = model.getUuid().toString();
        Path graphModelDir = GRAPHS_DIR.resolve(graphUuid);
        if (Files.exists(graphModelDir)) {
            String tmpDirName = System.getProperty("java.io.tmpdir");
            Path graphSaveDir = Paths.get(tmpDirName, graphUuid);
            copyFolder(graphModelDir, graphSaveDir);
            deleteFolder(graphModelDir);
            Files.createDirectory(graphModelDir);
            boolean succ = writeGraphData(graphModelDir, charset, model);
            if (!succ) {
                deleteFolder(graphModelDir);
                copyFolder(graphSaveDir, graphModelDir);
            }
            deleteFolder(graphSaveDir);
        } else {
            Files.createDirectory(graphModelDir);
            writeGraphData(graphModelDir, charset, model);
        }
    }

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
        List<Node> nodes = model.getNodes();
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
            data = node.getPositionAsString();
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
}
