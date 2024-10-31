package org.jsnake;

public class AIController {
    private Snake aiSnake;
    private Fruit fruit;

    public AIController(Snake aiSnake, Fruit fruit) {
        this.aiSnake = aiSnake;
        this.fruit = fruit;
    }

    public void move() {
        // Implement AI movement logic using Dijkstra's algorithm
    }
}