package io.github.mimoguz.tqmutator;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final var app = new AppFrame();
            app.setVisible(true);
        });
    }

}
