package cogito;

import javax.swing.*;
import cogito.view.MainPanel;

/**
 * Entry class of the program.
 */
public class Main {

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Cogito");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(new MainPanel());

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Entry point of the program.
     *
     * @param args The arguments given to the program.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }
}
