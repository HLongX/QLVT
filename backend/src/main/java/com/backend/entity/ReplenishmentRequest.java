package com.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "replenishment_requests")
public class ReplenishmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(length = 500)
    private String note;

    @Column(name = "requester_id")
    private Integer requesterId;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReplenishmentRequestItem> items = new ArrayList<>();

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    // ===============================
    // Helper method
    // ===============================
    public void addItem(ReplenishmentRequestItem item) {
        item.setRequest(this);
        this.items.add(item);
    }

    // ===============================
    // Getters & Setters
    // ===============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Integer getRequesterId() { return requesterId; }
    public void setRequesterId(Integer requesterId) { this.requesterId = requesterId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ReplenishmentRequestItem> getItems() { return items; }
    public void setItems(List<ReplenishmentRequestItem> items) { this.items = items; }
}
