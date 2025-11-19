package app.domain.model;

import app.domain.model.enums.Gender;
import app.domain.model.vo.*;
import jakarta.persistence.*;

import java.time.LocalDate;
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
            @AttributeOverride(name = "value", column = @Column(name = "national_id", unique = true, nullable = false))
    })
    private NationalId nationalId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "birth_date", nullable = false))
    })
    private DateOfBirth dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "address", nullable = false))
    })
    private Address address;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "phone_number", nullable = false))
    })
    private PhoneNumber phoneNumber;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "email", nullable = true))
    })
    private Email email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "fullName", column = @Column(name = "emergency_contact_full_name", nullable = false)),
            @AttributeOverride(name = "relationship", column = @Column(name = "emergency_contact_relationship", nullable = false)),
            @AttributeOverride(name = "phoneNumber.value", column = @Column(name = "emergency_contact_phone_number", nullable = false))
    })
    private EmergencyContact emergencyContact;

    @Embedded
    private InsurancePolicy insurancePolicy;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VitalSignsEntry> vitalSignsEntries = new ArrayList<>();

    protected Patient() {  }

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

    public void updateBasicInfo(String fullName, LocalDate birthDate, Gender gender) {
        if (fullName != null && !fullName.isBlank()) {
            this.fullName = fullName;
        }
        if (birthDate != null) {
            this.dateOfBirth = new DateOfBirth(birthDate);
        }
        if (gender != null) {
            this.gender = gender;
        }
    }

    public void updateContactInfo(Address newAddress, PhoneNumber newPhoneNumber, Email newEmail) {
        if (newAddress != null) {
            this.address = newAddress;
        }
        if (newPhoneNumber != null) {
            this.phoneNumber = newPhoneNumber;
        }
        this.email = newEmail;
    }

    public void updateEmergencyContact(EmergencyContact newEmergencyContact) {
        if (newEmergencyContact != null) {
            this.emergencyContact = newEmergencyContact;
        }
    }

    public void updateInsurancePolicy(InsurancePolicy newPolicy) {
        if (newPolicy != null) {
            this.insurancePolicy = newPolicy;
        }
    }

    public void addVitalSigns(VitalSignsEntry entry) {
        entry.setPatient(this);
        vitalSignsEntries.add(entry);
    }

    public void removeVitalSigns(VitalSignsEntry entry) {
        vitalSignsEntries.remove(entry);
        entry.setPatient(null);
    }

    public boolean hasActiveInsurance() {
        return insurancePolicy != null &&
                insurancePolicy.isActive() &&
                !insurancePolicy.getExpiryDate().isBefore(LocalDate.now());
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public NationalId getNationalId() { return nationalId; }

    public String getFullName() { return fullName; }

    public DateOfBirth getDateOfBirth() { return dateOfBirth; }

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
