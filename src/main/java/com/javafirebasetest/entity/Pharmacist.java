package com.javafirebasetest.entity;
public class Pharmacist extends Staff {
    private Pharmacist() {super();}
    private Pharmacist(String id, String name) {super(User.Mode.PHARMACIST, id, name);}
    @Override
    public String toString() {
        return "pharmacist [ID=" + getID() + ", name=" + getName() + "]";
    }
}
