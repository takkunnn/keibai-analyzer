package realestate.keibai_analyzer;

import java.util.Map;

import org.junit.Test;

public class PastKeibaiDataCollectorTest {

    @Test
    public void fetchPastDataPerCity() {
        Map<String, String> firstCity = CitiesConfig.list().stream().findFirst().get();
        PastKeibaiDataCollector.fetchPastDataPerCity(firstCity).forEach(singleData -> {
            System.out.println(singleData.get("base") + "  " + singleData.get("actual"));
        });;
    }
}
