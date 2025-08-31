package app.domain.model;

import app.domain.model.vo.*;

public class Patient {

    private long id;
    private final NationalId nationalId;
    private String fullName;
    private final BirthDate birthDate;
    private final Gender gender;
    private Address address;
    private PhoneNumber phoneNumber;
    private Email email;
    private EmergencyContact emergencyContact;
    private InsurancePolicy insurancePolicy;

    public Patient(NationalId nationalId, String fullName,
                   BirthDate birthDate, Gender gender, Address address,
                   PhoneNumber phoneNumber, Email email,
                   EmergencyContact emergencyContact, InsurancePolicy insurancePolicy) {
        this.nationalId = nationalId;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.emergencyContact = emergencyContact;
        this.insurancePolicy = insurancePolicy;
    }

    public void updateContactInfo(Address newAddress, PhoneNumber newPhoneNumber, Email newEmail) {

        this.address = newAddress;
        this.phoneNumber= newPhoneNumber;
        this.email=newEmail;
    }
    public void updateInsurancePolicy(InsurancePolicy newPolicy){
        this.insurancePolicy=newPolicy;
    }

    public long getId(){return id;}
    public NationalId getNationalId() {return nationalId;}
    public String getFullName() {return fullName;}
    public BirthDate getBirthDate() {return birthDate;}
    public Gender getGender() {return gender;}
    public Address getAddress() {return address;}
    public PhoneNumber getPhoneNumber() {return phoneNumber;}
    public Email getEmail() {return email;}
    public EmergencyContact getEmergencyContact() {return emergencyContact;}
    public InsurancePolicy getInsurancePolicy() {return insurancePolicy;}

    public void setId(long id){
        this.id=id;
    }
}
