package io.github.mimoguz.tqmutator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

public class AppFrame extends JFrame {
    private final JButton openButton = new JButton("Open save file");
    private final JButton saveButton = new JButton("Save changes");
    private final JPanel centrePanel = new JPanel();
    private final JTextArea logArea = new JTextArea();
    private final Logger logger = new TextAreaLogger(logArea);
    private TitanQuestCharacterFile characterFile;

    public AppFrame() {
        openButton.addActionListener(e -> {
            final var fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new ChrFilter());
            final var result = fileChooser.showOpenDialog(AppFrame.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                final var file = fileChooser.getSelectedFile();
                try {
                    characterFile = TitanQuestCharacterFile.load(file, logger);
                    setupCharacterPanel();
                } catch (Exception ex) {
                    logger.warning("Couldn't load file: " + ex.getMessage());
                }
            }
        });

        saveButton.addActionListener(e -> {
            try {
                if (characterFile != null)
                    characterFile.save();
            } catch (Exception ex) {
                logger.warning("Couldn't save file: " + ex.getMessage());
            }
        });

        logArea.setEditable(false);

        setupLayout();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setupCharacterPanel() {
        centrePanel.removeAll();
        centrePanel.invalidate();
        final var characterPanel = new CharacterPanel(characterFile);
        characterPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        centrePanel.add(characterPanel, BorderLayout.NORTH);
        centrePanel.doLayout();
    }

    private void setupLayout() {
        logArea.setMinimumSize(new Dimension(300, 200));
        logArea.setLineWrap(true);

        centrePanel.setLayout(new BorderLayout());

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        buttonPanel.add(openButton);
        buttonPanel.add(Box.createHorizontalStrut(8));
        buttonPanel.add(saveButton);

        final var splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centrePanel, logArea);
        splitPanel.setResizeWeight(1.0);
        splitPanel.setDividerLocation(0.7);
        splitPanel.setBorder(BorderFactory.createEmptyBorder());

        final var content = getContentPane();
        content.add(buttonPanel, BorderLayout.NORTH);
        content.add(splitPanel, BorderLayout.CENTER);
        pack();
    }

    private static class TextAreaLogger extends Logger {

        private final JTextArea output;

        public TextAreaLogger(JTextArea output) {
            super("Label logger", null);
            this.output = output;
        }

        @Override
        public void info(String msg) {
            output.setText(output.getText() + "\n" + "INFO: " + msg);
        }

        @Override
        public void warning(String msg) {
            output.setText(output.getText() + "\n" + "WARNING: " + msg);
        }
    }

    private static class ChrFilter extends FileFilter {

        private static final String EXTENSION = "chr";
        private static final String DESCRIPTION = "Titan Quest save file (*." + EXTENSION + ")";

        @Override
        public boolean accept(File f) {
            return f.isDirectory() || getExtension(f).equals(EXTENSION);
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }

        private String getExtension(File f) {
            final var name = f.getName();
            final var dot = name.lastIndexOf('.');
            if (dot > 0 && dot < name.length() - 1) {
                return name.substring(dot + 1).toLowerCase();
            }
            return "";
        }

    }
}
