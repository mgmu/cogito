package cogito.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import cogito.model.Graph;
import cogito.model.GraphInfo;
import cogito.model.Node;
import cogito.util.DataManager;

/**
 * The first screen of the GUI.
 *
 * This Screen prompts the user to choose between creating a new graph or
 * loading an existing one.
 */
public class MainScreen extends Screen implements ListSelectionListener {

    // Preferred width of the panel
    private static final int PREFERRED_WIDTH = 400;

    // Preferred height of the panel
    private static final int PREFERRED_HEIGHT = 200;
    
    // List of graph names.
    private JList<Object> jNames;

    // New graph button.
    private JButton newButton;

    // Open graph button.
    private JButton openButton;

    // List of graph informations.
    private List<GraphInfo> graphInfos;

    /**
     * Creates a new main screen with the given frame manager.
     *
     * @param frameManager The frame manager of the application.
     */
    public MainScreen(FrameManager frameManager) {
        super(frameManager);

        // Creates a new project
        newButton = createNamedButton(
          "New",
          KeyEvent.VK_N,
          al -> {
              JFrame appFrame = frameManager.getAppFrame();
              TextInputDialog dialog = new TextInputDialog(
                appFrame,
                "Graph name:",
                ""
              );
              if (dialog.isConfirmed()) {
                  try {
                      Graph model = new Graph(dialog.getInput());
                      this.frameManager.setCurrentScreen(
                        new GraphEditor(this.frameManager, model)
                      );
                      DataManager.saveGraph(model);
                  } catch (IllegalArgumentException iae) {
                      JOptionPane.showMessageDialog(
                        appFrame,
                        iae.getMessage(),
                        "Graph could not be created",
                        JOptionPane.ERROR_MESSAGE
                      );
                  } catch (IOException ioe) {
                      JOptionPane.showMessageDialog(
                        appFrame,
                        ioe.getMessage(),
                        "Graph could not be saved",
                        JOptionPane.ERROR_MESSAGE
                      );
                  }
              }
          }
        );
        this.add(newButton);

        this.graphInfos = null;
        try {
            graphInfos = DataManager.getSavedGraphInfos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
              frameManager.getAppFrame(),
              "Could not retrieve saved graphs.",
              "Error",
              JOptionPane.ERROR_MESSAGE
            );
        }

        this.openButton = new JButton("Open");
        this.openButton.setEnabled(false);
        this.openButton.addActionListener(
          al -> {
              if (this.graphInfos == null)
                  return;
              GraphInfo selectedGraphInfo =
                  (GraphInfo)this.jNames.getSelectedValue();
              if (selectedGraphInfo == null) {
                  JOptionPane.showMessageDialog(
                    frameManager.getAppFrame(),
                    "No graph selected.",
                    "Could not open graph",
                    JOptionPane.ERROR_MESSAGE
                  );
              } else {
                  try {
                      Graph model = DataManager.loadGraph(
                        selectedGraphInfo.identifier()
                      );
                      this.frameManager.setCurrentScreen(
                        new GraphEditor(this.frameManager, model)
                      );
                  } catch (IOException ioe) {
                      JOptionPane.showMessageDialog(
                        frameManager.getAppFrame(),
                        "Could not open graph.",
                        "Could not open graph",
                        JOptionPane.ERROR_MESSAGE
                      );
                  }
              }
          }
        );
        this.add(openButton);

        if (graphInfos == null) {
            JLabel noGraphLabel = new JLabel("No graph saved yet.");
            this.add(noGraphLabel);
        } else {
            jNames = new JList<Object>(graphInfos.toArray());
            jNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jNames.setLayoutOrientation(JList.VERTICAL);
            jNames.setVisibleRowCount(-1);
            jNames.addListSelectionListener(this);
            JScrollPane scrollPane = new JScrollPane(jNames);
            // continue here
            
            this.add(jNames);
        }
    }

    // Returns a named button, sets its mnemonic and action listener
    private JButton createNamedButton(String name, int mnemonic,
            ActionListener al) {
        JButton button = new JButton(name);
        button.setMnemonic(mnemonic);
        button.addActionListener(al);
        return button;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (jNames == null)
            return;
        if (e.getValueIsAdjusting() == false) {
            if (jNames.getSelectedIndex() == -1)
                openButton.setEnabled(false);
            else
                openButton.setEnabled(true);
        }
    }
}
