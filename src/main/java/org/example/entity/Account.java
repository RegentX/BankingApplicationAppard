package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account", schema = "financecontrol", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @Column(name = "account_number", nullable = false, length = -1)
    private String accountNumber;

    @Column(name = "a_type", nullable = true, length = -1)
    private String aType;

    @Column(name = "amount", nullable = true)
    private Long amount;

    @Column(name = "privacy", nullable = true, length = -1)
    private String privacy;

    @Column(name = "bank_name", nullable = true, length = -1)
    private String bankName;

    @Column(name = "passport", nullable = false, length = -1)
    private String passport;

    @Column(name = "a_currency_id", nullable = true)
    private Long aCurrencyId;


}
