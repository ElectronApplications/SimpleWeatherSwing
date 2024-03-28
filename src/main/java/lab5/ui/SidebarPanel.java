package lab5.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;

public class SidebarPanel extends JPanel {
    public Color sideBackgroundColor = new Color(43, 45, 48);
    public Color sideForegroundColor = Color.WHITE;
    public Color sideHoverBackgroundColor = new Color(67, 69, 74);
    public Color sideHoverForegroundColor = Color.WHITE;
    public Color sideSelectedBackgroundColor = new Color(53, 132, 228);
    public Color sideSelectedForegroundColor = Color.WHITE;

    protected final LinkedHashMap<String, SidebarPanel.Entry> entries = new LinkedHashMap<>();
    protected String selectedEntry = null;

    protected final JPanel sidePanel = new JPanel();
    protected final JPanel contentPanel = new JPanel();

    SidebarPanel() {
        super();

        // The left side panel
        this.sidePanel.setLayout(new BoxLayout(this.sidePanel, BoxLayout.Y_AXIS));
        this.sidePanel.setMinimumSize(new Dimension(200, 400));
        this.sidePanel.setBackground(this.sideBackgroundColor);
        this.sidePanel.setBorder(null);

        // The right side content panel
        this.contentPanel.setLayout(new CardLayout());
        this.contentPanel.setMinimumSize(new Dimension(400, 400));
        this.contentPanel.setBorder(null);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.sidePanel, this.contentPanel);
        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                        g.setColor(sideBackgroundColor);
                        g.fillRect(0, 0, getSize().width, getSize().height);
                    }
                };
            }
        });
        splitPane.setDividerLocation(200);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);

        this.setLayout(new BorderLayout());
        this.add(splitPane);
    }

    public void addEntry(String id, String name, JComponent entry) {
        if (this.entries.containsKey(id))
            return;

        JButton optionButton = new JButton(name);
        optionButton.setBorder(null);

        // Add hover effects to the button
        optionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                if (!id.equals(selectedEntry)) {
                    optionButton.setBackground(sideHoverBackgroundColor);
                    optionButton.setForeground(sideHoverForegroundColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                updateButtons();
            }
        });

        // Button click event
        optionButton.addActionListener(actionEvent -> {
            this.selectedEntry = id;
            this.updateButtons();
        });

        if (entries.isEmpty()) {
            this.selectedEntry = id;
        }

        this.entries.put(id, new Entry(optionButton, entry));
        this.contentPanel.add(entry, id);

        optionButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        this.sidePanel.add(optionButton);
        this.updateButtons();
    }

    public void removeEntry(String id) {
        var entry = this.entries.remove(id);

        if (entry == null)
            return;

        if (id.equals(this.selectedEntry)) {
            if (!this.entries.isEmpty()) {
                this.selectedEntry = this.entries.firstEntry().getKey();
            }
            else
                this.selectedEntry = null;
        }

        this.contentPanel.remove(entry.entry);
        this.sidePanel.remove(entry.sideButton);

        this.updateButtons();

        this.contentPanel.revalidate();
        this.contentPanel.repaint();
        this.sidePanel.revalidate();
        this.sidePanel.repaint();
    }

    public void selectEntry(String id) {
        if (this.entries.containsKey(id)) {
            this.selectedEntry = id;
            this.updateButtons();
        }
    }

    // Update buttons' colors
    protected void updateButtons() {
        for (var entry : this.entries.entrySet()) {
            if (entry.getKey().equals(this.selectedEntry)) {
                entry.getValue().sideButton.setBackground(sideSelectedBackgroundColor);
                entry.getValue().sideButton.setForeground(sideSelectedForegroundColor);
            } else {
                entry.getValue().sideButton.setBackground(sideBackgroundColor);
                entry.getValue().sideButton.setForeground(sideForegroundColor);
            }
        }

        CardLayout cardLayout = (CardLayout) this.contentPanel.getLayout();
        cardLayout.show(this.contentPanel, this.selectedEntry);
    }

    protected record Entry(JButton sideButton, JComponent entry) {}
}
