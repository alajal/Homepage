package ee.anu.koduleht;


import ee.anu.server.Request;
import ee.anu.server.RequestHandler;
import ee.anu.server.Response;
import ee.anu.server.Status;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ArtImgHandler implements RequestHandler {
    private final RequestHandler defaultHandler;
    private final ArtRepository artRepository;

    public ArtImgHandler(RequestHandler defaultHandler, ArtRepository artRepository) {
        this.defaultHandler = defaultHandler;
        this.artRepository = artRepository;
    }

    @Override
    public Response handleRequest(Request request) throws IOException, URISyntaxException {
        if (request.getRequestURI().startsWith("/assets")) {
            try {
                Image image = artRepository.getImages(request.getRequestURI());
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", image.getContentType() + "; charset=UTF-8");
                headers.put("Content-Length", String.valueOf(image.getFile().length));
                return new Response(headers, image.getFile(), Status.OK);
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

}
