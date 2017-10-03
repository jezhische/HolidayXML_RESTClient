package util;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class RetrievedXMLDocSplitter {

    // write xml document with the content of responseEntity
    public static void writeXMLDoc(String XMLFileName, String responseEntity) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(XMLFileName))) {
            bufferedWriter.write(responseEntity);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write new divorced xsd and xml files
    public static void writeXSDDoc(String XMLFileName, String XSDFileName) {
        List<String> content = readAndSplitXMLDoc(XMLFileName);
        String XMLContent = content.get(0);
        String XSDContent = content.get(1);
        try(BufferedWriter XMLWriter = new BufferedWriter(new FileWriter(XMLFileName));
            BufferedWriter XSDWriter = new BufferedWriter(new FileWriter(XSDFileName))) {
            XMLWriter.write(XMLContent);
            XMLWriter.flush();
            XSDWriter.write(XSDContent);
            XSDWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // read and split text of xml file to xml and xsd schema
    private static List<String> readAndSplitXMLDoc(String XMLFileName) {
        StringBuilder XMLText = new StringBuilder()
                , XSDText = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        if (new File(XMLFileName).exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(XMLFileName))) {
                String temp = null;
                while ((temp = bufferedReader.readLine()) != null)
                    if (temp.contains("xs"))
                        XSDText.append(temp + "\n");
                    else XMLText.append(temp + "\n");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Arrays.asList(XMLText.toString(), XSDText.toString());
    }
}
