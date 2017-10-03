import util.RetrievedXMLDocSplitter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

/**
 * Calls the RESTful web service at REST_URL defined below.
 *
 * @author Jezh
 */
public class XML_RESTMain {
    private static final String REST_URL = "http://www.holidaywebservice.com/Holidays/Holidayservice.asmx/" +
            "GetHolidaysForYear?countryCode=US&year=2017";
    private static final int OK_STATUS = Response.Status.OK.getStatusCode();
    private static final String DOC_XML = "src/main/xsd/GetHolidaysForYear.xml";
    private static final String DOC_XSD = "src/main/xsd/GetHolidaysForYear.xsd";

    /**
     * Calls the web service and display the response.
     *
     * @param args
     */
    public static void main(String[] args) {
        // call the service and get the response object.
        // see the interface Invocation.Builder
        // https://docs.oracle.com/javaee/7/api/javax/ws/rs/client/Invocation.Builder.html:

//        Client client = ClientBuilder.newClient();
//        WebTarget resourceTarget = client.target(REST_URL);
//        Invocation.Builder builder = resourceTarget.request(MediaType.APPLICATION_XML);
//        Response response = builder.get();
        Response response = ClientBuilder.newClient().target(REST_URL).request(MediaType.APPLICATION_XML).get();

        // process the response object
        Response.StatusType status = response.getStatusInfo();
        int statusCode = status.getStatusCode();
        if (statusCode == OK_STATUS) {
            // write the response object to external file instead of System.out.println(response.readEntity(String.class)):
            RetrievedXMLDocSplitter.writeXMLDoc(DOC_XML, response.readEntity(String.class));
            // write new divorced xsd and xml files:
            RetrievedXMLDocSplitter.writeXSDDoc(DOC_XML, DOC_XSD);
        }
        System.out.printf("Service returned status: \"%d %s\"\n", statusCode, status.getReasonPhrase());
    }
}
