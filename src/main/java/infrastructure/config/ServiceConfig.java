package infrastructure.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.*;

@LoadPolicy(LoadType.MERGE)
@Sources({
        "file:src/test/resources/application-default.properties",
        "system:properties",
        "system:env"
})
public interface ServiceConfig extends Config {

    @Key("stand-url")
    String BaseUrl();

    @Key("test")
    String keyTest();
}
