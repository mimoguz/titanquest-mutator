package io.github.mimoguz.tqmutator;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class CharacterPanel extends JPanel {

        private final TitanQuestCharacterFile charFile;
        private final JTextField nameInput = new JTextField();
        private final JSpinner levelInput = new JSpinner();
        private final JSpinner moneyInput = new JSpinner();
        private final JSpinner attributePtInput = new JSpinner();
        private final JSpinner skillPtInput = new JSpinner();
        private final JSpinner killsInput = new JSpinner();
        private final JSpinner deathsInput = new JSpinner();

        public CharacterPanel(TitanQuestCharacterFile characterFile) {
                this.charFile = characterFile;
                setup();
        }

        private void setup() {
                nameInput.addFocusListener(new FocusListener() {

                        @Override
                        public void focusGained(FocusEvent e) {
                                // Pass
                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                                final var text = nameInput.getText().trim();
                                if (!text.isBlank()) {
                                        nameInput.setText(text);
                                        charFile.setCharacterName(text);
                                }
                        }

                });
                nameInput.setText(charFile.getCharacterName());

                levelInput.setModel(new SpinnerNumberModel(charFile.getLevel(), 0, 999, 1));
                levelInput.addChangeListener(e -> charFile.setLevel(getNumber(levelInput)));

                moneyInput.setModel(new SpinnerNumberModel(charFile.getMoney(), 0, 999999999, 100));
                moneyInput.addChangeListener(e -> charFile.setMoney(getNumber(moneyInput)));

                attributePtInput.setModel(new SpinnerNumberModel(charFile.getAttributePoints(), 0, 999, 1));
                attributePtInput.addChangeListener(e -> charFile.setAttributePoints(getNumber(attributePtInput)));

                skillPtInput.setModel(new SpinnerNumberModel(charFile.getSkillPoints(), 0, 999, 1));
                skillPtInput.addChangeListener(e -> charFile.setSkillPoints(getNumber(skillPtInput)));

                killsInput.setModel(new SpinnerNumberModel(charFile.getNumberOfKills(), 0, Integer.MAX_VALUE, 1));
                killsInput.addChangeListener(e -> charFile.setNumberOfKills(getNumber(killsInput)));

                deathsInput.setModel(new SpinnerNumberModel(charFile.getNumberOfDeaths(), 0, Integer.MAX_VALUE, 1));
                deathsInput.addChangeListener(e -> charFile.setNumberOfDeaths(getNumber(deathsInput)));

                setLayout(new GridLayout(7, 2, 8, 12));
                add(new JLabel("Character name"));
                add(nameInput);
                add(new JLabel("Level"));
                add(levelInput);
                add(new JLabel("Money"));
                add(moneyInput);
                add(new JLabel("Attribute points"));
                add(attributePtInput);
                add(new JLabel("Skill points"));
                add(skillPtInput);
                add(new JLabel("Kills"));
                add(killsInput);
                add(new JLabel("Deaths"));
                add(deathsInput);
                setMinimumSize(new Dimension(400, 600));
        }

        private static int getNumber(JSpinner spinner) {
                return ((SpinnerNumberModel) spinner.getModel()).getNumber().intValue();
        }

}
