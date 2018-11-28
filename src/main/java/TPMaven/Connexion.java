package TPMaven;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Céline MERAND
 * Created on 28/11/2018
 */
public class Connexion {

    private static final String FILE = "/info.properties";
    private static Connexion ourInstance = new Connexion();

    public static Connexion getInstance() {
        return ourInstance;
    }

    private final String version;
    private final String jdbc;
    private final String user;
    private final String password;

    private Connexion() {
        Properties prop = new Properties();

        try (InputStream is = getClass().getResourceAsStream(FILE)) {
            prop.load(is);
        } catch (IOException e) {
            throw new UnsupportedOperationException("IOException " + e);
        }

        this.version = prop.getProperty("version");
        this.jdbc = prop.getProperty("jdbc");
        this.user = prop.getProperty("login");
        this.password = prop.getProperty("password");
    }

    public String getVersion() {
        return version;
    }

    public String getJdbc() {
        return jdbc;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
