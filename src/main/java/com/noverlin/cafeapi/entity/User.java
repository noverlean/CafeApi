package com.example.account_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Account> accounts;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    public String toString()
    {
        return "=== USER ================\n\t" +
                "id: " + id + ", \n\t" +
                "fullname: " + fullname + ", \n\t" +
                "nickname: " + nickname + ", \n\t" +
                "password: " + password + ", \n\t" +
                "roles: " + roles.stream().map(Role::toString);
    }
}
