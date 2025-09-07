package app.domain.model.vo;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;


@Embeddable
public class Money implements Serializable {

    private  BigDecimal value;
    protected Money(){}

    public Money(BigDecimal value){
        if(value == null || value.compareTo(BigDecimal.ZERO)<0){
            throw new IllegalArgumentException("El valor monetario no puede ser nulo o negativo.");
        }
        this.value=value;
    }

    public BigDecimal getValue(){
        return value;
    }
}
