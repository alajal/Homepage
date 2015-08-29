package ee.anu.koduleht;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ArtRepository {
    private final DataSource dataSource;

    public ArtRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Art getArt(String projectId) throws IOException, SQLException, URISyntaxException {
        try (Connection connection = dataSource.getConnection()) {
            Map<String, String> artMainData = getArtMainDataByID(projectId, connection);
            List<ArtPoster> artPosterURLs = getArtProjectInfo(projectId, connection);
            return new Art(artMainData.get("id"), artMainData.get("name"), artMainData.get("authors"),
                    artPosterURLs, artMainData.get("iconUrl"), artMainData.get("linkToDetailedInfo"));
        }
    }

    private List<ArtPoster> getArtProjectInfo(String projectId, Connection connection) throws SQLException {
        List<ArtPoster> posterInfo = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("select posterurl, width, height from poster where project_id= ?")) {
            statement.setString(1, projectId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ArtPoster artPoster = new ArtPoster(projectId, resultSet.getString("posterurl"), resultSet.getInt("width"), resultSet.getInt("height"));
                    posterInfo.add(artPoster);
                }
            }
        }
        return posterInfo;
    }

    //kindla idega arti saamiseks
    private Map<String, String> getArtMainDataByID(String projectId, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("select * from art where id= ? ")) {
            statement.setString(1, projectId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new NoSuchElementException("Puudub sellise id-ga projekt:" + projectId);
                }
                Map<String, String> mainData = new HashMap<>();
                mainData.put("id", resultSet.getString("id"));
                mainData.put("name", resultSet.getString("name"));
                mainData.put("authors", resultSet.getString("authors"));
                mainData.put("iconUrl", resultSet.getString("icon_url"));
                mainData.put("linkToDetailedInfo", resultSet.getString("link"));
                return mainData;
            }
        }
    }


    public List<Art> getArtMainData() throws SQLException, IOException, URISyntaxException {
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement("select * from art")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Art> arts = new ArrayList<>();
                    while (resultSet.next()){
                        Art art = new Art(resultSet.getString("id"), resultSet.getString("name"),resultSet.getString("authors"),
                                getPostersUrls(), resultSet.getString("icon_url"), resultSet.getString("link"));
                        arts.add(art);
                    }
                    return arts;
                }
            }
        }
    }

    //see meetod ei tee päris õiget asja
    private List<ArtPoster> getPostersUrls() throws SQLException, IOException, URISyntaxException {
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement("select * from poster")){
                try (ResultSet resultSet = statement.executeQuery()){
                    List<ArtPoster> posters = new ArrayList<>();
                    while (resultSet.next()){
                        ArtPoster artPoster = new ArtPoster(resultSet.getString("project_id"), resultSet.getString("posterurl"),
                                resultSet.getInt("width"), resultSet.getInt("height"));
                        posters.add(artPoster);
                    }
                    // todo võtta kindla idga posterite urlid
                    return posters;
                }
            }
        }
    }

    public Image getImages(String fileName) throws IOException, SQLException, URISyntaxException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT file, content_type from files where name= ?")) {
                statement.setString(1, fileName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new NoSuchElementException("Puudub sellise id-ga ikoonpilt: " + fileName);
                    }
                    String content_type = resultSet.getString("content_type");
                    byte[] data = resultSet.getBytes("file");
                    return new Image(content_type, data);

                }
            }
        }
    }

}