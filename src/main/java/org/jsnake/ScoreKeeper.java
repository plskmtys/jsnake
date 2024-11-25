package org.jsnake;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreKeeper {
    private int currentScore;
    private List<Integer> scores;
    private static final String SCORES_FILE = "src/main/resources/scores.dat";

    public ScoreKeeper() {
        currentScore = 0;
        scores = new ArrayList<>();
        loadScores();
    }

    public void resetScore() {
        currentScore = 0;
    }

    public void increaseScore(int amount) {
        currentScore+=amount;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void saveScore() {
        scores.add(currentScore);
        saveScores();
    }

    public List<Integer> getScores() {
        return scores;
    }

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
