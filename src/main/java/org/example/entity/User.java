package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "users", schema = "financecontrol", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {


    @Id
    private String passport;

    @Column(name = "first_name", nullable = true, length = -1)
    private String firstName;

    @Column(name = "last_name", nullable = true, length = -1)
    private String lastName;

    @Column(name = "email", nullable = true, length = -1)
    private String email;

    @Column(name = "u_password", nullable = true, length = -1)
    private String uPassword;

    @Column(name = "registration_date", nullable = true)
    private OffsetDateTime registrationDate;




}
