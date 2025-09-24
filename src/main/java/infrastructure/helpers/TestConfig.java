package infrastructure.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Basic test configuration params.
 * Warning: SSH secret key must contain prefix!
 */
public class TestConfig {
    private static final ThreadLocal<TestConfig> tlInstanceContainer = new ThreadLocal<>();
    private final Properties properties;

    public final String standUrl;
    public final String adminLogin;
    public final String adminPassword;
    public final String userLogin;
    public final String userPassword;
    public final String sshSecretKey;
    public final boolean isCleaningRequired;
    public final String sshHost;
    public final int sshPort;
    public final String sshUser;
    public final String sshKnownHosts;
    public final String backupPath;
    public final String sshPassword;


    { // fill fields
        properties = loadProperties();
        standUrl = getOrDefault(ConfigEntity.STAND_URL, true);
        adminLogin = getOrDefault(ConfigEntity.ADMIN_LOGIN, true);
        adminPassword = getOrDefault(ConfigEntity.ADMIN_PASSWORD, true);
        userLogin = getOrDefault(ConfigEntity.USER_LOGIN, true);
        userPassword = getOrDefault(ConfigEntity.USER_PASSWORD, true);
        isCleaningRequired = Optional.ofNullable(getOrDefault(ConfigEntity.IS_CLEANING_REQUIRED, false))
                .map(v -> v.equals("true")).orElse(false);
        sshSecretKey = getOrDefault(ConfigEntity.SSH_SECRET_KEY, isCleaningRequired);
        sshHost = getOrDefault(ConfigEntity.SSH_HOST, isCleaningRequired);
        sshUser = getOrDefault(ConfigEntity.SSH_USER, isCleaningRequired);
        sshPort = Optional.ofNullable(getOrDefault(ConfigEntity.SSH_PORT, isCleaningRequired))
                .map(Integer::parseInt).orElse(22);
        sshKnownHosts = getOrDefault(ConfigEntity.SSH_KNOWN_HOSTS, isCleaningRequired);
        backupPath = getOrDefault(ConfigEntity.PATH_TO_BACKUP, isCleaningRequired);
        sshPassword = getOrDefault(ConfigEntity.SSH_PASSWORD, isCleaningRequired);
    }

    /**
     * Get thread-sefa instance of this class.
     * @return new or existing instance
     */
    public static TestConfig getInstance() {
        if (tlInstanceContainer.get() == null) tlInstanceContainer.set(new TestConfig());
        return tlInstanceContainer.get();
    }

    private String getOrDefault(ConfigEntity configEntity, boolean required) {
        String value = Optional.ofNullable(System.getenv(configEntity.envName)).orElse(properties.getProperty(configEntity.propName));
        if (value == null && required) throw new RuntimeException(
                String.format("Параметр конфигурации \"%s\" или переменная окружения \"%s\" обязательны, но не найдены", configEntity.propName, configEntity.envName));
        return value;
    }

    private static Properties loadProperties() {
        String env = System.getenv("ENVIRONMENT");
        Properties envProps = loadPropByName(env);
        Properties defaultProps = loadPropByName("default");
        return Stream.of(defaultProps, envProps, System.getProperties())
                .collect(Properties::new, Map::putAll, Map::putAll);
    }

    private static Properties loadPropByName(String propName) {
        Properties properties = new Properties();
        try (InputStream inputStream = TestConfig.class.getClassLoader().getResourceAsStream("application-" + propName + ".properties")) {
            properties.load(inputStream);
        } catch (IOException | NullPointerException ignored) {}
        return properties;
    }

    /**
     * All available configuration entities as pair with environment variable and java property name.
     */
    private enum ConfigEntity {
        STAND_URL("STAND_URL", "stand-url"),
        ADMIN_LOGIN("ADMIN_LOGIN", "admin-login"),
        ADMIN_PASSWORD("ADMIN_PASSWORD", "admin-password"),
        USER_LOGIN("USER_LOGIN", "user-login"),
        USER_PASSWORD("USER_PASSWORD", "user-password"),
        IS_CLEANING_REQUIRED("IS_CLEANING_REQUIRED", "is-cleaning-required"),
        SSH_SECRET_KEY("SSH_SECRET_KEY", "ssh-secret-key"),
        SSH_HOST("SSH_HOST", "ssh-host"),
        SSH_USER("SSH_USER", "ssh-user"),
        SSH_PORT("SSH_PORT", "ssh-port"),
        SSH_KNOWN_HOSTS("SSH_KNOWN_HOSTS", "ssh-known-hosts"),
        PATH_TO_BACKUP("PATH_TO_BACKUP", "backup-path"),
        SSH_PASSWORD("SSH_PASSWORD", "ssh-password");

        private static final String PROPS_PREFIX = "";
        public final String envName;
        public final String propName;

        ConfigEntity(String envName, String propName) {
            this.envName = envName;
            this.propName = PROPS_PREFIX + propName;
        }
    }
}
