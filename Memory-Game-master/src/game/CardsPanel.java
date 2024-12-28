package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CardsPanel extends JPanel {

    private List<Object> cards;

    public CardsPanel(Level level) {
        int rows, cols;

        switch (level) {
            case Easy:
                rows = 4;
                cols = 4;
                break;
            case Medium:
                rows = 6;
                cols = 6;
                break;
            default:
                throw new IllegalArgumentException("Invalid level");
        }

        Card.setBackImage(rows, cols);
        Card.loadCardImages();

        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(100, 150, 20, 10));

        cards = new ArrayList<>();
        createCards(rows * cols);
        addCardsToBoard(rows, cols);
    }

    private void createCards(int totalCards) {
        int pairs = totalCards / 2;
        List<String> availablePairs = new ArrayList<>(GameEngine.getMatchingPairs().keySet());
        Collections.shuffle(availablePairs);

        for (int i = 0; i < pairs; i++) {
            String cardName1 = availablePairs.get(i);
            String cardName2 = GameEngine.getMatchingPairs().get(cardName1);

            cards.add(new Card(cardName1, Card.getCardImage(cardName1)));
            cards.add(new Card(cardName2, Card.getCardImage(cardName2)));
        }

        Collections.shuffle(cards);
    }

    private void addCardsToBoard(int rows, int cols) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int index = i * cols + j;
                if (index < cards.size()) {
                    Object obj = cards.get(index);
                    if (obj instanceof Card card) {
                        card.setPreferredSize(new Dimension(Card.getBackImage().getIconWidth(),
                                Card.getBackImage().getIconHeight()));

                        gbc.gridx = j;
                        gbc.gridy = i;
                        add(card, gbc);
                    }
                }
            }
        }
    }

    public void replaceCardWithPlaceholder(Card card) {
        SwingUtilities.invokeLater(() -> {
            int index = cards.indexOf(card);
            if (index != -1) {
                JLabel placeholder = new JLabel();
                placeholder.setPreferredSize(card.getPreferredSize());
                placeholder.setOpaque(false);
                placeholder.setEnabled(false);

                GridBagConstraints gbc = ((GridBagLayout) getLayout()).getConstraints(card);
                remove(card);
                cards.set(index, placeholder);
                add(placeholder, gbc);

                revalidate();
                repaint();
            }
        });
    }

    public List<Card> getActiveCards() {
        List<Card> activeCards = new ArrayList<>();
        for (Object obj : cards) {
            if (obj instanceof Card card) {
                activeCards.add(card);
            }
        }
        return activeCards;
    }
}