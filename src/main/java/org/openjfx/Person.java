package org.openjfx;

public class Person {
    
    private final String name;
    private final String surname;
    private final Gender gender;
    private final Integer age;

    Person(String name, String surname, Gender gender, Integer age) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Gender getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    // Step 2 override toString method
    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + ", surname='" + surname + '\'' + ", gender=" + gender + ", age=" + age + '}';
    }

    enum Gender {
        MALE, FEMALE
    }
    
}
