package com.example.account_management.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public String toString()
    {
        return "=== ROLE ================\n\t" +
                "id: " + id + ", \n\t" +
                "name: " + name;
    }
}