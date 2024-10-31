package org.jsnake;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {
    private List<Integer> scores;

    public ScoreManager() {
        scores = new ArrayList<>();
        loadScores();
    }

    public void addScore(int score) {
        scores.add(score);
        saveScores();
    }

    private void loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("resources/highscores.dat"))) {
            scores = (List<Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("resources/highscores.dat"))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getScores() {
        return scores;
    }
}
