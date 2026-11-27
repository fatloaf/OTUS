package ru.otus.factory;

public enum AnimalType {
    CAT,
    DOG,
    DUCK;

    public static AnimalType fromString(String input) {
        try {
            return valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
