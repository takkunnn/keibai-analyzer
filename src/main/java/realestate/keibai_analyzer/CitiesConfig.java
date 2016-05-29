package realestate.keibai_analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

import com.sun.org.apache.xpath.internal.XPathAPI;

class CitiesConfig {
    
    private static List<Map<String, String>> loadedCities = load();
    
    static List<Map<String, String>> list() {
        return loadedCities;
    }
    
    private static List<Map<String, String>> load() {
        NodeIterator iter;
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                                                 .newDocumentBuilder()
                                                 .parse(CitiesConfig.class.getResourceAsStream("/city.xml"));
            iter = XPathAPI.selectNodeIterator(doc, "/cities/city");
        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
        
        List<Map<String, String>> cities = new ArrayList<Map<String, String>>();
        
        Node node;
        while((node = iter.nextNode()) != null) {
            Map<String, String> city = new HashMap<String, String>();
            
            try {
                city.put("municipalityId", XPathAPI.selectSingleNode(node, "municipalityId").getTextContent());
                city.put("name", XPathAPI.selectSingleNode(node, "name").getTextContent());
                city.put("courtId", XPathAPI.selectSingleNode(node, "courtId").getTextContent());
                cities.add(city);
            } catch (DOMException | TransformerException e) {
                throw new RuntimeException(e);
            }
        }
        
        return cities;
    }

}
