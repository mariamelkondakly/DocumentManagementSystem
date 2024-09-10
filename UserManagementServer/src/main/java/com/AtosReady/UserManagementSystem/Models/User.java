package com.AtosReady.UserManagementSystem.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="Users")
@CrossOrigin
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name="NID",nullable = false,unique = true)
    private long nid;

    @Column(name="password", nullable = false)
    //@Size(min = 8,max = 20)
    private String password;

    @Column(name="first_name", nullable = false)
    private String first_name;

    @Column(name="last_name", nullable=false)
    private String last_name;

    @Column(name="age")
    private int age;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public User(String email, long nid, String password, String first_name, String last_name, int age) {
        this.email=email;
        this.nid=nid;
        this.password=password;
        this.first_name=first_name;
        this.last_name=last_name;
        this.age=age;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nid=" + nid +
                ", password='" + password + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", age=" + age +
                '}';
    }
}
