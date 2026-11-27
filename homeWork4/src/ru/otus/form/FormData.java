package ru.otus.form;

import java.util.List;

public class FormData {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String passwordConfirm;
    private final String nickname;
    private final String birthday;
    private final String age;
    private final String bio;
    private final String contact;
    private final List<String> interests;
    private final String experience;
    private final String color;

    public FormData(String firstName, String lastName, String email, String password,
                    String passwordConfirm, String nickname, String birthday, String age,
                    String bio, String contact, List<String> interests,
                    String experience, String color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.nickname = nickname;
        this.birthday = birthday;
        this.age = age;
        this.bio = bio;
        this.contact = contact;
        this.interests = interests;
        this.experience = experience;
        this.color = color;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public String getNickname() {
        return nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAge() {
        return age;
    }

    public String getBio() {
        return bio;
    }

    public String getContact() {
        return contact;
    }

    public List<String> getInterests() {
        return interests;
    }

    public String getExperience() {
        return experience;
    }

    public String getColor() {
        return color;
    }
}
