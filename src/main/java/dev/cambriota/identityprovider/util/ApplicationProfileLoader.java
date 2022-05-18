package dev.cambriota.identityprovider.util;

import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.Properties;

import io.quarkus.runtime.configuration.ProfileManager; 

/**
 * Small implementation to load config properties from file (similar to Spring Profiles).
 *
 * @see <a href="https://www.baeldung.com/spring-profiles">Spring Profiles</a>
 */
public class ApplicationProfileLoader {

    private final static Logger log = Logger.getLogger(ApplicationProfileLoader.class);

    private static Properties loadProperties(String profile) {
        Properties props = new Properties();

        log.infof("The Quarkus Profile is: %s", ProfileManager.getActiveProfile());
        log.infof("The Profile used here is: %s", profile);       
        try {
            if (profile == null) {
                props.load(ApplicationProfileLoader.class.getClassLoader().getResourceAsStream("iota-identity-provider.properties"));
            } else {
                props.load(ApplicationProfileLoader.class.getClassLoader().getResourceAsStream("iota-identity-provider-" + profile + ".properties"));
            }
            log.infof("Properties loaded [profile=%s].", profile);
            log.infof("Properties are=%s]", props.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    public static String getAuthenticationEndpoint() {
        String profile = System.getProperty("profile");
        Properties props = loadProperties(profile);
        return props.getProperty("authentication.endpoint");
    }

    public static boolean isSkipVerification() {
        String profile = System.getProperty("profile");
        Properties props = loadProperties(profile);
        return Boolean.parseBoolean(props.getProperty("verification.skip", "false"));
    }

    public static String getSidecarUrl() {
        String profile = System.getProperty("profile");
        Properties props = loadProperties(profile);
        String sidecarUrl = props.getProperty("sidecar.url");
        if (sidecarUrl == null) {
            log.warn("No configuration found for \"sidecar.url\".");
        }
        return sidecarUrl;
    }
}
