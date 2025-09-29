package app.domain.model;

import app.domain.model.vo.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "national_id", unique = true))
    })
    private NationalId nationalId;

    private String fullName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "birth_date"))
    })
    private DateOfBirth dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "address"))
    })
    private Address address;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "phone_number"))
    })
    private PhoneNumber phoneNumber;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "email"))
    })
    private Email email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "fullName", column = @Column(name = "emergency_contact_full_name")),
            @AttributeOverride(name = "relationship", column = @Column(name = "emergency_contact_relationship")),
            @AttributeOverride(name = "phoneNumber.value", column = @Column(name = "emergency_contact_phone_number"))
    })
    private EmergencyContact emergencyContact;

    @Embedded
    private InsurancePolicy insurancePolicy;


    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VitalSignsEntry> vitalSignsEntries = new ArrayList<>();


    protected Patient() {

    }

    public Patient(NationalId nationalId, String fullName,
                   DateOfBirth dateOfBirth, Gender gender,
                   Address address, PhoneNumber phoneNumber,
                   Email email, EmergencyContact emergencyContact,
                   InsurancePolicy insurancePolicy) {
        this.nationalId = nationalId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.emergencyContact = emergencyContact;
        this.insurancePolicy = insurancePolicy;
    }


    public void updateContactInfo(Address newAddress, PhoneNumber newPhoneNumber, Email newEmail) {
        this.address = newAddress;
        this.phoneNumber = newPhoneNumber;
        this.email = newEmail;
    }

    public void updateInsurancePolicy(InsurancePolicy newPolicy) {
        this.insurancePolicy = newPolicy;
    }

    public void addVitalSigns(VitalSignsEntry entry) {
        entry.setPatient(this);
        vitalSignsEntries.add(entry);
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public NationalId getNationalId() { return nationalId; }
    public String getFullName() { return fullName; }
    public DateOfBirth getBirthDate() { return dateOfBirth; }
    public Gender getGender() { return gender; }
    public Address getAddress() { return address; }
    public PhoneNumber getPhoneNumber() { return phoneNumber; }
    public Email getEmail() { return email; }
    public EmergencyContact getEmergencyContact() { return emergencyContact; }
    public InsurancePolicy getInsurancePolicy() { return insurancePolicy; }

    public List<VitalSignsEntry> getVitalSignsEntries() {
        return vitalSignsEntries;
    }
}
