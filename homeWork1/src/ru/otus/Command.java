package ru.otus;

public enum Command {
    ADD,
    LIST,
    EXIT;

    public static Command fromString(String input) {
        try {
            return valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static boolean doesNotContain(String input) {
        return fromString(input) == null;
    }
}
