import generated.NewDataSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Jezh
 */
public class XMLMain {
    // filename
    public static final String XML_FILE_NAME = "src/main/xsd/GetHolidaysForYear.xml";

    /**
     * @param args
     */
    public static void main(String[] args) {
        // get unmarshaller, which moves XML data into the class
        JAXBContext jaxbContext = null;
        Unmarshaller unmarshaller = null;
        try {
            jaxbContext = JAXBContext.newInstance("generated"); // this String is the path to context, and this
            // directory must contain ObjectFactory.class or jaxb.index. NB: this is the path here  to directory located
            // in the target directory (generated-sources/jaxb/generated, but since jaxb marked as Generated Sources Root,
            // so the relative path is "generated")
            unmarshaller = jaxbContext.createUnmarshaller();

            // build DOM (Document Object Model) - the tree, that represents XML data and exists entirely in memory,
            // not in the file:
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(XML_FILE_NAME));

            // Traverse the DOM until 'NewDataSet' is reached
            Element subtree = doc.getDocumentElement();
            Node node = subtree.getElementsByTagName("NewDataSet").item(0);

            // Unmarshal NewDataSet
            JAXBElement<NewDataSet> dataSet = unmarshaller.unmarshal(node, NewDataSet.class);

            // Print the holidays
            List<NewDataSet.Holidays> holidays = dataSet.getValue().getHolidays();
            holidays.forEach(holiday -> System.out.printf("%30s: %d/%d/%d\n", holiday.getName()
                    , holiday.getDate().getMonth(), holiday.getDate().getDay(), holiday.getDate().getYear()));

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
