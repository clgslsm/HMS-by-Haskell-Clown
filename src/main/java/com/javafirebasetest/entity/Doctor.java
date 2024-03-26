package com.javafirebasetest.entity;
public class Doctor extends Staff {
    private DeptType department;
    private int patientCount;
    protected static Doctor instanceDoctor;
    private Doctor() {super();}
    private Doctor(String username, String password, String id, String name, DeptType department) {
        super(username, password, User.Mode.DOCTOR, id, name);
        this.department = department;
    }
    public static Doctor getInstanceDoctor() {
        if (instanceDoctor == null) instanceDoctor = new Doctor();
        return instanceDoctor;
    }
    public static Doctor getInstanceDoctor(String username, String password, String id, String name, DeptType department) {
        if (instanceDoctor == null) instanceDoctor = new Doctor(username, password, id, name, department);
        return instanceDoctor;
    }
    @Override
    public String toString() {
        return "doctor [ID=" + getID() + ", name=" + getName() +
                ", department=" + department.getValue() + ", patientCount=" + patientCount + "]";
    }
}
