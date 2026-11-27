package ru.otus.factory;

import ru.otus.animals.Animal;
import ru.otus.animals.Cat;
import ru.otus.animals.Color;
import ru.otus.animals.Dog;
import ru.otus.animals.birds.Duck;

public class AnimalFactory {

    private AnimalFactory() {
    }

    public static Animal create(AnimalType type, String name, int age, int weight, Color color) {
        return switch (type) {
            case CAT -> new Cat(name, age, weight, color);
            case DOG -> new Dog(name, age, weight, color);
            case DUCK -> new Duck(name, age, weight, color);
        };
    }
}
