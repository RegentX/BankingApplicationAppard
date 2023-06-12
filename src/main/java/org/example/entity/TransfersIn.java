package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "transfers_in", schema = "financecontrol", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransfersIn {

    @Column(name = "account_number", nullable = true, length = -1)
    private String accountNumber;

    @Column(name = "amount", nullable = true)
    private Long amount;

    @Column(name = "date", nullable = true)
    private OffsetDateTime date;

    @Column(name = "bank_name_s", nullable = true, length = -1)
    private String bankNameS;

    @Column(name = "bank_name", nullable = true, length = -1)
    private String bankName;

    @Column(name = "sender_account", nullable = true, length = -1)
    private String senderAccount;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "transfer_in_id", nullable = false)
    private Long transferInId;


}
