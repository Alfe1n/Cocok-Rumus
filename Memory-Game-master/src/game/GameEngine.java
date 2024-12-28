package game;

import java.util.*;
import javax.swing.*;

public class GameEngine extends Thread {

    private final CardsPanel cardsPanel;
    private final GameTimer gameTimer;
    private final UserPanel userPanel;
    private static HashMap<String, String> matchingPairs;
    private int matchedPairs;
    private boolean gameRunning;
    private static boolean playing;
    private static int attempts;

    static {
        setupMatchingPairs();
    }

    public GameEngine(CardsPanel cardsPanel, GameTimer gameTimer, UserPanel userPanel) {
        this.cardsPanel = cardsPanel;
        this.gameTimer = gameTimer;
        this.userPanel = userPanel;
        this.matchedPairs = 0;
        this.gameRunning = true;
        playing = true;
        attempts = 0;
    }

    private static void setupMatchingPairs() {
        matchingPairs = new HashMap<>();
        List<Integer> indices = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);

        for (int i = 0; i < indices.size(); i++) {
            matchingPairs.put("A(" + indices.get(i) + ")", "B(" + indices.get(i) + ")");
        }
    }

    @Override
    public void run() {
        while (gameRunning) {
            while (!cardsPanel.getActiveCards().isEmpty()) {
                List<Card> currentCards;

                synchronized (cardsPanel) {
                    currentCards = cardsPanel.getActiveCards();
                }

                int[] clickedIndices = getClickedCards(currentCards);
                if (clickedIndices.length == 2) {
                    disableOtherCardsExcept(currentCards, clickedIndices);
                    processMatching(currentCards, clickedIndices);
                    enableAllCards(currentCards);
                }
            }

            if (cardsPanel.getActiveCards().isEmpty()) {
                gameRunning = false;
                stopTimer();
                SwingUtilities.invokeLater(this::showWinDialog);
                return;
            }
        }
    }

    private void stopTimer() {
        gameTimer.stopTimer(); // Panggil metode stopTimer() untuk menghentikan secara permanen
        userPanel.setTimerStopped(); // Ubah label menjadi Timer Stopped
    }

    private int[] getClickedCards(List<Card> cards) {
        int first = -1;
        int second = -1;
        int clicked = 0;

        for (int i = 0; i < cards.size() && clicked < 2; i++) {
            if (cards.get(i).isClicked() && cards.get(i).isAllowClick()) {
                if (first == -1) {
                    first = i;
                } else {
                    second = i;
                }

                clicked++;
            }
        }

        return clicked == 2 ? new int[]{first, second} : new int[0];
    }

    private void processMatching(List<Card> cards, int[] indices) {
        int first = indices[0], second = indices[1];

        synchronized (cardsPanel) {
            cards.get(first).setOpened(true);
            cards.get(second).setOpened(true);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isMatch(cards, first, second)) {
                Card firstCard = cards.get(first);
                Card secondCard = cards.get(second);

                SwingUtilities.invokeLater(() -> {
                    cardsPanel.replaceCardWithPlaceholder(firstCard);
                    cardsPanel.replaceCardWithPlaceholder(secondCard);
                });

                matchedPairs++;

                if (matchedPairs == matchingPairs.size()) {
                    stopTimer();
                    gameRunning = false;
                }
            } else {
                resetCards(cards, first, second);
            }

            cards.get(first).setClicked(false);
            cards.get(second).setClicked(false);
        }
    }

    private boolean isMatch(List<Card> cards, int first, int second) {
        if (first >= cards.size() || second >= cards.size()) {
            return false;
        }

        String card1Name = cards.get(first).getCardName();
        String card2Name = cards.get(second).getCardName();

        return (matchingPairs.containsKey(card1Name) && matchingPairs.get(card1Name).equals(card2Name))
                || (matchingPairs.containsKey(card2Name) && matchingPairs.get(card2Name).equals(card1Name));
    }

    private void resetCards(List<Card> cards, int first, int second) {
        cards.get(first).setOpened(false);
        cards.get(second).setOpened(false);
    }

    private void disableOtherCardsExcept(List<Card> cards, int[] clickedIndices) {
        for (int i = 0; i < cards.size(); i++) {
            if (i != clickedIndices[0] && i != clickedIndices[1]) {
                cards.get(i).setAllowClick(false);
                cards.get(i).setOpened(false);
            }
        }
    }

    private void enableAllCards(List<Card> cards) {
        for (Card card : cards) {
            if (!card.isClicked()) {
                card.setAllowClick(true);
            }
        }
    }

    private void showWinDialog() {
        stopTimer(); // Pastikan timer dihentikan secara permanen
        int time = gameTimer.getTime();
        Level level = BoardComponent.getLevel();
        int score = calculateScore(time, attempts, level);
        WinDialog winDialog = new WinDialog(score, attempts, level);
        winDialog.setLocationRelativeTo(null);
        winDialog.setVisible(true);
    }

    private int calculateScore(int time, int attempts, Level level) {
        switch (level) {
            case Easy:
                return Math.round((16.0f / (time + attempts)) * 100);
            case Medium:
                return Math.round((36.0f / (time / 10f + attempts / 10f)) * 200);
            default:
                return 0;
        }
    }

    public static HashMap<String, String> getMatchingPairs() {
        return matchingPairs;
    }

    public static void setPlaying(boolean playingStatus) {
        playing = playingStatus;
    }

    public static boolean isPlaying() {
        return playing;
    }

    public static int getAttempts() {
        return attempts;
    }
}
