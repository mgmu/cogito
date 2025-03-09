package cogito.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;

public class ConfirmDialog extends JDialog {

    // Indicates if whether or not the user clicked on confirm.
    private boolean isConfirmed;

    public ConfirmDialog(JFrame appFrame, String label) {
        super(appFrame, true); // true makes dialog modal
        this.isConfirmed = false;

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

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 150);
    }
}
