package ee.anu.server;

public class RawRequest {
    byte[] headers;
    byte[] RequestBody;

    public RawRequest(byte[] headers, byte[] requestBody) {
        this.headers = headers;
        RequestBody = requestBody;
    }

    public byte[] getHeaders() {
        return headers;
    }

    public byte[] getRequestBody() {
        return RequestBody;
    }
}
