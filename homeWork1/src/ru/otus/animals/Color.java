package ru.otus.animals;

public enum Color {
    BLACK("чёрный"),
    WHITE("белый"),
    GREY("серый"),
    RED("рыжий"),
    YELLOW("жёлтый"),
    BROWN("коричневый");

    private final String title;

    Color(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static Color fromString(String input) {
        String normalized = input.trim();
        for (Color color : values()) {
            if (color.name().equalsIgnoreCase(normalized) || color.title.equalsIgnoreCase(normalized)) {
                return color;
            }
        }
        return null;
    }

    public static String titles() {
        StringBuilder sb = new StringBuilder();
        for (Color color : values()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(color.title);
        }
        return sb.toString();
    }
}
