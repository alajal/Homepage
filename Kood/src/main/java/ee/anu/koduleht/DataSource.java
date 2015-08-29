package ee.anu.koduleht;

import jdk.nashorn.internal.ir.IfNode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {
    public Connection getConnection() throws SQLException, URISyntaxException, IOException {
        Connection localProperties = getLocalProperties();
        if (localProperties != null) {
            return localProperties;
        }

        Connection herokuProperties = getHerokuProperties();
        if (herokuProperties != null) {
            return herokuProperties;
        }

        throw new RuntimeException("Couldn't get database properties.");
    }

    private Connection getLocalProperties() throws IOException, SQLException {
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("database.properties")){
            if (resourceAsStream == null){
                return null;
            }
            Properties props = new Properties();
            props.load(resourceAsStream);
            return DriverManager.getConnection(props.getProperty("url"), props.getProperty("user"), props.getProperty("passwd"));
        }
    }

    private Connection getHerokuProperties() throws URISyntaxException, SQLException {
        String database_url = System.getenv("DATABASE_URL");
        if (database_url == null){
            return null;
        }
        URI dbUri = new URI(database_url);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        return DriverManager.getConnection(dbUrl, username, password);
    }
}
