package com.javafirebasetest.entity;
public class Technician extends Staff {
    protected static Technician instanceTechnician;
    private Technician() {super();}
    private Technician(String username, String password, String id, String name) {
        super(username, password, User.Mode.TECHNICIAN, id, name);
    }
    public static Technician getInstanceTechnician() {
        if (instanceTechnician == null) instanceTechnician = new Technician();
        return instanceTechnician;
    }
    public static Technician getInstanceTechnician(String username, String password, String id, String name) {
        if (instanceTechnician == null) instanceTechnician = new Technician(username, password, id, name);
        return instanceTechnician;
    }
    @Override
    public String toString() {
        return "technician [ID=" + getID() + ", name=" + getName() + "]";
    }
}