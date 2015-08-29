package ee.anu.koduleht;

import java.util.List;

public class Art {

    private final String id;
    private final String nimi;
    private final String authors;
    private final List<ArtPoster> posterURL;
    private final String iconUrl;
    private final String linkToDetailedInfo;

    public Art(String id, String nimi, String authors, List<ArtPoster> posterURLs, String iconUrl, String linkToDetailedInfo) {
        this.id = id;
        this.nimi = nimi;
        this.authors = authors;
        this.posterURL = posterURLs;
        this.iconUrl = iconUrl;
        this.linkToDetailedInfo = linkToDetailedInfo;
    }

    public String getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }

    public String getAuthors() {
        return authors;
    }

    public List<ArtPoster> getArtInfo() {
        return posterURL;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getLinkToDetailedInfo() {
        return linkToDetailedInfo;
    }
}
