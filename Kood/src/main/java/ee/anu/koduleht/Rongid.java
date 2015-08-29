package ee.anu.koduleht;

import ee.anu.server.Request;
import ee.anu.server.RequestHandler;
import ee.anu.server.Response;
import ee.anu.server.Status;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Rongid implements RequestHandler {


    @Override
    public Response handleRequest(Request request) throws UnsupportedEncodingException {
        String result = java.net.URLDecoder.decode(request.getRequestURI(), "UTF-8");
        String[] splittedrequestURI = result.split("/");
        //String requestURIendpoint = splittedrequestURI[0];
        byte[] contentData = splittedrequestURI[1].getBytes("UTF-8");

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain; charset=UTF-8");
        headers.put("Content-Length", String.valueOf(contentData.length));

        return new Response(headers, contentData, Status.OK);

    }

}
