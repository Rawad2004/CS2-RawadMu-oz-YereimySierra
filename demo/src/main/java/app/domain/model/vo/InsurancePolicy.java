package app.domain.model.vo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;


@Embeddable
public class InsurancePolicy implements Serializable {
    private String companyName;
    private String policyNumber;
    private boolean active;
    private LocalDate expiryDate;

    protected InsurancePolicy(){}

    public InsurancePolicy(String companyName,String policyNumber,boolean active,LocalDate expiryDate){
        this.companyName=companyName;
        this.policyNumber=policyNumber;
        this.active=active;
        this.expiryDate=expiryDate;
    }

    public String getCompanyName() {return companyName;}
    public String getPolicyNumber() {return policyNumber;}
    public boolean isActive() {return active;}
    public LocalDate getExpiryDate() {return expiryDate;}
}
