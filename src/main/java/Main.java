import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        String filepath = "src/main/resources/checks.xml";
        ArrayList<Check> checks = Write.readCheck(filepath);
        int choice = -1;
        while (choice != 0) {
            System.out.println("""
                    1 - List checks\r
                    2 - Add new check\r
                    3 - Modify check\r
                    4 - Delete check\r
                    0 - Exit""");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 0 || 4 < choice) {
                    System.out.println("Not a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Not a valid option.");
                scanner.nextLine();
            }
            switch (choice) {
                case 1 -> {
                    if(checks.isEmpty()){
                        System.out.println("There are no checks\n");
                    }else{
                        System.out.println(checks);
                    }
                }
                case 2 -> addCheck(checks);
                case 3 -> {
                    if (checks.isEmpty()) {
                        System.out.println("There are no checks\n");
                    } else{
                        modify(checks);
                    }
                }
                case 4 -> {
                    if (checks.isEmpty()) {
                        System.out.println("There are no checks\n");
                    } else{
                        deleteCheck(checks);
                    }
                }
            }
        }
        saveChecksToXml(checks, filepath);
    }

    private static void addCheck(ArrayList<Check> checks) {
        int checkNum = readCheckNum();
        String date = readDate();
        System.out.print("Enter payer: ");
        String payer = scanner.nextLine();
        double amount = readAmount();
        checks.add(new Check(checkNum, date, payer, amount));
    }

    private static void modify(ArrayList<Check> checks) {
        Check check = findCheckIn(checks);
        String date = readDate();
        System.out.print("Enter payer: ");
        String payer = scanner.nextLine();
        double amount = readAmount();
        checks.set(checks.indexOf(check), new Check(check.getCheckNum(), date, payer, amount));
    }

    private static void deleteCheck(ArrayList<Check> checks) {
        checks.remove(findCheckIn(checks));
    }

    private static Check findCheckIn(ArrayList<Check> checks) {
        Check check = new Check();
        int checkNum = 0;
        while (checkNum == 0) {
            try {
                System.out.print("Enter the check ####: ");
                checkNum = scanner.nextInt();
                for (Check CheckElement : checks) {
                    if (CheckElement.getCheckNum() == checkNum) {
                        return CheckElement;
                    }
                }
            }catch (InputMismatchException e){
                System.out.println("Enter a valid check ####");
                scanner.nextLine();
            }
            checkNum = 0;
        }
        return check;
    }

    private static int readCheckNum() {
        int checkNum = -1;
        while (checkNum == -1) {
            try {
                do{
                    System.out.print("Enter check ####: ");
                    checkNum = scanner.nextInt();
                }while(checkNum < 0);
            } catch (InputMismatchException e) {
                System.out.println("Enter an integer");
                scanner.nextLine();
            }
        }
        return checkNum;
    }


    private static String readDate() {
        String date = "";
        while (date.isEmpty()) {
            System.out.print("Enter date: ");
            date = scanner.nextLine();
        }
        return date;
    }

    private static double readAmount() {
        double amount = 0.0;
        while (amount == 0.0) {
            try {
                do{
                    System.out.print("Enter amount: ");
                    amount = scanner.nextDouble();
                }while(amount < 0);
            } catch (InputMismatchException e) {
                System.out.println("Not valid amount.");
                scanner.nextLine();
            }
        }
        return amount;
    }

    public static void saveChecksToXml(ArrayList<Check> checks, String filepath) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element rootElement = document.createElement("checks");
            document.appendChild(rootElement);

            for (Check check : checks) {
                Element checkElement = document.createElement("check");
                rootElement.appendChild(checkElement);
                createChildElement(document, checkElement, "checkNum", String.valueOf(check.getCheckNum()));
                createChildElement(document, checkElement, "date", check.getDate());
                createChildElement(document, checkElement, "payer", check.getPayer());
                createChildElement(document, checkElement, "amount", String.valueOf(check.getAmount()));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);

            StreamResult result = new StreamResult(new FileOutputStream(filepath));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createChildElement(Document document, Element parent, String tagName, String value) {
        Element element = document.createElement(tagName);
        element.setTextContent(value);
        parent.appendChild(element);
    }
}