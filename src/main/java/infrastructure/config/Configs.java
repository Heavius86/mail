package infrastructure.config;

import org.aeonbits.owner.ConfigFactory;

public final class Configs {
    static ChromeConfig config = ConfigFactory.create(ChromeConfig.class);

    public static ChromeConfig getInstance() {

        return config;
    }
}
