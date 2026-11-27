package ru.otus;

import ru.otus.animals.Animal;
import ru.otus.animals.Color;
import ru.otus.factory.AnimalFactory;
import ru.otus.factory.AnimalType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Animal> animals = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Доступные команды: add / list / exit");

        Command currentCommand;
        do {
            currentCommand = askForCommand();
            switch (currentCommand) {
                case ADD -> addAnimal();
                case LIST -> listAnimals();
                case EXIT -> System.out.println("Выход из программы. Пока!");
            }
        } while (currentCommand != Command.EXIT);
    }

    private static Command askForCommand() {
        String input = null;
        do {
            if (input != null) {
                System.out.println("Введена неверная команда, попробуйте еще раз");
            }
            System.out.printf("Введите одну из команд : (%s): ",
                    String.join("/", "add", "list", "exit"));
            input = scanner.nextLine();
        } while (Command.doesNotContain(input));
        return Command.fromString(input);
    }

    private static AnimalType askForAnimalType() {
        String input = null;
        do {
            if (input != null) {
                System.out.println("Введено неверное животное, попробуйте еще раз");
            }
            System.out.printf("Введите одного из животных : (%s): ",
                    String.join("/", "cat", "dog", "duck"));
            input = scanner.nextLine();
        } while (AnimalType.fromString(input) == null);
        return AnimalType.fromString(input);
    }

    private static Color askForColor() {
        String input = null;
        do {
            if (input != null) {
                System.out.println("Введен неверный цвет, попробуйте еще раз");
            }
            System.out.printf("Введите цвет : (%s): ", Color.titles());
            input = scanner.nextLine();
        } while (Color.fromString(input) == null);
        return Color.fromString(input);
    }

    private static int askForInt(String message) {
        while (true) {
            System.out.print(message);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value > 0) {
                    return value;
                }
                System.out.println("Число должно быть больше нуля.");
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    private static void addAnimal() {
        AnimalType type = askForAnimalType();
        System.out.print("Введите имя: ");
        String name = scanner.nextLine().trim();
        int age = askForInt("Введите возраст: ");
        int weight = askForInt("Введите вес (кг): ");
        Color color = askForColor();

        Animal animal = AnimalFactory.create(type, name, age, weight, color);
        animals.add(animal);
        animal.say();
    }

    private static void listAnimals() {
        if (animals.isEmpty()) {
            System.out.println("Список пуст");
            return;
        }
        for (Animal animal : animals) {
            System.out.println(animal);
        }
    }
}
