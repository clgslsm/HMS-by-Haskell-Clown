package com.javafirebasetest.entity;

public class Receptionist extends Staff {
    protected static Receptionist instanceReceptionist;
    private Receptionist() {super();}
    private Receptionist(String username, String password, String id, String name) {
        super(username, password, User.Mode.RECEPTIONIST, id, name);
    }
    public static Receptionist getInstanceReceptionist() {
        if (instanceReceptionist == null) instanceReceptionist = new Receptionist();
        return instanceReceptionist;
    }
    public static Receptionist getUserReceptionist(String username, String password, String id, String name) {
        if (instanceReceptionist == null) instanceReceptionist = new Receptionist(username, password, id, name);
        return instanceReceptionist;
    }
    @Override
    public String toString() {
        return "receptionist [ID=" + getID() + ", name=" + getName() + "]";
    }
}
