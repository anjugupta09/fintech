package com.reloadly.model.entity;

import com.reloadly.AccountType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(	name = "Account")
@Getter
@Setter
public class Account {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "account_type")
        @Enumerated(EnumType.STRING)
        private AccountType type;

        @Column(name = "available_balance")
        private BigDecimal balance = new BigDecimal(0);

        @ManyToOne
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        private User user;
}
