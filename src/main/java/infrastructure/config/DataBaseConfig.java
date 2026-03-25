package infrastructure.config;


import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;


@LoadPolicy(LoadType.MERGE)
@Sources({
        "file:src/test/resources/database.properties",
        "system:properties",
        "system:env"
})
public interface DataBaseConfig extends Config {

    @Key("user")
    String userDB();

    @Key("password")
    String passwordDB();

    @Key("url")
    String urlDB();
}