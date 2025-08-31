package app.domain.model.vo;

import java.time.LocalDate;

public class InsurancePolicy {
    private final String companyName;
    private final String policyNumber;
    private final boolean active;
    private final LocalDate expiryDate;

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
