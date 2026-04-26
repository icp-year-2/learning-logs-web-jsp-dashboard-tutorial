package com.learninglogs.entity;

import java.sql.Timestamp;

public class Topic {

    private int id;
    private String name;
    private int userId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Topic(String name) {
        this.name = name;
    }

    public Topic(int id, String name, int userId, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getUserId() { return userId; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setUserId(int userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "[" + id + "] " + name + " (Created: " + createdAt + ")";
    }
}
