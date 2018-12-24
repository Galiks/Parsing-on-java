package com.turchenkov.parsing.domains.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
//@Data
//@EqualsAndHashCode(exclude = "users")
public enum Role {
    USER,
    ADMIN;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String role;
//
//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users = new HashSet<>();
//
//    public Role(String role) {
//        this.role = role;
//    }
//
//    public Role() {
//    }
}
