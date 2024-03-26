package com.javafirebasetest.entity;
public class Pharmacist extends Staff {
    protected static Pharmacist instancePharmacist;
    private Pharmacist() {super();}
    private Pharmacist(String username, String password, String id, String name) {
        super(username, password, Mode.PHARMACIST, id, name);
    }
    public static Pharmacist getInstance() {
        if (instancePharmacist == null) instancePharmacist = new Pharmacist();
        return instancePharmacist;
    }
    public static Pharmacist getInstance(String username, String password, String id, String name) {
        if (instancePharmacist == null) instancePharmacist = new Pharmacist(username, password, id, name);
        return instancePharmacist;
    }
    @Override
    public String toString() {
        return "pharmacist [ID=" + getID() + ", name=" + getName() + "]";
    }
}
