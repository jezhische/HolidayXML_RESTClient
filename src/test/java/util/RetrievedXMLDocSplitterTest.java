package util;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.*;

public class RetrievedXMLDocSplitterTest {

    private String XMLFilename, XSDFilename, responseEntity;

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        File returnedFile = folder.newFile("src/test/java/util/temp/testGetHolidayForYear.xml");
        XMLFilename = returnedFile.getPath();
        XSDFilename = XMLFilename.substring(0, XMLFilename.length() - 2) + "sd";
        responseEntity = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<DataSet xmlns=\"http://www.27seconds.com/Holidays/\">\n" +
                "  <xs:schema id=\"NewDataSet\" xmlns=\"\"     </xs:element>\n" +
                "  </xs:schema>\n" +
                "  <diffgr:diffgram xmlns:";
    }

    @After
    public void tearDown() throws Exception {
        new File(XMLFilename).delete();
        new File(XSDFilename).delete();
        XSDFilename = null;
        XMLFilename = null;
        responseEntity = null;
    }

    @Test
    public void writeXMLDoc() throws Exception {
        RetrievedXMLDocSplitter.writeXMLDoc(XMLFilename, responseEntity);
        File test = new File(XMLFilename);
        assertTrue(test.exists());
        BufferedReader br = new BufferedReader(new FileReader(test));
        StringBuilder fromFile = new StringBuilder("");
        int i = 0;
        while((i = br.read()) != -1)
            fromFile.append((char) i);
        br.close();
        assertEquals(responseEntity, fromFile.toString());
    }

    @Test
    public void writeXSDDoc() throws Exception {
        RetrievedXMLDocSplitter.writeXMLDoc(XMLFilename, responseEntity);
        RetrievedXMLDocSplitter.writeXSDDoc(XMLFilename, XSDFilename);
        BufferedReader XMLFileReader = new BufferedReader(new FileReader(XMLFilename));
        BufferedReader XSDFileReader = new BufferedReader(new FileReader(XSDFilename));
        String XMLFileContent = "", XSDFileContent = "", TempXSDFileContent = "";
        while ((XMLFileContent = XMLFileReader.readLine()) != null)
            assertFalse(XMLFileContent.contains("xs"));
        while ((TempXSDFileContent = XSDFileReader.readLine()) != null)
            XSDFileContent += TempXSDFileContent;
        assertTrue(XSDFileContent.contains("xs"));
        XMLFileReader.close();
        XSDFileReader.close();
    }

    @Test
    public void readAndSplitXMLDoc() throws Exception {
        RetrievedXMLDocSplitter.writeXMLDoc(XMLFilename, responseEntity);
        Class clazz = RetrievedXMLDocSplitter.class;
        Method privateMethod = clazz.getDeclaredMethod("readAndSplitXMLDoc", String.class);
        privateMethod.setAccessible(true);
        List<String> testList = (List<String>) privateMethod.invoke(clazz.newInstance(), XMLFilename);
        assertFalse(testList.get(0).contains("xs"));
        assertTrue(testList.get(1).contains("xs"));
    }

}