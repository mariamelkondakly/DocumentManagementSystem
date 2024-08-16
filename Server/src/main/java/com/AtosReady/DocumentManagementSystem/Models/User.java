package com.AtosReady.DocumentManagementSystem.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="Users")
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

    @Column(name="personal_info")
    private String personal_info;

    public User(String email, long nid, String password, String personalInfo) {
        this.email=email;
        this.nid=nid;
        this.password=password;
        this.personal_info=personalInfo;
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
                ", personal_info='" + personal_info + '\'' +
                '}';
    }
}
