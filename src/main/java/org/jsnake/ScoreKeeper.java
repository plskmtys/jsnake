package org.jsnake;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A pontszámokat kezelő osztály.
 */
public class ScoreKeeper {
    /**
     * A játékos aktuális pontszáma.
     */
    private int currentScore;

    /**
     * A korábbi pontszámok listája.
     */
    private List<Integer> scores;

    /**
     * A pontszámokat tartalmazó fájl elérési útja.
     */
    private static final String SCORES_FILE = "src/main/resources/scores.dat";

    /**
     * Konstruktor, amely inicializálja a jelenlegi pontszámot 0-ra, és betölti a korábbi pontszámokat.
     */
    public ScoreKeeper() {
        currentScore = 0;
        scores = new ArrayList<>();
        loadScores();
    }

    /**
     * A jelenlegi pontszám nullázása.
     */
    public void resetScore() {
        currentScore = 0;
    }

    /**
     * A jelenlegi pontszám növelése a megadott értékkel.
     * @param amount a növelendő pontszám.
     */
    public void increaseScore(int amount) {
        currentScore+=amount;
    }

    /**
     * A jelenlegi pontszám lekérdezése.
     * @return a jelenlegi pontszám.
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * A jelenlegi pontszám mentése a korábbi pontszámok közé.
     */
    public void saveScore() {
        if(currentScore == 0) return;
        scores.add(currentScore);
        saveScores();
    }

    /**
     * A legjobb pontszám lekérdezése.
     * @return a legjobb pontszám.
     */
    public int getBestScore() {
        return scores.stream().max(Integer::compareTo).orElse(0);
    }

    /**
     * A korábbi pontszámok listájának lekérdezése.
     * @return a korábbi pontszámok listája.
     */
    public List<Integer> getScores() {
        return scores;
    }

    /**
     * A korábbi pontszámok betöltése a fájlból.
     */
    private void loadScores() {
        File file = new File(SCORES_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                @SuppressWarnings("unchecked")
                List<Integer> loadedScores = (List<Integer>) ois.readObject();
                scores.addAll(loadedScores);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A korábbi pontszámok mentése a fájlba.
     */
    private void saveScores() {
        try {
            File file = new File(SCORES_FILE);
            file.getParentFile().mkdirs();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(new ArrayList<>(scores));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
