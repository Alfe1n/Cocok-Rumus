package game;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BoardComponent extends JComponent {

    private CardsPanel cardsPanel;
    private UserPanel userPanel;
    private JPanel sidePanel;
    private static Level level;
    private static JLabel highScoreLabel;
    private JTable highScoreTable;
    private DefaultTableModel tableModel;

    public BoardComponent(Level level) {
        setLayout(new BorderLayout());
        BoardComponent.level = level;

        // Panel kartu
        cardsPanel = new CardsPanel(level);
        userPanel = new UserPanel(cardsPanel.getActiveCards());
        add(cardsPanel, BorderLayout.CENTER);
        cardsPanel.setOpaque(false);

        // Panel kiri (side panel)
        sidePanel = createSidePanel();
        add(sidePanel, BorderLayout.WEST);

        // Buat GameTimer dan GameEngine
        GameTimer gameTimer = new GameTimer(userPanel);
        gameTimer.start(); // Jalankan timer di awal
        GameEngine gameEngine = new GameEngine(cardsPanel, gameTimer, userPanel);
        gameEngine.start(); // Jalankan game engine

        ninjaMode();
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        JLabel levelLabel = new JLabel(level.toString()); // Level dinamis
        levelLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel timerLabel = userPanel.getTimeLabel(); // Timer
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        highScoreLabel = new JLabel("High Score");
        highScoreLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        highScoreLabel.setForeground(Color.WHITE);
        highScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create high score table
        String[] columnNames = {"High Score"};
        tableModel = new DefaultTableModel(columnNames, 0);
        highScoreTable = new JTable(tableModel);
        highScoreTable.setFillsViewportHeight(true);
        highScoreTable.setRowHeight(20);
        highScoreTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(highScoreTable);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(200, 100));

        JButton playPauseButton = userPanel.getPlayPauseButton();
        JButton exitButton = new JButton(new ImageIcon(getClass().getResource("../images/Exit.png")));
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(new ExitListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        playPauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(playPauseButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(exitButton);

        panel.add(Box.createVerticalGlue());
        panel.add(levelLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(timerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(highScoreLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(100, 20)));
        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    public void ninjaMode() {
        Action ninjaAction = new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent event) {
                for (Card c : cardsPanel.getActiveCards()) {
                    c.setNinjaMode();
                }
            }
        };

        cardsPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("shift N"), "ninja");
        cardsPanel.getActionMap().put("ninja", ninjaAction);
    }

    public static JLabel getHighScoreLabel() {
        return highScoreLabel;
    }

    public void addHighScore(int score) {
        tableModel.addRow(new Object[]{score});
    }

    public static Level getLevel() {
        return level;
    }
}
