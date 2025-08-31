package app.domain.model;

import app.domain.model.vo.*;

public class Staff {
    private long id;
    private String fullName;
    private final NationalId nationalId;
    private Email email;
    private PhoneNumber phoneNumber;
    private final BirthDate birthDate;
    private Address address;
    private StaffRole role;
    private final Username username;
    private Password password;

    public Staff(String fullName, NationalId nationalId,
                 Email email, PhoneNumber phoneNumber, BirthDate birthDate,
                 Address address, StaffRole role, Username username, Password password) {

        this.fullName = fullName;
        this.nationalId = nationalId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    /**
     * Actualiza la información de contacto de un miembro del personal.
     *
     * @param newAddress     la nueva dirección.
     * @param newPhoneNumber el nuevo número de teléfono.
     */
    public void updateContactInfo(Address newAddress, PhoneNumber newPhoneNumber) {
        this.address = newAddress;
        this.phoneNumber = newPhoneNumber;
    }

    /**
     * Permite cambiar la contraseña de un usuario.
     *
     * @param newPassword la nueva contraseña (ya validada por el VO)
     */
    public void changePassword(Password newPassword) {
        this.password = newPassword;
    }

    public long getId() {return id;}
    public String getFullName() {return fullName;}
    public NationalId getNationalId() {return nationalId;}
    public Email getEmail() {return email;}
    public PhoneNumber getPhoneNumber() {return phoneNumber;}
    public BirthDate getBirthDate() {return birthDate;}
    public Address getAddress() {return address;}
    public StaffRole getRole() {return role;}
    public Username getUsername() {return username;}
    public Password getPassword() {return password;}

    public void setId(Long id){
        this.id=id;
    }
}
