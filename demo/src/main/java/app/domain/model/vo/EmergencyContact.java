package app.domain.model.vo;

public class EmergencyContact {
    private final String fullName;
    private final String relationship;
    private final PhoneNumber phoneNumber;

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
