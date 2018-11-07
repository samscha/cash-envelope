package com.example.cashenvelope.baseClass;

import java.time.LocalDateTime;

import javax.persistence.Column;

public abstract class Base {
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updated_at;

    public Base() {
        final LocalDateTime now = LocalDateTime.now();
        this.created_at = now;
        this.updated_at = now;
    }

    public void updateTimestamp() {
        this.updated_at = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return this.created_at;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updated_at;
    }
}