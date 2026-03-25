package infrastructure.config;

import org.aeonbits.owner.ConfigFactory;

public final class Configs {
    static private final ChromeConfig config = ConfigFactory.create(ChromeConfig.class);

    static private final DataBaseConfig configDb = ConfigFactory.create(DataBaseConfig.class);


    public static ChromeConfig getInstance() {
        return config;
    }

    public static DataBaseConfig getInstanceDB() {
        return configDb;
    }

}
