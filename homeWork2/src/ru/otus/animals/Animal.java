package ru.otus.animals;

public class Animal {
    private long id;
    private String name;
    private int age;
    private int weight;
    private Color color;

    public Animal(String name, int age, int weight, Color color) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void say() {
        System.out.println("Я говорю");
    }

    public void go() {
        System.out.println("Я иду");
    }

    public void drink() {
        System.out.println("Я пью");
    }

    public void eat() {
        System.out.println("Я ем");
    }

    private String getAgeString() {
        if (age % 100 >= 11 && age % 100 <= 14) {
            return "лет";
        }
        return switch (age % 10) {
            case 1 -> "год";
            case 2, 3, 4 -> "года";
            default -> "лет";
        };
    }

    @Override
    public String toString() {
        return String.format("Привет! Меня зовут %s, мне %d %s, я вешу - %d кг, мой цвет - %s.",
                name, age, getAgeString(), weight, color.getTitle());
    }
}
