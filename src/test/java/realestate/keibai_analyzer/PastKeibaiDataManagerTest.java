package realestate.keibai_analyzer;

import java.util.Map;

import org.junit.Test;

public class PastKeibaiDataManagerTest {

    @Test
    public void fetchPastDataPerCity() {
        Map<String, String> firstCity = CitiesConfig.list().stream().findFirst().get();
        PastKeibaiDataManager.fetchPastDataPerCity(firstCity).forEach(singleData -> {
            System.out.println(singleData.basePrice + "  " + singleData.actualPrice);
        });
    }
    
    @Test
    public void testCollect() {
        PastKeibaiDataManager.collect();
    }
    
    @Test
    public void testShowCityAverageData() {
        PastKeibaiDataManager.showCityAverageData();
    }
}
