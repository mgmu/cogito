package cogito.view;

import java.util.Objects;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import cogito.model.Node;

class NodeTitleListener implements DocumentListener {

    private JFrame appFrame;
    private Node model;

    NodeTitleListener(JFrame appFrame, Node model) {
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
        String newTitle = this.model.getTitle();
        try {
            newTitle = doc.getText(0, doc.getLength());
        } catch(BadLocationException ble) {
            JOptionPane.showMessageDialog(
              this.appFrame,
              ble.getMessage(),
              "Node title could not be saved",
              JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String currTitle = this.model.getTitle();
        if (!currTitle.equals(newTitle)) {
            try {
                this.model.setTitle(newTitle);
                this.model.updateObservers();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                  this.appFrame,
                  e.getMessage(),
                  "Node Title could not be saved",
                  JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
