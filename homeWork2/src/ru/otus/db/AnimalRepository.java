package ru.otus.db;

import ru.otus.animals.Animal;
import ru.otus.animals.Cat;
import ru.otus.animals.Color;
import ru.otus.animals.Dog;
import ru.otus.animals.birds.Duck;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AnimalRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/animals_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void init() throws SQLException {
        try (Connection connection = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE DATABASE IF NOT EXISTS animals_db DEFAULT CHARACTER SET utf8mb4");
        }
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS animals (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        type VARCHAR(20) NOT NULL,
                        name VARCHAR(100) NOT NULL,
                        age INT NOT NULL,
                        weight INT NOT NULL,
                        color VARCHAR(20) NOT NULL
                    ) DEFAULT CHARACTER SET utf8mb4""");
        }
    }

    public void add(Animal animal) throws SQLException {
        String sql = "INSERT INTO animals (type, name, age, weight, color) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, animal.getClass().getSimpleName().toUpperCase());
            ps.setString(2, animal.getName());
            ps.setInt(3, animal.getAge());
            ps.setInt(4, animal.getWeight());
            ps.setString(5, animal.getColor().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    animal.setId(keys.getLong(1));
                }
            }
        }
    }

    public List<Animal> findAll() throws SQLException {
        return query("SELECT * FROM animals ORDER BY id", null);
    }

    public List<Animal> findByType(String type) throws SQLException {
        return query("SELECT * FROM animals WHERE type = ? ORDER BY id", type);
    }

    public Animal findById(long id) throws SQLException {
        List<Animal> result = query("SELECT * FROM animals WHERE id = ?", null, id);
        return result.isEmpty() ? null : result.get(0);
    }

    public void update(Animal animal) throws SQLException {
        String sql = "UPDATE animals SET name = ?, age = ?, weight = ?, color = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, animal.getName());
            ps.setInt(2, animal.getAge());
            ps.setInt(3, animal.getWeight());
            ps.setString(4, animal.getColor().name());
            ps.setLong(5, animal.getId());
            ps.executeUpdate();
        }
    }

    private List<Animal> query(String sql, String type, Object... extraParams) throws SQLException {
        List<Animal> animals = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            if (type != null) {
                ps.setString(1, type);
            } else {
                for (int i = 0; i < extraParams.length; i++) {
                    ps.setObject(i + 1, extraParams[i]);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    animals.add(mapRow(rs));
                }
            }
        }
        return animals;
    }

    private Animal mapRow(ResultSet rs) throws SQLException {
        Animal animal = createByType(rs.getString("type"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getInt("weight"),
                Color.valueOf(rs.getString("color")));
        animal.setId(rs.getLong("id"));
        return animal;
    }

    private Animal createByType(String type, String name, int age, int weight, Color color) {
        return switch (type) {
            case "CAT" -> new Cat(name, age, weight, color);
            case "DOG" -> new Dog(name, age, weight, color);
            case "DUCK" -> new Duck(name, age, weight, color);
            default -> throw new IllegalArgumentException("Неизвестный тип животного в БД: " + type);
        };
    }
}
