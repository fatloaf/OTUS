package ru.otus.animals;

public class Cat extends Animal {
    public Cat(String name, int age, int weight, Color color) {
        super(name, age, weight, color);
    }

    @Override
    public void say() {
        System.out.println("Мяу");
    }
}
