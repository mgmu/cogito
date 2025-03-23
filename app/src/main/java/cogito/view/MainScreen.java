package cogito.view;

import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
    private JList<Object> graphNames;

    // New graph button.
    private JButton newButton;

    // Open graph button.
    private JButton openButton;

    // Name and identifier of saved graphs.
    private List<GraphInfo> graphInfos;

    /**
     * Creates a new main screen with the given frame manager.
     *
     * @param frameManager The frame manager of the application.
     */
    public MainScreen(FrameManager frameManager) {
        super(frameManager);

        this.setLayout(new BorderLayout());

        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel labelPane = new JPanel();
        labelPane.setLayout(new BoxLayout(labelPane, BoxLayout.LINE_AXIS));
        labelPane.add(new JLabel("Your graphs:"));
        labelPane.add(Box.createHorizontalGlue());
        listPane.add(labelPane);

        try {
            this.graphInfos = DataManager.getSavedGraphInfos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
              frameManager.getAppFrame(),
              "Could not retrieve saved graphs.",
              "Error",
              JOptionPane.ERROR_MESSAGE
            );
        }

        this.graphNames = new JList<Object>(this.graphInfos.toArray());
        this.graphNames.setSelectionMode(
          ListSelectionModel.SINGLE_SELECTION
        );
        this.graphNames.setLayoutOrientation(JList.VERTICAL);
        this.graphNames.setVisibleRowCount(-1);
        this.graphNames.addListSelectionListener(this);
        JScrollPane scrollPane = new JScrollPane(this.graphNames);
        listPane.add(scrollPane);

        // Creates a new project
        this.newButton = createNamedButton(
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

        this.openButton = createNamedButton(
          "Open",
          KeyEvent.VK_O,
          al -> {
              if (this.graphInfos == null)
                  return;
              GraphInfo selectedGraphInfo =
                  (GraphInfo)this.graphNames.getSelectedValue();
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
        this.openButton.setEnabled(false);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(this.openButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(this.newButton);

        this.add(listPane, BorderLayout.CENTER);
        this.add(buttonPane, BorderLayout.PAGE_END);
    }

    // Returns a named button, sets its mnemonic and action listener
    private static JButton createNamedButton(String name, int mnemonic,
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
        if (this.graphNames == null)
            return;
        if (e.getValueIsAdjusting() == false) {
            if (this.graphNames.getSelectedIndex() == -1)
                openButton.setEnabled(false);
            else
                openButton.setEnabled(true);
        }
    }
}
