package realestate.keibai_analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.seasar.doma.jdbc.tx.TransactionManager;

import realestate.keibai_analyzer.entity.KakoData;
import realestate.keibai_analyzer.setting.AppConfig;

public class PastKeibaiDataManager {
    
    private static final String BODY_FRAGMENT = "menuKey=&mojiSize=1&screenId=SCPT003&screenURI=SCPT001%2CSCPT003&"
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
        
        TransactionManager tm = AppConfig.singleton().getTransactionManager();
        KakoDataDaoImpl dao = new KakoDataDaoImpl();
        
        tm.required(() -> {
            CitiesConfig.list().forEach(cityConfig -> {
                List<KakoData> cityDatas = fetchPastDataPerCity(cityConfig);
                dao.insert(cityDatas);
            });
        });
        
    }
    
    public static void showCityAverageData() {
        TransactionManager tm = AppConfig.singleton().getTransactionManager();
        KakoDataDaoImpl dao = new KakoDataDaoImpl();
        
        tm.required(() -> {
            dao.selectCityAverage().forEach(data -> {
                System.out.println(data);
            });
        });
    }
    
    static List<KakoData> fetchPastDataPerCity(Map<String, String> city) {
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
                .append(BODY_FRAGMENT)
                .append("&").append("courtId=").append(city.get("courtId"))
                .append("&").append("municipalityId=").append(city.get("municipalityId")).toString();
        con.requestBody(body);
        
        Document doc;
        try {
            doc = con.post();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        List<KakoData> perCity = new ArrayList<KakoData>();
        
        doc.select("tr:has(.blk_largetxt)")
           .forEach(tr -> {
               KakoData singleData = new KakoData();
               singleData.basePrice = new Integer(tr.select(".blk_largetxt").text().replaceAll(",", ""));
               singleData.actualPrice = new Integer(tr.select(".org_largetxt").text().replaceAll(",", ""));
               singleData.municipalityId = city.get("municipalityId");
               
               perCity.add(singleData);
            });
        
        return perCity;
    }
}

