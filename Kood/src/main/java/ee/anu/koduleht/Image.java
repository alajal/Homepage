package ee.anu.koduleht;

public class Image {
    private final String contentType;
    private final byte[] file;

    public Image(String contentType, byte[] file) {
        this.contentType = contentType;
        this.file = file;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getFile() {
        return file;
    }
}
