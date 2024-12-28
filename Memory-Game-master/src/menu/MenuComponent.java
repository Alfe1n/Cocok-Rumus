package menu;

import game.BoardComponent;
import game.LevelDialog;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import scores.HighScoresDialog;

public class MenuComponent extends JComponent {

    private JPanel mainPanel;
    private Image image;
    private final String BACKGROUND_IMAGE = "../images/Background.jpg";
    private LevelDialog levelDialog;
    private HighScoresDialog scoresDialog;

    public MenuComponent() {
        setLayout(new BorderLayout());

        image = new ImageIcon(getClass().getResource(BACKGROUND_IMAGE)).getImage();

        mainPanel = new JPanel(new GridLayout(5, 1, 20, 40));
        mainPanel.setBorder(new EmptyBorder(40, 180, 80, 180));

        addTitle();
        addStartButton();
        addRulesButton();
        addDictionaryButton();
        addAboutButton();

        add(mainPanel, BorderLayout.CENTER);

        mainPanel.setOpaque(false);
    }

    public void addTitle() {
        MenuLabel gameName = new MenuLabel("Cocok Rumus", new Font("Arial", Font.BOLD, 102), Color.WHITE);
        mainPanel.add(gameName);
    }

    public void addStartButton() {
        MenuButton newGame = new MenuButton("Mulai", new Color(138, 200, 114), 44, null);

        newGame.addActionListener(event
                -> {
            if (levelDialog == null) {
                levelDialog = new LevelDialog();
            }

            levelDialog.setLocationRelativeTo(null);
            levelDialog.setSelected(false);
            levelDialog.setVisible(true);

            if (levelDialog.isSelected()) {
                remove(mainPanel);
                add(new BoardComponent(levelDialog.getLevel()));
                revalidate();
                repaint();
            }
        });

        mainPanel.add(newGame);
    }

    public void addRulesButton() {
        MenuButton rulesButton = new MenuButton("Aturan Bermain", new Color(138, 200, 214), 44, null);

        rulesButton.addActionListener(event -> {

        });

        mainPanel.add(rulesButton);
    }

    public void addDictionaryButton() {
        MenuButton dictionaryButton = new MenuButton("Kamus", new Color(200, 165, 114), 44, null);

        dictionaryButton.addActionListener(event -> {

        });

        mainPanel.add(dictionaryButton);
    }

    public void addAboutButton() {
        MenuButton aboutButton = new MenuButton("About Us", new Color(200, 114, 114), 44, null);

        aboutButton.addActionListener(event -> {

        });

        mainPanel.add(aboutButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image == null) {
            return;
        }
        g.drawImage(image, 0, 0, null);
    }
}
