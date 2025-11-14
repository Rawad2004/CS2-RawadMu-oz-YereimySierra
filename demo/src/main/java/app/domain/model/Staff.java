    package app.domain.model;

    import app.domain.model.enums.StaffRole;
    import app.domain.model.vo.*;
    import jakarta.persistence.*;

    @Entity
    @Table(name = "staff")
    public class Staff {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String fullName;

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "value", column = @Column(name = "national_id", unique = true))
        })
        private NationalId nationalId;

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "value", column = @Column(name = "email"))
        })
        private Email email;

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "value", column = @Column(name = "phone_number"))
        })
        private PhoneNumber phoneNumber;

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "value", column = @Column(name = "birth_date"))
        })
        private DateOfBirth dateOfBirth;

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "value", column = @Column(name = "address"))
        })
        private Address address;

        @Enumerated(EnumType.STRING)
        private StaffRole role;

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "value", column = @Column(name = "username", unique = true))
        })
        private Username username;

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "value", column = @Column(name = "password"))
        })
        private Password password;


        public Staff(String fullName, NationalId nationalId,
                     Email email, PhoneNumber phoneNumber, DateOfBirth dateOfBirth,
                     Address address, StaffRole role, Username username, Password password) {
            this.fullName = fullName;
            this.nationalId = nationalId;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.dateOfBirth = dateOfBirth;
            this.address = address;
            this.role = role;
            this.username = username;
            this.password = password;
        }

        protected Staff(){}

        public boolean isActive() {
            return true; // Por ahora siempre activo, pero puedes agregar lógica
        }

        public void updateContactInfo(Address newAddress, PhoneNumber newPhoneNumber, Email newEmail) {
            this.address = newAddress;
            this.phoneNumber = newPhoneNumber;
            this.email = newEmail;
        }

        public void changePassword(Password newPassword) {
            this.password = newPassword;
        }


        public Long getId() {return id;}
        public String getFullName() {return fullName;}
        public NationalId getNationalId() {return nationalId;}
        public Email getEmail() {return email;}
        public PhoneNumber getPhoneNumber() {return phoneNumber;}
        public DateOfBirth getBirthDate() {return dateOfBirth;}
        public Address getAddress() {return address;}
        public StaffRole getRole() {return role;}
        public Username getUsername() {return username;}
        public Password getPassword() {return password;}


        public void setId(Long id){
            this.id=id;
        }
    }
