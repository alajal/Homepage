package ee.anu.server;

import java.util.Map;

public class Request {
    private final String requestURI;
    private final Map<String, String> requestParametersMap;
    private final byte[] requestData;
    private final String method;

    public Request(String requestURI, Map<String, String> requestParametersMap, byte[] requestData, String method) {
        this.requestURI = requestURI;
        this.requestParametersMap = requestParametersMap;
        this.requestData = requestData;
        this.method = method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public Map<String, String> getRequestParametersMap() {
        return requestParametersMap;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public String getMethod() {
        return method;
    }
}
