package ru.otus;

import ru.otus.animals.Animal;
import ru.otus.animals.Color;
import ru.otus.db.AnimalRepository;
import ru.otus.factory.AnimalFactory;
import ru.otus.factory.AnimalType;

import java.util.List;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AnimalRepository repository = new AnimalRepository();

    public static void main(String[] args) {
        try {
            AnimalRepository.init();
        } catch (Exception e) {
            System.out.println("Не удалось подключиться к MySQL: " + e.getMessage());
            return;
        }
        System.out.println("Подключение к БД успешно. Доступные команды: add / list / edit / filter / exit");

        Command currentCommand;
        do {
            currentCommand = askForCommand();
            switch (currentCommand) {
                case ADD -> addAnimal();
                case LIST -> listAnimals(findAll());
                case EDIT -> editAnimal();
                case FILTER -> filterAnimals();
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
                    String.join("/", "add", "list", "edit", "filter", "exit"));
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
        try {
            repository.add(animal);
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении в БД: " + e.getMessage());
            return;
        }
        animal.say();
        System.out.println("Животное сохранено в БД с id = " + animal.getId());
    }

    private static List<Animal> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            System.out.println("Ошибка при чтении из БД: " + e.getMessage());
            return List.of();
        }
    }

    private static void listAnimals(List<Animal> animals) {
        if (animals.isEmpty()) {
            System.out.println("Список пуст");
            return;
        }
        for (Animal animal : animals) {
            System.out.printf("[%d] %s%n", animal.getId(), animal);
        }
    }

    private static void editAnimal() {
        List<Animal> animals = findAll();
        if (animals.isEmpty()) {
            System.out.println("Список пуст, редактировать нечего");
            return;
        }
        listAnimals(animals);
        System.out.print("Введите id животного для редактирования: ");
        long id;
        try {
            id = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("id должен быть целым числом.");
            return;
        }
        Animal animal;
        try {
            animal = repository.findById(id);
        } catch (Exception e) {
            System.out.println("Ошибка при чтении из БД: " + e.getMessage());
            return;
        }
        if (animal == null) {
            System.out.println("Животное с id = " + id + " не найдено.");
            return;
        }

        System.out.print("Что изменить (name/age/weight/color): ");
        String field = scanner.nextLine().trim().toLowerCase();
        switch (field) {
            case "name" -> {
                System.out.print("Введите новое имя: ");
                animal.setName(scanner.nextLine().trim());
            }
            case "age" -> animal.setAge(askForInt("Введите новый возраст: "));
            case "weight" -> animal.setWeight(askForInt("Введите новый вес (кг): "));
            case "color" -> animal.setColor(askForColor());
            default -> {
                System.out.println("Неизвестное поле: " + field);
                return;
            }
        }

        try {
            repository.update(animal);
            System.out.println("Обновлено: [" + animal.getId() + "] " + animal);
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении в БД: " + e.getMessage());
        }
    }

    private static void filterAnimals() {
        AnimalType type = askForAnimalType();
        try {
            listAnimals(repository.findByType(type.name()));
        } catch (Exception e) {
            System.out.println("Ошибка при чтении из БД: " + e.getMessage());
        }
    }
}
