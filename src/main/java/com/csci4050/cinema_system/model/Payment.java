package com.csci4050.cinema_system.model;

import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String transactionId;

    private Long amount;

    private Long userId;

    public Payment() {}

    public Payment(Long amount, Long userId) {
        this.amount = amount;
        this.userId = userId;
        this.transactionId = RandomStringUtils.randomAlphanumeric(12);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
