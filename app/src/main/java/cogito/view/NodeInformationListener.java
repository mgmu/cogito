package cogito.view;

import java.util.Objects;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import cogito.model.Node;

class NodeInformationListener implements DocumentListener {

    private JFrame appFrame;
    private Node model;

    NodeInformationListener(JFrame appFrame, Node model) {
        this.appFrame = Objects.requireNonNull(appFrame);
        this.model = Objects.requireNonNull(model);
    }

    public void insertUpdate(DocumentEvent de) {
        this.updateModel(de.getDocument());
    }

    public void removeUpdate(DocumentEvent de) {
        this.updateModel(de.getDocument());
    }

    public void changedUpdate(DocumentEvent de) {
        // does nothing
    }

    private void updateModel(Document doc) {
        String newInfo = this.model.getInformation();
        try {
            newInfo = doc.getText(0, doc.getLength());
        } catch(BadLocationException ble) {
            JOptionPane.showMessageDialog(
              this.appFrame,
              ble.getMessage(),
              "Node information could not be saved",
              JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String currInfo = this.model.getInformation();
        if (!currInfo.equals(newInfo)) {
            try {
                this.model.setInformation(newInfo);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                  this.appFrame,
                  e.getMessage(),
                  "Node information could not be saved",
                  JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
