package org.jsnake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ScoreTest {
    
    private ScoreKeeper scoreKeeper;

    @BeforeEach
    public void setUp() {
        scoreKeeper = new ScoreKeeper();
    }

    @Test
    public void testInitialScore() {
        assertEquals(0, scoreKeeper.getCurrentScore());
    }

    @Test
    public void testIncreaseScore() {
        scoreKeeper.increaseScore(10);
        assertEquals(10, scoreKeeper.getCurrentScore());
    }

    @Test
    public void testResetScore() {
        scoreKeeper.increaseScore(10);
        scoreKeeper.resetScore();
        assertEquals(0, scoreKeeper.getCurrentScore());
    }
}
