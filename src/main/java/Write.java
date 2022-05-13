import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Write {

    public static ArrayList<Check> readCheck(String filepath) {
        ArrayList<Check> check = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new FileInputStream(filepath));

            Element rootElement = document.getDocumentElement();

            NodeList childrenOfRootElement = rootElement.getChildNodes();
            for (int i = 0; i < childrenOfRootElement.getLength(); i++) {
                Node childNode = childrenOfRootElement.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childrenOfCheckTag = childNode.getChildNodes();
                    int checkNum = 0;
                    String date = "";
                    String payer = "";
                    double amount = 0.0;
                    for (int j = 0; j < childrenOfCheckTag.getLength(); j++) {
                        Node childNodeOfCheckTag = childrenOfCheckTag.item(j);
                        if (childNodeOfCheckTag.getNodeType() == Node.ELEMENT_NODE) {
                            switch (childNodeOfCheckTag.getNodeName()) {
                                case "checkNum" -> checkNum = Integer.parseInt(childNodeOfCheckTag.getTextContent());
                                case "date" -> date = childNodeOfCheckTag.getTextContent();
                                case "payer" -> payer = childNodeOfCheckTag.getTextContent();
                                case "amount" -> amount = Double.parseDouble(childNodeOfCheckTag.getTextContent());
                            }
                        }
                    }
                    check.add(new Check(checkNum, date, payer, amount));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

}
