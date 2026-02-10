package infrastructure.config;

import com.codeborne.selenide.TextCheck;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Converter;

import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Paths;

@LoadPolicy(LoadType.MERGE)
@Sources({
        "file:src/test/resources/application-default.properties",
        "system:properties",
        "system:env"
})
public interface ChromeConfig extends Config {

    @Key("stand-url")
    @ConverterClass(TextCheckConverter.class)
    String BaseUrl();

    @ConverterClass(UrlChromeConverter.class)
    @DefaultValue("is_docker")
    URL UrlChrome();

    @Key("savePageSource")
    Boolean savePageSource();

    @Key("screenshots")
    Boolean screenshots();

    @Key("downloadsFolder")
    String downloadsFolder();

    @Key("reportsFolder")
    String reportsFolder();

    @Key("timeout")
    Integer timeout();

    @Key("browserSize")
    String browserSize();

    @Key("pageLoadStrategy")
    String pageLoadStrategy();

    @Key("pageLoadTimeout")
    Integer pageLoadTimeout();

    @Key("reopenBrowserOnFail")
    Boolean reopenBrowserOnFail();

    @Key("webdriverLogsEnabled")
    Boolean webdriverLogsEnabled();

    @Key("defaultDuration")
    Integer defaultDuration();

    @Key("textCheck")
    @ConverterClass(TextCheckConverter.class)
    TextCheck textCheck();

    @Key("user.dir")
    String user_dir();

    @Key("user.dir")
    @ConverterClass(PathToReportConverter.class)
    String path_to_report();

    @Key("user.dir")
    @ConverterClass(PathDownloadsFoldertConverter.class)
    String pathDownloadsFolder();
}