package amw.ecb.ecbproducer;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class XMLService {

    Document  document;
    LocalDate today = LocalDate.now();
    Currency currency;

    public List<Currency> parseCurrency() {


        String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(new URL(url).openStream());
        }catch(Exception e){
            e.printStackTrace();
        }
        NodeList list = document.getElementsByTagName("Cube");
        Node nodeTime = list.item(1);
        Element time = (Element) nodeTime;
        List<Currency> currencies = new ArrayList<>();
        for(int i = 2; i < list.getLength();i++){
            Node node = list.item(i);
            Element element = (Element) node;
            currency = new Currency(today.toString(), "EUR", element.getAttribute("currency"), Double.parseDouble(element.getAttribute("rate")) );
            currencies.add(currency);
        }
        return (currencies);
    }

    public List<Currency> parseCurrencyHist() {

        String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml";
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(new URL(url).openStream());
        }catch(Exception e){
            e.printStackTrace();
        }
        NodeList list = document.getElementsByTagName("Cube");
        String time = LocalDate.now().toString();
        List<Currency> currencies = new ArrayList<>();
        for(int i = 1; i < list.getLength();i++){
            Node nodeTime = list.item(i);
            Element element = (Element) nodeTime;
            if(element.hasAttribute("time")){
                time = element.getAttribute("time");
            }else {
                currency = new Currency(time, "EUR", element.getAttribute("currency"), Double.parseDouble(element.getAttribute("rate")) );
                currencies.add(currency);
            }
        }
        return (currencies);
    }
}
