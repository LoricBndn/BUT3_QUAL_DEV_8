package com.iut.banque.modele;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reset_tokens")
public class ResetToken {

    @Id
    @Column(name = "token", length = 255)
    private String token;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "expiration_time", nullable = false)
    private long expirationTime;

    public ResetToken() {
    }

    public ResetToken(String token, String userId, long expirationTime) {
        this.token = token;
        this.userId = userId;
        this.expirationTime = expirationTime;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
