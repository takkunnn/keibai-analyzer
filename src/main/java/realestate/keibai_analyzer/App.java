package realestate.keibai_analyzer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        new App().collectPastKeibaiData();
    }
    
    private void collectPastKeibaiData() {
        Connection con = Jsoup.connect("http://bit.sikkou.jp/app/past/pt003/h20").header("Host", "bit.sikkou.jp")
                .header("Connection", "keep-alive").header("Content-Length", "768").header("Cache-Control", "max-age=0")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Origin", "http://bit.sikkou.jp").header("Upgrade-Insecure-Requests", "1")
                // .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64)
                // AppleWebKit/537.36 (KHTML", like Gecko) Chrome/48.0.2564.116
                // Safari/537.36")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Referer", "http://bit.sikkou.jp/app/past/pl003/h05").header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "ja,en-US;q=0.8,en;q=0.6")
                .header("Cookie", "HEAD_COUNTER=2078; HEAD_TIMESTAMP=20160522063004; MOJI_SIZE=1")
                .requestBody("menuKey=&mojiSize=1&screenId=SCPT003&screenURI=SCPT001%2CSCPT003&maxage=2147483647&courtId=31311&prefecturesId=11&hdnPeriodId=3&hdnFiscalYearId=2016&hdnEraId=04&hdnBuildYear=13&hdnObjPeriodId=1&kind=2&ymSelect=0&periodId=3&municipalityId=4320&saleAmountLowerId=0&saleAmountUpperId=300&landClsId=00&buildingCoverageLower=&buildingCoverageUpper=&floorAreaRatioLower=&floorAreaRatioUpper=&landAreaLower=&landAreaUpper=&floorAreaLower=&floorAreaUpper=&exclusiveAreaLower=&exclusiveAreaUpper=&structure1Id=00&structure2Id=00&roomArrangementId=99&storyLower=&storyUpper=&buildYearSelect=1&eraId=04&buildYear=13&objPeriodId=1&groundUndergroundId=0&floorLower=&floorUpper=&adminExpensesLower=&adminExpensesUpper=&balconyAreaLower=&balconyAreaUpper=&totalNoLower=&totalNoUpper=");

        Document doc;
        try {
            doc = con.post();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        doc.select("tr:has(.blk_largetxt)")
           .forEach(tr -> {
               BigDecimal base = new BigDecimal(tr.select(".blk_largetxt").text().replaceAll(",", ""));
               BigDecimal actual = new BigDecimal(tr.select(".org_largetxt").text().replaceAll(",", ""));
               System.out.println(base + "," + actual + "," + actual.divide(base, 2, RoundingMode.HALF_UP));
            });
        
    }
    
}

