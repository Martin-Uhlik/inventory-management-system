package cz.muni.fi.pb175.project.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class loads variables from database config file.
 *
 * @author Martin Uhlik
 */
public final class DatabaseConfiguration {
    private final String url;
    private final String name;
    private final String password;

    public DatabaseConfiguration() throws IOException {
        Properties prop = new Properties();
        String fileName = "." + File.separator + "database.conf";
        InputStream is = new FileInputStream(fileName);
        prop.load(is);

        url = prop.getProperty("url");
        name = prop.getProperty("name");
        password = prop.getProperty("password");
        is.close();
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
