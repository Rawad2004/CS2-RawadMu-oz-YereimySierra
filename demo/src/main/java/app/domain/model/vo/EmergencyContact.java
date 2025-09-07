package app.domain.model.vo;


import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class EmergencyContact implements Serializable {
    private  String fullName;
    private  String relationship;
    private  PhoneNumber phoneNumber;
    protected EmergencyContact(){}


    public EmergencyContact(String fullName, String relationship, PhoneNumber phoneNumber){
        if (fullName == null || fullName.trim().isEmpty() || relationship == null || relationship.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre y la relación del contacto de emergencia son obligatorios.");
        }

        this.fullName=fullName;
        this.relationship=relationship;
        this.phoneNumber=phoneNumber;
    }

    public String getFullName() {return fullName;}
    public String getRelationship() {return relationship;}
    public PhoneNumber getPhoneNumber() {return phoneNumber;}
}
