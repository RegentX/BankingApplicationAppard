package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transfers_out", schema = "financecontrol", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransfersOut {

    @Column(name = "getter_acount", nullable = true, length = -1)
    private String getterAcount;

    @Column(name = "account_number", nullable = true, length = -1)
    private String accountNumber;

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
    @Column(name = "transfer_out_id", nullable = false)
    private Long transferOutId;


}
