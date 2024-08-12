package io.github.mimoguz.tqmutator;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Pass
            }
            final var app = new AppFrame();
            app.setVisible(true);
        });
    }

}
