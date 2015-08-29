package ee.anu.koduleht;

import ee.anu.server.Request;
import ee.anu.server.RequestHandler;
import ee.anu.server.Response;
import ee.anu.server.Status;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.*;

public class SmarterFileHandler implements RequestHandler {

    private final String template;
    private final RequestHandler defaultHandler;
    private final ArtRepository artRepository;

    public SmarterFileHandler(RequestHandler defaultHandler, ArtRepository artRepository) throws IOException {
        this.defaultHandler = defaultHandler;
        this.artRepository = artRepository;
        try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web/template.xhtml")) {
            template = new String(getBytes(resourceAsStream));
        }
    }

    private Response getXhtmlResponse(String requestURI) throws IOException, SQLException, URISyntaxException {
        String projectId = requestURI.substring(1, requestURI.lastIndexOf("."));

        try {
            Art art = artRepository.getArt(projectId);
            byte[] responseBytes = getReplacedHtmlValues(art).getBytes();
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "text/html; charset=UTF-8");
            headers.put("Content-Length", String.valueOf(responseBytes.length));
            return new Response(headers, responseBytes, Status.OK);
        } catch (NoSuchElementException e) {
            Map<String, String> headers = Collections.singletonMap("Content-Length", "0");
            return new Response(headers, new byte[0], Status.NotFound);
        }

    }

    //composition over inheritance
    //open-closed principle
    @Override
    public Response handleRequest(Request request) throws IOException, URISyntaxException {
        if (request.getRequestURI().endsWith(".xhtml")) {
            try {
                return getXhtmlResponse(request.getRequestURI());
            } catch (SQLException e) {
                e.printStackTrace();
                Map<String, String> headers = new HashMap<>();
                byte[] data = new byte[0];
                headers.put("Content-Length", String.valueOf(data.length));
                return new Response(headers, data, Status.InternalError);
            }
        } else {    //tavaline FileHandler
            return defaultHandler.handleRequest(request);
        }
    }

    private String getReplacedHtmlValues(Art art) {
        String responseData = template;
        responseData = responseData.replace("@NIMI@", art.getNimi());
        responseData = responseData.replace("@DETAILS@", art.getAuthors());

        StringBuilder urlReplacement = new StringBuilder();
        for (ArtPoster posterData : art.getArtInfo()) {
            urlReplacement.append("<img class=\"pic-img\" src=\"").append(posterData.getUrl()).append("\" width=\"")
                    .append(posterData.getWidth()).append("\" height=\"").append(posterData.getHeight()).append("\"/><br>");
        }
        responseData = responseData.replace("@GENERATE_POSTERS@", urlReplacement);
        return responseData;
    }

    private byte[] getBytes(InputStream resourceAsStream) throws IOException {
        if (resourceAsStream == null) {
            throw new IOException("Couldn't read template.");
        }

        int read;
        byte[] buf = new byte[512];
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        while ((read = resourceAsStream.read(buf)) != -1) {
            out2.write(buf, 0, read);
        }
        return out2.toByteArray();
    }
}
