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

public class ArtHtmlHandler implements RequestHandler {
    private final String template;
    private final RequestHandler defaultHandler;
    private final ArtRepository artRepository;


    public ArtHtmlHandler(RequestHandler defaultHandler, ArtRepository artRepository) throws IOException {
        this.defaultHandler = defaultHandler;
        this.artRepository = artRepository;
        try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web/art.html")) {
            template = new String(getBytes(resourceAsStream));
        }
    }

    @Override
    public Response handleRequest(Request request) throws IOException, URISyntaxException {
        if (request.getRequestURI().startsWith("/art")) {
            try {
                return getArtMainPageResponse();
            } catch (SQLException e) {
                e.printStackTrace();
                Map<String, String> headers = new HashMap<>();
                byte[] data = new byte[0];
                headers.put("Content-Length", String.valueOf(data.length));
                return new Response(headers, data, Status.InternalError);
            }
        } else {
            return defaultHandler.handleRequest(request);
        }
    }

    private Response getArtMainPageResponse() throws IOException, SQLException, URISyntaxException {
        try {
            List<Art> artXhtmlResponse = artRepository.getArtMainData();
            byte[] responseBytes = getReplacedHtmlValues(artXhtmlResponse).getBytes();
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "text/html; charset=UTF-8");
            headers.put("Content-Length", String.valueOf(responseBytes.length));
            return new Response(headers, responseBytes, Status.OK);
        } catch (NoSuchElementException e) {
            Map<String, String> headers = Collections.singletonMap("Content-Length", "0");
            return new Response(headers, new byte[0], Status.NotFound);
        }
    }

    //htmli sees olevate src lünkade asendamiseks ->siia tulevad urlid
    private String getReplacedHtmlValues(List<Art> artMainPageData) {
        String responseData = template;
        StringBuilder divsReplacement = new StringBuilder();

        for (Art anArtMainPageData : artMainPageData) {
            String linkToDetailedInfo = anArtMainPageData.getLinkToDetailedInfo();
            String iconUrl = anArtMainPageData.getIconUrl();
            String nimi = anArtMainPageData.getNimi();
            divsReplacement.append("<div class=\"pic-container\"><div class=\"pic\"><a href=\"")
                    .append(linkToDetailedInfo).append("\"><img class=\"pic-img\" src=\"").append(iconUrl)
                    .append("\" width=\"400\" height=\"260\"/> <span class=\"text-content\"><span>")
                    .append(nimi).append("</span></span></a></div></div>");
        }
        responseData = responseData.replace("@PHOTOS@", divsReplacement);

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