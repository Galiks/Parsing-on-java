package com.turchenkov.parsing.domains;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "User_Table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private int password;

    public User() {
    }

    public User(String login, int password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password=" + password +
                '}';
    }
}
