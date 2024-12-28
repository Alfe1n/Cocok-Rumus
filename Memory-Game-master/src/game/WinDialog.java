package game;

import java.awt.*;
import javax.swing.*;
import scores.PointsCalculator;
import scores.ScoreTableModel;

public class WinDialog extends JDialog {

    private Image background;
    private static final String BACKGROUND_PATH = "../images/Win.jpg";
    private JTextField nickField;
    private JLabel scoreLabel;
    private JLabel attemptsLabel;
    private PointsCalculator pointsCalculator;

    public WinDialog(int score, int attempts, Level level) {
        setTitle("Game Completed!");
        setResizable(false);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addPointsCalculator();
        background = new ImageIcon(getClass().getResource(BACKGROUND_PATH)).getImage();

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                if (background == null) {
                    return;
                }
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        nickField = new JTextField("Player", 20);
        nickField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        nickField.setMaximumSize(new Dimension(300, 50));
        infoPanel.add(nickField);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(scoreLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        attemptsLabel = new JLabel("Attempts: " + attempts);
        attemptsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        attemptsLabel.setForeground(Color.WHITE);
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(attemptsLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        saveButton.setBackground(new Color(138, 200, 114));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(event -> {
            saveScore(score, attempts, level);
            stopTimer();
            dispose();
        });
        infoPanel.add(saveButton);

        mainPanel.add(infoPanel, BorderLayout.CENTER);
        add(mainPanel);
        pack();
    }

    private void addPointsCalculator() {
        pointsCalculator = (int time, int attempts, Level level) -> {
            switch (level) {
                case Easy:
                    return Math.round((16.0f / (time + attempts)) * 100);
                case Medium:
                    return Math.round((36.0f / (time / 10f + attempts / 10f)) * 200);
                default:
                    return 0;
            }
        };
    }

    private void saveScore(int score, int attempts, Level level) {
        String playerName = nickField.getText().trim();
        if (playerName.isEmpty()) {
            playerName = "Unknown Player";
        }

        int time = UserPanel.getTimer().getTime();
        String formattedTime = formatTime(time);

        ScoreTableModel.getInstanceOf().addScore(
                playerName, level, formattedTime, attempts, score);

        JOptionPane.showMessageDialog(this, "Score saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void stopTimer() {
        UserPanel.getTimer().setTimeRunning(false);
        UserPanel.getTimer().interrupt(); // Pastikan timer benar-benar berhenti
    }

    private String formatTime(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}
