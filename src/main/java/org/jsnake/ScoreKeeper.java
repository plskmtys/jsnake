package org.jsnake;

import java.util.ArrayList;
import java.util.List;

public class ScoreKeeper {
    private int currentScore;
    private List<Integer> scores;

    public ScoreKeeper() {
        currentScore = 0;
        scores = new ArrayList<>();
    }

    public void resetScore() {
        currentScore = 0;
    }

    public void increaseScore() {
        currentScore++;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void saveScore() {
        scores.add(currentScore);
    }

    public List<Integer> getScores() {
        return scores;
    }
}
