package model;

import java.util.UUID;

public class User {
    private UUID id;
    private String name;

    public User( String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public User() {
        this.id = UUID.randomUUID();
        this.name = "";
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
