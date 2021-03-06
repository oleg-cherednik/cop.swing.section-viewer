package cop.swing.demo;

import cop.swing.controls.combo.ColorPicker;
import cop.swing.controls.layouts.SingleColumnLayout;
import cop.swing.controls.layouts.SingleRowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
public class SectionViewerDemo extends JFrame implements ActionListener {
    private final LocalSectionViewer sectionViewer = new LocalSectionViewer();
    private final SettingsPanel settingsPanel = new SettingsPanel(sectionViewer, this);

    public SectionViewerDemo() {
        init();
    }

    private void init() {
        sectionViewer.setLayoutOrganizer(SettingsPanel.SINGLE_COLUMN);
        setLayout(new BorderLayout(5, 5));

        add(settingsPanel, BorderLayout.EAST);
        add(sectionViewer, BorderLayout.CENTER);

        sectionViewer.setBorder(BorderFactory.createEtchedBorder());
        sectionViewer.setBackground(Color.pink);

        setSize(800, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    // ========== ActionListener ==========

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    // ========== static ==========

    public static void main(String... args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(() -> new SectionViewerDemo().setVisible(true));
    }

    // ========== classes ==========

    static class SettingsPanel extends JPanel implements ActionListener, ChangeListener {
        private static final long serialVersionUID = -7738468553704362158L;
        private static final SingleColumnLayout SINGLE_COLUMN = new SingleColumnLayout();
        private static final SingleRowLayout SINGLE_ROW = new SingleRowLayout();

        private int id = 1;

        private final LocalSectionViewer sectionViewer;
        private final JButton addSection = new JButton("Add section");
        private final JButton addGlue = new JButton("Add glue");
        private final JButton addTextField0 = new JButton("Add text field (col=0)");
        private final JButton addTextField1 = new JButton("Add text field (col=10)");
        private final JButton removeLast = new JButton("Remove last");
        private final JButton changeBackground = new JButton("Change background");
        private final JButton columnStrategy = new JButton("Column");
        private final JButton rowStrategy = new JButton("Row");
        private final JSpinner spaceSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        private final ColorPicker selectionColorPicker = new ColorPicker();
        private final JComboBox<AlignmentEnum> alignmentCombo = new JComboBox<>();
        private final JComboBox<ThemeEnum> themeCombo = new JComboBox<>();

        private final Random rand = new Random();
        private final JFrame frame;

        public SettingsPanel(LocalSectionViewer sectionViewer, JFrame frame) {
            this.sectionViewer = sectionViewer;
            this.frame = frame;

            init();
            addListeners();
        }

        private void init() {
            setLayout(new GridBagLayout());

            for (ColorEnum color : ColorEnum.values())
                selectionColorPicker.addItem(color.color);
            for (AlignmentEnum alignment : AlignmentEnum.values())
                alignmentCombo.addItem(alignment);
            for (ThemeEnum theme : ThemeEnum.values())
                themeCombo.addItem(theme);

            selectionColorPicker.setSelectedItem(Color.RED);
            alignmentCombo.setSelectedItem(AlignmentEnum.NORTH);

            GridBagConstraints gbc = createConstraints();

            add(addSection, gbc);
            add(addGlue, gbc);
            add(addTextField0, gbc);
            add(addTextField1, gbc);
            add(removeLast, gbc);
            add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
            add(changeBackground, gbc);
            add(new JSeparator(SwingConstants.HORIZONTAL), gbc);
            add(columnStrategy, gbc);
            add(rowStrategy, gbc);
            add(new JSeparator(SwingConstants.HORIZONTAL), gbc);

            gbc.gridwidth = 1;
            add(new JLabel("space: "), gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            add(spaceSpinner, gbc);
            gbc.gridwidth = 1;
            add(new JLabel("selection color: "), gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            add(selectionColorPicker, gbc);
            gbc.gridwidth = 1;
            add(new JLabel("alignment: "), gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            add(alignmentCombo, gbc);
            gbc.gridwidth = 1;
            add(new JLabel("theme: "), gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            add(themeCombo, gbc);

            gbc.insets.top = 2;
            add(new JSeparator(SwingConstants.HORIZONTAL), gbc);

            gbc.weighty = 1;
            add(Box.createVerticalGlue(), gbc);

            onAlignmentCombo();
            onSelectionColorPicker();

            addGlue.setEnabled(false);
            addTextField0.setEnabled(false);
            addTextField1.setEnabled(false);
        }

        private void addListeners() {
            addSection.addActionListener(this);
            addGlue.addActionListener(this);
            addTextField0.addActionListener(this);
            addTextField1.addActionListener(this);
            removeLast.addActionListener(this);
            changeBackground.addActionListener(this);
            columnStrategy.addActionListener(this);
            rowStrategy.addActionListener(this);
            spaceSpinner.addChangeListener(this);
            selectionColorPicker.addActionListener(this);
            alignmentCombo.addActionListener(this);
            themeCombo.addActionListener(this);
        }

        // ========== ActionListener ==========

        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == addSection) {
                LocalSection section = new LocalSection(id++);
                sectionViewer.addSection(section);
            } else if (event.getSource() == addGlue) {
//                panel.addComp(panel.getLayoutOrganizer() == SINGLE_COLUMN ? Box.createVerticalGlue() : Box.createHorizontalGlue());
            } else if (event.getSource() == addTextField0) {
                for (Component component : sectionViewer.getSections())
                    System.out.println(String.format("%s: [%d;%d] w:%d h:%d", component, component.getX(), component.getY(), component.getWidth(),
                            component.getHeight()));
                System.out.println();
//                panel.addComp(new JTextField("This is a text field"));
            } else if (event.getSource() == addTextField1) {
//                JTextField textField = new JTextField(10);
//                textField.setText("Text field with 10 columns");
//                panel.addComp(textField);
            } else if (event.getSource() == removeLast) {
                int total = sectionViewer.getSectionsAmount();

                if (total > 0) {
                    LocalSection section = sectionViewer.getSections().get(total - 1);
                    sectionViewer.removeSection(section);
                }
            } else if (event.getSource() == changeBackground)
                sectionViewer.setBackground(new Color(rand.nextInt(0xFFFFFF)));
            else if (event.getSource() == columnStrategy)
                sectionViewer.setLayoutOrganizer(SINGLE_COLUMN);
            else if (event.getSource() == rowStrategy)
                sectionViewer.setLayoutOrganizer(SINGLE_ROW);
            else if (event.getSource() == alignmentCombo)
                onAlignmentCombo();
            else if (event.getSource() == selectionColorPicker)
                onSelectionColorPicker();
            else if (event.getSource() == themeCombo)
                onThemeCombo();
        }

        private void onAlignmentCombo() {
            AlignmentEnum alignment = (AlignmentEnum)alignmentCombo.getSelectedItem();
            SINGLE_COLUMN.setAlignment(alignment.value);
            SINGLE_ROW.setAlignment(alignment.value);
            sectionViewer.updateUI();
        }

        private void onSelectionColorPicker() {
            sectionViewer.setSelectionColor(selectionColorPicker.getSelectedItem());
        }

        private void onThemeCombo() {
            ((ThemeEnum)themeCombo.getSelectedItem()).apply(frame);
        }

        // ========== ChangeListener ==========

        @Override
        public void stateChanged(ChangeEvent event) {
            if (event.getSource() == spaceSpinner) {
                int space = (Integer)spaceSpinner.getValue();
                SINGLE_COLUMN.setSpace(space);
                SINGLE_ROW.setSpace(space);
                sectionViewer.updateUI();
            }
        }

        // ========== static ==========

        private static GridBagConstraints createConstraints() {
            GridBagConstraints gbc = new GridBagConstraints();

            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;

            return gbc;
        }

        // ========== enum ==========

        private enum AlignmentEnum {
            CENTER("Center", SwingConstants.CENTER),
            NORTH("North", SwingConstants.NORTH),
            SOUTH("South", SwingConstants.SOUTH),
            WEST("West", SwingConstants.WEST),
            EAST("East", SwingConstants.EAST),
            LEADING("Leading", SwingConstants.LEADING),
            TRAILING("Trailing", SwingConstants.TRAILING);

            private final String title;
            private final int value;

            AlignmentEnum(String title, int value) {
                this.title = title;
                this.value = value;
            }

            // ========== Object ==========

            @Override
            public String toString() {
                return title;
            }
        }

        private enum ColorEnum {
            WHITE("white", Color.WHITE),
            LIGHT_GRAY("light gray", Color.LIGHT_GRAY),
            GRAY("gray", Color.GRAY),
            DARK_GRAY("dark gray", Color.DARK_GRAY),
            BLACK("black", Color.BLACK),
            RED("red", Color.RED),
            PINK("pink", Color.PINK),
            ORANGE("pink", Color.ORANGE),
            YELLOW("yellow", Color.YELLOW),
            GREEN("green", Color.GREEN),
            MAGENTA("magenta", Color.MAGENTA),
            CYAN("cyan", Color.CYAN),
            BLUE("blue", Color.BLUE);

            private final String title;
            private final Color color;

            ColorEnum(String title, Color color) {
                this.title = title;
                this.color = color;
            }

            // ========== Object ==========

            @Override
            public String toString() {
                return title;
            }
        }

        private enum ThemeEnum {
            SYSTEM("System") {
                @Override
                protected void setLookAndFeel() throws Exception {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
            },
            CROSS_PLATFORM("Cross-Platform") {
                @Override
                protected void setLookAndFeel() throws Exception {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                }
            },
            METAL("Metal") {
                @Override
                protected void setLookAndFeel() throws Exception {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                }
            },
            NIMBUS("Nimbus") {
                @Override
                protected void setLookAndFeel() throws Exception {
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                }
            },
            MOTIF("Motif") {
                @Override
                protected void setLookAndFeel() throws Exception {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                }
            },
            WINDOWS("Windows") {
                @Override
                protected void setLookAndFeel() throws Exception {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                }
            },
            WINDOWS_CLASSIC("Windows Classic") {
                @Override
                protected void setLookAndFeel() throws Exception {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                }
            };

            private final String title;

            ThemeEnum(String title) {
                this.title = title;
            }

            protected abstract void setLookAndFeel() throws Exception;

            public void apply(JFrame frame) {
                try {
                    setLookAndFeel();
                    SwingUtilities.updateComponentTreeUI(frame);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            // ========== Object ==========

            @Override
            public String toString() {
                return title;
            }
        }
    }
}
