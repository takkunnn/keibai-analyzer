package realestate.keibai_analyzer;

import java.math.BigDecimal;

import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.SNAKE_LOWER_CASE)
public class KakoDataCityAverage {
    
    public String municipalityId;

    public BigDecimal average;
    
    @Override
    public String toString() {
        return "KakoDataCityAverage [municipalityId=" + municipalityId + ", average=" + average.toPlainString() + "]";
    }

}
