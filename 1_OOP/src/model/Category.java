package model;

import java.util.UUID;

public class Category{
    private String name;
    private UUID id;

    public Category(){
        name = "";
        id = UUID.randomUUID();
    }

    public Category(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}