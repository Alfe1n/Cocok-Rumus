package game;

import javax.swing.*;

public class GameTimer extends Thread {
    private final UserPanel userPanel;
    private boolean isRunning;
    private boolean isStopped;
    private int time;

    public GameTimer(UserPanel userPanel) {
        this.userPanel = userPanel;
        this.isRunning = true;
        this.isStopped = false; // Default tidak dihentikan permanen
        this.time = 0;
    }

    @Override
    public void run() {
        while (!isStopped) { // Pastikan loop berhenti jika isStopped = true
            synchronized (this) {
                if (!isRunning) {
                    try {
                        wait(); // Timer berhenti sementara
                    } catch (InterruptedException e) {
                        // Periksa apakah timer harus dihentikan permanen
                        if (isStopped) {
                            break;
                        }
                    }
                }
            }

            if (isRunning) {
                try {
                    Thread.sleep(1000);
                    synchronized (this) {
                        if (isRunning) {
                            time++;
                            SwingUtilities.invokeLater(() -> userPanel.setTimeLabelText(formatTime(time)));
                        }
                    }
                } catch (InterruptedException e) {
                    // Periksa kembali status jika ada interupsi
                    if (isStopped) {
                        break;
                    }
                }
            }
        }
    }

    public synchronized void setTimeRunning(boolean running) {
        this.isRunning = running;
        if (running) {
            notify(); // Melanjutkan thread jika running diubah menjadi true
        }
    }

    public synchronized void stopTimer() {
        this.isStopped = true; // Set status berhenti permanen
        this.isRunning = false;
        interrupt(); // Interupsi thread untuk memastikan loop berhenti
    }

    public synchronized int getTime() {
        return time;
    }

    private String formatTime(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
