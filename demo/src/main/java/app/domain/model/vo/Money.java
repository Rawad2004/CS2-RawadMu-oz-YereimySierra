package app.domain.model.vo;
import java.math.BigDecimal;

public class Money {

    private final BigDecimal value;

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
