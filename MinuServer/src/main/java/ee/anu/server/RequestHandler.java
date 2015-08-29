package ee.anu.server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public interface RequestHandler {

    // TODO parameetriks Request objekt
    Response handleRequest(Request request) throws IOException, URISyntaxException;

}
