package org.jsnake;

public enum Fruit {
    APPLE(1, 1),
    PEAR(2, 0.4),
    BANANA(3, 0.2),
    STRAWBERRY(5, 0.1);

    private final int value;
    private final double probability;

    Fruit(int value, double probability) {
        this.value = value;
        this.probability = probability;
    }

    public int getValue() {
        return value;
    }

    public double getProbability() {
        return probability;
    }
}
