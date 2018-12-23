package com.turchenkov.parsing.domains.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = "roles")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 5, max = 50, message = "Incorrect Login")
    private String login;

    @Column(nullable = false)
    private String password;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "User_Role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String login, String password) {
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
