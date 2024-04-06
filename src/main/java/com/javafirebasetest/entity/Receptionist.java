package com.javafirebasetest.entity;
public class Receptionist extends Staff {
    private Receptionist() {super();}
    private Receptionist(String id, String name) {super(User.Mode.RECEPTIONIST, id, name);}
    @Override
    public String toString() {
        return "receptionist [ID=" + getID() + ", name=" + getName() + "]";
    }
}
