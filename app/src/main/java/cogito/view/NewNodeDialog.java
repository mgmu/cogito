package cogito.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Dimension;

public class NewNodeDialog extends JDialog {

    // Indicates if whether or not to add the node.
    private boolean confirmAdd;

    // The text field of the title.
    private JTextField titleTextField;

    public NewNodeDialog(JFrame appFrame) {
        super(appFrame, true); // true makes dialog modal
        this.confirmAdd = false;
        this.titleTextField = new JTextField(20); // 20 rows != 20 chars

        JPanel panel = new JPanel();
        JLabel titleLabel = new JLabel("Title");
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(ae -> {
                    this.confirmAdd = true;
                    this.dispose();
                });
        panel.add(titleLabel);
        panel.add(this.titleTextField);
        panel.add(confirmButton);

        this.getContentPane().add(panel);
        
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        this.pack();
        this.setVisible(true);
    }

    /**
     * Returns true if the add operation is confirmed.
     *
     * @return true if the add operation is confirmed.
     */
    public boolean confirmAddOperation() {
        return this.confirmAdd;
    }

    /**
     * Returns the title input by the user.
     *
     * A call to this method should only be made if confirmAddOperation returns
     * true.
     *
     * @return The input title, if any.
     */
    public String getTitleInput() {
        return this.titleTextField.getText();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 150);
    }
}
