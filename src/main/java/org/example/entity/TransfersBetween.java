package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transfers_between", schema = "financecontrol", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransfersBetween {

    @Column(name = "getter_account", nullable = true, length = -1)
    private String getterAccount;

    @Column(name = "setter_number", nullable = true, length = -1)
    private String setterNumber;

    @Column(name = "amount", nullable = true)
    private Long amount;

    @Column(name = "date", nullable = true)
    private OffsetDateTime date;

    @Column(name = "bank_name_g", nullable = true, length = -1)
    private String bankNameG;

    @Column(name = "bank_name", nullable = true, length = -1)
    private String bankName;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "transfer_between_id", nullable = false)
    private Long transferBetweenId;


}
