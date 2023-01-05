package org.andrew.models;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 1, max = 40, message = "Invalid name size (1-40 symbols)")
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "year_of_birth")
    private int yearOfBirth;

    @OneToMany(mappedBy = "bookOwner")
    private List<Book> personBooks;

    public Person() {
    }

    public Person(int id, String fullName, int dateOfBirth) {
        this.fullName = fullName;
        this.yearOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int dateOfBirth) {
        this.yearOfBirth = dateOfBirth;
    }

    public List<Book> getPersonBooks() {
        return personBooks;
    }

    public void setPersonBooks(List<Book> personBooks) {
        this.personBooks = personBooks;
    }
}
