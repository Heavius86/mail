package infrastructure.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import infrastructure.helpers.ParseUrl;
import infrastructure.utils.Constants;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.ConcurrentInitializer;
import org.apache.commons.lang3.concurrent.LazyInitializer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Backend {
    private static TestConfig config = TestConfig.getInstance();

    protected static final String standAndCompanyUrl = config.standUrl;

    private static final ConcurrentInitializer<String> lazyTokenInitializerAdmin;
    private static final ConcurrentInitializer<String> lazyTokenInitializerUser;

    static {
        lazyTokenInitializerAdmin = new LazyInitializer<>() {

            @Override
            protected String initialize() {
                try {
                    return createAuthToken(config.adminLogin, config.adminPassword);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        lazyTokenInitializerUser = new LazyInitializer<String>() {

            @Override
            protected String initialize() {
                try {
                    return createAuthToken(config.userLogin, config.userPassword);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public static String createAuthToken(String login, String password) throws IOException {
        String standUrl = config.standUrl;
        String vahterUrl;
        String origin;
        if (ParseUrl.checkUrl(standUrl)) {
            vahterUrl = String.format("%s://%s.%s/guard/login", ParseUrl.getProtocol(standUrl), ParseUrl.getStandName(standUrl), ParseUrl.getLocation(standUrl));
            origin = standAndCompanyUrl;
        } else {
            vahterUrl = standAndCompanyUrl + "/guard/login";
            origin = standUrl;
        }
        String json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", login, password);
        String responseString = new HttpClient()
                .setUrl(vahterUrl)
                .setJsonBody(json)
                .setProperty("Origin", origin)
                .setProperty("Content-Type", "application/json")
                .sendPost();

        return parseToken(responseString);
    }

    private static String parseToken(String body) {
        Pattern pattern = Pattern.compile("\"token\":\"(.*)\"");
        Matcher matcher = pattern.matcher(body);
        String token = "";
        if (matcher.find()) {
            token = matcher.group(1);
        }

        return token;
    }

    public static String getAuthTokenAdmin() {
        try {
            return lazyTokenInitializerAdmin.get();
        } catch (ConcurrentException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getAuthTokenUser(){
        try {
            return lazyTokenInitializerUser.get();
        } catch (ConcurrentException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean checkToken(String token) {
        try {
          return  new HttpClient()
                    .setUrl(standAndCompanyUrl + "/pub/v1/help")
                    .setProperty("Authorization", "Bearer " + token)
                    .setProperty("Content-Type", "application/json")
                    .checkCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
