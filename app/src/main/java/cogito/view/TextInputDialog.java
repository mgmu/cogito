package cogito.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Dimension;

public class TextInputDialog extends JDialog {

    // Indicates if whether or not the user clicked on confirm.
    private boolean isConfirmed;

    // The text field for the text input
    private JTextField input;

    public TextInputDialog(JFrame appFrame, String label, String initialInput) {
        super(appFrame, true); // true makes dialog modal
        this.isConfirmed = false;
        this.input = new JTextField(initialInput, 20); // 20 rows != 20 chars

        JPanel panel = new JPanel();
        JLabel jLabel = new JLabel(label);
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(
          ae -> {
              this.isConfirmed = true;
              this.dispose();
          }
        );
        panel.add(jLabel);
        panel.add(this.input);
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
    public boolean isConfirmed() {
        return this.isConfirmed;
    }

    /**
     * Returns the text input by the user.
     *
     * A call to this method should only be made if isConfirmed returns true.
     *
     * @return The text input, if any.
     */
    public String getInput() {
        return this.input.getText();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 150);
    }
}
