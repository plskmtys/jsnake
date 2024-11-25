package org.jsnake;

public class FruitGenerator {
    public static Fruit generateFruit() {
        double rand = Math.random();
        if(rand < Fruit.STRAWBERRY.getProbability()){
            return Fruit.STRAWBERRY;
        } else if(rand < Fruit.BANANA.getProbability()){
            return Fruit.BANANA;
        } else if(rand < Fruit.PEAR.getProbability()){
            return Fruit.PEAR;
        } else {
            return Fruit.APPLE;
        }
    }
}
