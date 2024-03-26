package com.javafirebasetest.entity;
import java.util.ArrayList;
public enum DeptType {
    MEDICINE("Medicine"), SURGERY("Surgery"), GYNAECOLOGY("Gynecology"), OBSTETRICS("Obstetrics"),
    PAEDIATRICS("Paediatrics"), EYE("Eye"), ENT("ENT"), DENTAL("Dental"), ORTHOPAEDICS("Orthopaedics"),
    NEUROLOGY("Neurology"), CARDIOLOGY("Cardiology"), PSYCHIATRY("Psychiatry"), SKIN("Skin"),
    V_D("V.D."), PLASTIC_SURGERY("Plast surgery"), NUCLEAR_MEDICINE("Nuclear medicine"),
    INFECTIOUS_DISEASE("Infectious disease");
    private final String value;
    DeptType(String value) {this.value = value;}
    public ArrayList<String> search(String key) { //Find list value in enum by keys
        ArrayList<String> Listvalue = new ArrayList<String>();
        for (DeptType dt : DeptType.values()) {
            int length = dt.getValue().length();
            for (int i = 0; i < length; ++i) {
                String res = dt.getValue().substring(i);
                if (res.equals(key)) {
                    Listvalue.add(dt.getValue());
                    break;
                }
            }
        }
        return Listvalue;
    }
    public String getValue() {return value;}
    public static DeptType fromValue(String value) {
        for (DeptType dt : DeptType.values())
            if (dt.name().equalsIgnoreCase(value)) return dt;
        throw new IllegalArgumentException("Invalid department type: " + value);
    }
}
