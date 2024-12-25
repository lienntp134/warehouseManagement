package com.datn.warehousemgmt.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "email", columnDefinition = "nvarchar(255)", unique = true)
    String email;

    @Column(name = "name", columnDefinition = "nvarchar(255)")
    String name;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate birthday;

    @Column(name = "username", columnDefinition = "nvarchar(255)", unique = true)
    String username;

    @Column(name = "status")
    Boolean status;

    @Column(name = "password", columnDefinition = "nvarchar(255)")
    String password;

    @Column(name = "phoneNumber", columnDefinition = "varchar(10)")
    String phoneNumber;

    @Column(name = "sex")
    Integer gender;

    @Column(name = "ware_house_id")
    Long warehouseId;

    @ManyToMany
    @JoinTable(name = "user_permission",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions = new ArrayList<>();
}
