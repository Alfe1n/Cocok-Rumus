package game;

import java.awt.*;
import java.util.HashMap;
import javax.swing.*;

public class Card extends JButton {

    private String cardName; // Nama kartu
    private ImageIcon cardImageIcon; // Gambar depan kartu
    private boolean clicked; // Status apakah kartu telah diklik
    private boolean ninjaMode; // Status mode ninja (kartu selalu terlihat)
    private boolean allowClick; // Status apakah kartu dapat diklik

    private static HashMap<String, ImageIcon> cardImages = new HashMap<>();
    private static ImageIcon backImage;
    private static int sharkWidth;
    private static int sharkHeight;

    public Card(String cardName, ImageIcon cardImageIcon) {
        this.cardName = cardName;
        this.cardImageIcon = cardImageIcon;
        this.clicked = false;
        this.ninjaMode = false;
        this.allowClick = true;

        // Set tampilan awal kartu
        setPrimaryCard();
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Pastikan kartu tidak memiliki transparansi
        setOpaque(true);
        setContentAreaFilled(false);
        setBorderPainted(false);

        // Tambahkan listener untuk mengatur perilaku ketika kartu diklik
        addActionListener((e) -> {
            if (allowClick && !ninjaMode && !clicked) {
                setOpened(true); // Buka kartu
                clicked = true;
            }
        });
    }

    /**
     * Menampilkan gambar belakang kartu (default).
     */
    public void setPrimaryCard() {
        setIcon(getBackImage()); // Tampilkan gambar belakang kartu
        clicked = false; // Reset status klik
    }

    /**
     * Mengatur apakah kartu terbuka atau tertutup.
     *
     * @param opened true jika kartu terbuka (gambar depan), false jika kartu tertutup (gambar belakang).
     */
    public void setOpened(boolean opened) {
        if (opened) {
            setIcon(cardImageIcon); // Tampilkan gambar depan kartu
        } else {
            setPrimaryCard(); // Tampilkan gambar belakang kartu
        }
    }

    /**
     * Mengaktifkan atau menonaktifkan klik pada kartu.
     *
     * @param allow true jika kartu dapat diklik, false jika tidak.
     */
    public void setAllowClick(boolean allow) {
        this.allowClick = allow;
    }

    public boolean isAllowClick() {
        return allowClick;
    }

    /**
     * Mengaktifkan atau menonaktifkan mode ninja. Dalam mode ninja, kartu
     * selalu terlihat.
     */
    public void setNinjaMode() {
        ninjaMode = !ninjaMode;
        setOpened(ninjaMode); // Sesuaikan tampilan berdasarkan mode ninja
    }

    /**
     * Mendapatkan status apakah kartu telah diklik.
     *
     * @return true jika kartu telah diklik, false jika belum.
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * Mengatur status klik kartu.
     *
     * @param clicked true jika kartu dianggap telah diklik, false jika tidak.
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    /**
     * Mendapatkan nama kartu.
     *
     * @return nama kartu.
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * Mendapatkan gambar depan kartu.
     *
     * @return objek ImageIcon gambar depan kartu.
     */
    public ImageIcon getCardImageIcon() {
        return cardImageIcon;
    }

    /**
     * Mengatur gambar belakang kartu berdasarkan jumlah baris dan kolom.
     */
    public static void setBackImage(int rows, int cols) {
        ImageIcon backIcon = loadImage("../images/Brain" + (rows * cols) + ".png");
        if (backIcon != null) {
            sharkWidth = backIcon.getIconWidth();
            sharkHeight = backIcon.getIconHeight();
            backImage = backIcon;
        }
    }

    /**
     * Memuat gambar kartu ke dalam peta `cardImages`.
     */
    public static void loadCardImages() {
        if (sharkWidth == 0 || sharkHeight == 0) {
            return;
        }

        String[] cardNames = {
                "A(1)", "A(2)", "A(3)", "A(4)", "A(5)", "A(6)", "A(7)", "A(8)", "A(9)", "A(10)",
                "A(11)", "A(12)", "A(13)", "A(14)", "A(15)", "A(16)", "A(17)", "A(18)", "A(19)", "A(20)",
                "B(1)", "B(2)", "B(3)", "B(4)", "B(5)", "B(6)", "B(7)", "B(8)", "B(9)", "B(10)",
                "B(11)", "B(12)", "B(13)", "B(14)", "B(15)", "B(16)", "B(17)", "B(18)", "B(19)", "B(20)"
        };

        for (String cardName : cardNames) {
            ImageIcon imageIcon = loadImage("../images/" + cardName + ".png");
            if (imageIcon != null) {
                Image scaledImage = imageIcon.getImage()
                        .getScaledInstance(sharkWidth, sharkHeight, Image.SCALE_SMOOTH);
                cardImages.put(cardName, new ImageIcon(scaledImage));
            }
        }
    }

    /**
     * Mendapatkan gambar depan kartu berdasarkan nama kartu.
     *
     * @param cardName nama kartu.
     * @return objek ImageIcon untuk gambar depan kartu.
     */
    public static ImageIcon getCardImage(String cardName) {
        return cardImages.get(cardName);
    }

    /**
     * Mendapatkan gambar belakang kartu.
     *
     * @return objek ImageIcon untuk gambar belakang kartu.
     */
    public static ImageIcon getBackImage() {
        return backImage;
    }

    /**
     * Memuat gambar dari jalur yang ditentukan.
     *
     * @param path jalur gambar.
     * @return objek ImageIcon untuk gambar, atau null jika gagal.
     */
    private static ImageIcon loadImage(String path) {
        java.net.URL imgURL = Card.class.getResource(path);
        return imgURL != null ? new ImageIcon(imgURL) : null;
    }
}
