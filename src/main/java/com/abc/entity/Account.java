package com.abc.entity;

import lombok.Data;
import org.hibernate.annotations.Check;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
@Check(constraints = "balance >= 0")
public class Account {

    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal balance;

}
