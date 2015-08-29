package ee.anu.koduleht;

public class ArtPoster {
    private final String url;
    private final int width;
    private final int height;
    private final String projectId;
    //siia teha posteri urlid?

    public ArtPoster(String projectId, String url, int width, int height) {
        this.projectId = projectId;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getProjectId() {
        return projectId;
    }
}
