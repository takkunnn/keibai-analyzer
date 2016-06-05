package realestate.keibai_analyzer.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.SNAKE_LOWER_CASE)
public class KakoData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public Integer basePrice;

    public Integer actualPrice;
    
    public String municipalityId;

    @Override
    public String toString() {
        return "KakoData [id=" + id + ", basePrice=" + basePrice + ", actualPrice=" + actualPrice + "]";
    }

}
