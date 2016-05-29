package realestate.keibai_analyzer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PastKeibaiDataCollector {
    
    private static final String CONDITION_PARTS = "menuKey=&mojiSize=1&screenId=SCPT003&screenURI=SCPT001%2CSCPT003&"
                                                      + "maxage=2147483647&prefecturesId=11&hdnPeriodId=3&hdnFiscalYearId=2016&"
                                                      + "hdnEraId=04&hdnBuildYear=13&hdnObjPeriodId=1&kind=2&ymSelect=0&periodId=3&"
                                                      + "saleAmountLowerId=0&saleAmountUpperId=1000&landClsId=00&buildingCoverageLower=&"
                                                      + "buildingCoverageUpper=&floorAreaRatioLower=&floorAreaRatioUpper=&landAreaLower=&"
                                                      + "landAreaUpper=&floorAreaLower=&floorAreaUpper=&exclusiveAreaLower=&exclusiveAreaUpper=&"
                                                      + "structure1Id=01&structure2Id=00&roomArrangementId=99&storyLower=&storyUpper=&"
                                                      + "buildYearSelect=1&eraId=04&buildYear=13&objPeriodId=1&groundUndergroundId=0&"
                                                      + "floorLower=&floorUpper=&adminExpensesLower=&adminExpensesUpper=&balconyAreaLower=&"
                                                      + "balconyAreaUpper=&totalNoLower=&totalNoUpper=";
    
    public static void collect(){
        CitiesConfig.list().stream().forEach(cityConfig -> {
            fetchPastDataPerCity(cityConfig).stream().forEach(singleData -> {
                // TODO:DB登録
            });
        });
    }
    
    static List<Map<String, Object>> fetchPastDataPerCity(Map<String, String> city) {
        Connection con = Jsoup.connect("http://bit.sikkou.jp/app/past/pt003/h20")
                .header("Host", "bit.sikkou.jp")
                .header("Connection", "keep-alive")
                .header("Content-Length", "768")
                .header("Cache-Control", "max-age=0")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Origin", "http://bit.sikkou.jp")
                .header("Upgrade-Insecure-Requests", "1")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Referer", "http://bit.sikkou.jp/app/past/pl003/h05")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "ja,en-US;q=0.8,en;q=0.6")
                .header("Cookie", "HEAD_COUNTER=2078; HEAD_TIMESTAMP=20160522063004; MOJI_SIZE=1");
        
        String body = new StringBuilder()
                .append(CONDITION_PARTS)
                .append("&").append("courtId=").append(city.get("courtId"))
                .append("&").append("municipalityId=").append(city.get("municipalityId")).toString();
        con.requestBody(body);
        
        Document doc;
        try {
            doc = con.post();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        List<Map<String, Object>> perCity = new ArrayList<Map<String, Object>>();
        
        doc.select("tr:has(.blk_largetxt)")
           .forEach(tr -> {
               Map<String, Object> singleData = new HashMap<String, Object>();
               singleData.put("base", new BigDecimal(tr.select(".blk_largetxt").text().replaceAll(",", "")));
               singleData.put("actual", new BigDecimal(tr.select(".org_largetxt").text().replaceAll(",", "")));
               
               perCity.add(singleData);
            });
        
        return perCity;
    }
}

