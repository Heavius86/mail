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

    public static void createApplication(String sectionName, String appName) {
        String json = String.format("{\n" +
                "    \"name\": \"%s\",\n" +
                "    \"elementName\": \"%s\",\n" +
                "    \"code\": \"%s\",\n" +
                "    \"namespace\": \"%s\",\n" +
                "    \"appViewType\": \"STANDARD\",\n" +
                "    \"icon\": \"system_cloud\",\n" +
                "    \"pageData\": {\n" +
                "        \"layout\": \"tiles\",\n" +
                "        \"menu\": {\n" +
                "            \"type\": \"namespace\"\n" +
                "        },\n" +
                "        \"widgets\": [\n" +
                "            {\n" +
                "                \"type\": \"appview\",\n" +
                "                \"data\": {\n" +
                "                    \"namespaceCode\": \"%s\",\n" +
                "                    \"pageCode\": \"%s\"\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"pageType\": \"APPLICATION\",\n" +
                "    \"sort\": 1,\n" +
                "    \"system\": false,\n" +
                "    \"__permissions\": {\n" +
                "        \"inheritParent\": false,\n" +
                "        \"values\": [\n" +
                "            {\n" +
                "                \"group\": {\n" +
                "                    \"type\": \"group\",\n" +
                "                    \"id\": \"fda5c295-230a-5025-9797-b8b4e99e08aa\"\n" +
                "                },\n" +
                "                \"types\": [\n" +
                "                    \"read\"\n" +
                "                ],\n" +
                "                \"inherited\": false\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}", appName, appName, appName.toLowerCase(Locale.ROOT), sectionName.toLowerCase(Locale.ROOT), sectionName.toLowerCase(Locale.ROOT), appName.toLowerCase(Locale.ROOT));
        try {
            new HttpClient()
                    .setUrl(standAndCompanyUrl + "/api/apps")
                    .setJsonBody(json)
                    .setProperty("Cookie", "vtoken=" + getAuthTokenAdmin())
                    .setProperty("Content-Type", "application/json")
                    .sendPost();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createPage(String sectionName, String pageName) {
        String json = String.format("{\n" +
                "   \"namespace\": \"%s\",\n" +
                "   \"type\": \"PAGE\",\n" +
                "   \"name\": \"%s\",\n" +
                "   \"code\": \"%s\",\n" +
                "   \"icon\": \"file_type_text\",\n" +
                "   \"data\": {\n" +
                "       \"columns\": 1, \n" +
                "       \"widgets\": [], \n" +
                "       \"menu\": { \n" +
                "           \"type\": \"main\" \n" +
                "           } \n" +
                "       },\n" +
                "   \"sort\": 1,\n" +
                "   \"__permissions\": {\n" +
                "       \"inheritParent\": false,\n" +
                "       \"values\": [\n" +
                "           {\n" +
                "               \"group\": {\n" +
                "                   \"type\": \"group\",\n" +
                "                   \"id\": \"fda5c295-230a-5025-9797-b8b4e99e08aa\"\n" +
                "               },\n" +
                "               \"types\": [\n" +
                "                   \"read\"\n" +
                "                   ],\n" +
                "               \"inherited\": false\n" +
                "           }\n" +
                "       ]\n" +
                "   }\n" +
                "}", sectionName.toLowerCase(Locale.ROOT), pageName, pageName.toLowerCase(Locale.ROOT));
        try {
            String response = new HttpClient()
                    .setUrl(standAndCompanyUrl + "/api/pages")
                    .setJsonBody(json)
                    .setProperty("Cookie", "vtoken=" + getAuthTokenAdmin())
                    .setProperty("Content-Type", "application/json")
                    .sendPost();
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            return jsonObject.get("__id").toString().replace("\"", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createSection(String name) {
        String json = String.format("{\n" +
                "    \"namespace\": \"global\",\n" +
                "    \"type\": \"NAMESPACE\",\n" +
                "    \"name\": \"%s\",\n" +
                "    \"code\": \"%s\",\n" +
                "    \"icon\": \"system_inbox\",\n" +
                "    \"data\": {\n" +
                "        \"columns\": 1,\n" +
                "        \"widgets\": [],\n" +
                "        \"menu\": {\n" +
                "            \"type\": \"namespace\",\n" +
                "            \"showName\": true\n" +
                "        }\n" +
                "    },\n" +
                "    \"__permissions\": {\n" +
                "        \"inheritParent\": false,\n" +
                "        \"values\": [\n" +
                "            {\n" +
                "                \"group\": {\n" +
                "                    \"type\": \"group\",\n" +
                "                    \"id\": \"fda5c295-230a-5025-9797-b8b4e99e08aa\"\n" +
                "                },\n" +
                "                \"types\": [\n" +
                "                    \"read\"\n" +
                "                ],\n" +
                "                \"inherited\": false\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}", name, name.toLowerCase(Locale.ROOT));
        try {
            new HttpClient()
                    .setUrl(standAndCompanyUrl + "/api/pages")
                    .setJsonBody(json)
                    .setProperty("Cookie", "vtoken=" + getAuthTokenAdmin())
                    .setProperty("Content-Type", "application/json")
                    .sendPost();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static String createElement(String name, String sectionName, String appName) {
        String json = String.format("{\n" +
                "    \"payload\": {\n" +
                "        \"__createdAt\": null,\n" +
                "        \"__createdBy\": null,\n" +
                "        \"__debug\": null,\n" +
                "        \"__deletedAt\": null,\n" +
                "        \"__directory\": null,\n" +
                "        \"__externalId\": null,\n" +
                "        \"__externalProcessMeta\": null,\n" +
                "        \"__id\": null,\n" +
                "        \"__index\": null,\n" +
                "        \"__name\": \"%s\",\n" +
                "        \"__tasks\": null,\n" +
                "        \"__tasks_earliest_duedate\": null,\n" +
                "        \"__tasks_performers\": null,\n" +
                "        \"__updatedAt\": null,\n" +
                "        \"__updatedBy\": null,\n" +
                "        \"__version\": 0\n" +
                "    }\n" +
                "}", name);
        try {
            String response = new HttpClient()
                    .setUrl(standAndCompanyUrl + "/api/apps/" + sectionName.toLowerCase() + "/" + appName.toLowerCase() + "/items")
                    .setJsonBody(json)
                    .setProperty("Cookie", "vtoken=" + getAuthTokenAdmin())
                    .setProperty("Content-Type", "application/json")
                    .sendPost();
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            return jsonObject.get("__id").toString().replace("\"", "");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Создает статусы в приложении
     *
     * @param section Секция приложения;
     * @param app Название приложения;
     * @param statusesNum Нужное количество статусов у приложения;
     */
    public static void createAppStatuses(String section, String app, int statusesNum) {
        StringBuilder statusObjects = new StringBuilder();
        //цикл для создания json объекта для статуса, они будут вставлены в массив items
        for (int i = 1; i <= statusesNum; i++) {
            String statusObject = String.format("{\n" +
                    "      \"id\": %s,\n" +
                    "      \"code\": \"%s\",\n" +
                    "      \"name\": \"%s\",\n" +
                    "      \"isFinished\": false,\n" +
                    "      \"jumpMethod\": \"NEXT\",\n" +
                    "      \"jumpToStatuses\": [],\n" +
                    "      \"hideFromBoard\": false,\n" +
                    "      \"isNegative\": false,\n" +
                    "      \"readonly\": false,\n" +
                    "      \"groupId\": \"00000000-0000-0000-0000-000000000000\",\n" +
                    "      \"backgroundColor\": \"\"\n" +
                    "    },", i, ("status" + i), ("status" + i));
            //если последняя итерация то обрезаем запятую чтобы json был правильный
            if (i == statusesNum)
                statusObject = statusObject.substring(0, statusObject.length() - 1);
            statusObjects.append(statusObject);
        }

        //в items подставляется statusObjects строка
        String json = String.format("{\n" +
                "    \"enabled\": true,\n" +
                "    \"manualChange\": false,\n" +
                "    \"nextId\": %s,\n" +
                "    \"items\": [%s],\n" +
                "    \"groups\": [\n" +
                "      {\n" +
                "        \"id\": \"00000000-0000-0000-0000-000000000000\",\n" +
                "        \"name\": \"\",\n" +
                "        \"code\": \"__default\",\n" +
                "        \"flow\": {\n" +
                "          \"type\": \"manual\",\n" +
                "          \"forwardOnly\": false\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"transitionLog\": false\n" +
                "  }", (statusesNum + 1), statusObjects);
        try {
            new HttpClient()
                    .setUrl(standAndCompanyUrl + "/api/apps/" + section.toLowerCase(Locale.ROOT) +
                            "/" + app.toLowerCase(Locale.ROOT) + "/settings/status")
                    .setJsonBody(json)
                    .setProperty("Cookie", "vtoken=" + getAuthTokenAdmin())
                    .setProperty("Content-Type", "application/json")
                    .sendPut();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setStatusFields(section, app);
    }

    /**
     * Отправляет кучу полей при создании статусов, нужен в догонку к методу createAppStatuses
     *
     * @param section Секция приложения;
     * @param app Название приложения;
     */
    private static void setStatusFields(String section, String app) {
        try {
            FileReader reader = new FileReader(new File(Constants.PATH_TO_DEFAULT_DIR, "testData/JsonProcess/setStatusFields.json"));
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            new HttpClient()
                    .setUrl(standAndCompanyUrl + "/api/apps/" + section.toLowerCase(Locale.ROOT) +
                            "/" + app.toLowerCase(Locale.ROOT) + "/fields")
                    .setJsonBody(String.valueOf(jsonArray))
                    .setProperty("Cookie", "vtoken=" + getAuthTokenAdmin())
                    .setProperty("Content-Type", "application/json")
                    .sendPut();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получает id элемента приложения по его имени(имена должны быть уникальны)
     *
     * @param sectionName Секция приложения;
     * @param appName Название приложения;
     * @param elementName Название элемента;
     */
    public static String getElementIdByName(String sectionName, String appName, String elementName) {
        String responseBody = null;
        try {
            responseBody = new HttpClient()
                    .setUrl(standAndCompanyUrl + "/api/apps/" + sectionName.toLowerCase(Locale.ROOT)
                            + "/" + appName.toLowerCase(Locale.ROOT)
                            + "/items?from=0&size=10&q=&withPerms=true&sortField=__createdAt&ascending=false&active=true")
                    .setProperty("Cookie", "vtoken=" + getAuthTokenAdmin())
                    .setProperty("Content-Type", "application/json")
                    .sendGet();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject allObject = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonArray userArray = allObject.get("result").getAsJsonArray();
        for (int i = 0; i < userArray.size(); i++) {
            JsonObject getUser = userArray.get(i).getAsJsonObject();
            String userEmail = getUser.get("__name").toString().replace("\"", "");
            if (userEmail.equals(elementName)) {
                return getUser.get("__id").toString().replace("\"", "");
            }
        }
        return null;
    }

    /**
     * Создание формы в приложении, находящемся внутри секции, по имени секции и приложения.
     * @param sectionName - имя секции
     * @param appName - имя приложения
     * @param formName - имя для создаваемой формы
     * @return поле __name из ответа сервера.
     */
    public static String createFormInApplicationByName(String sectionName, String appName, String formName) {
        String json = String.format("{\n" +
                "    \"namespace\": \"%1$s.%2$s\",\n" +
                "    \"code\": \"%3$s\",\n" +
                "    \"__name\": \"%4$s\",\n" +
                "    \"hidden\": false,\n" +
                "    \"draft\": false,\n" +
                "    \"version\": 1,\n" +
                "    \"dataNamespace\": \"%1$s\",\n" +
                "    \"dataCode\": \"%2$s\",\n" +
                "    \"descriptor\": {\n" +
                "        \"code\": \"%1$s.%2$s@%3$s\",\n" +
                "        \"template\": {\n" +
                "            \"descriptor\": \"item-form-complex-popup\",\n" +
                "            \"values\": {\n" +
                "                \"formGroup\": {\n" +
                "                    \"path\": [\n" +
                "                        \"item\"\n" +
                "                    ]\n" +
                "                }\n" +
                "            },\n" +
                "            \"content\": {\n" +
                "                \"[content]\": [\n" +
                "                    {\n" +
                "                        \"id\": \"c18e9118-cda2-49f4-ae50-5414c9b970b2\",\n" +
                "                        \"descriptor\": \"modal-body\",\n" +
                "                        \"values\": {\n" +
                "                            \"shadow\": true\n" +
                "                        },\n" +
                "                        \"content\": {\n" +
                "                            \"\": [\n" +
                "                                {\n" +
                "                                    \"id\": \"b9dbfcad-d5e2-49d0-bfc9-0e817c7a30e6\",\n" +
                "                                    \"descriptor\": \"dynamic-form\",\n" +
                "                                    \"values\": {\n" +
                "                                        \"form\": {\n" +
                "                                            \"path\": [\n" +
                "                                                \"item\"\n" +
                "                                            ]\n" +
                "                                        },\n" +
                "                                        \"fields\": {\n" +
                "                                            \"path\": [\n" +
                "                                                \"fields\"\n" +
                "                                            ]\n" +
                "                                        }\n" +
                "                                    },\n" +
                "                                    \"content\": {},\n" +
                "                                    \"fixed\": false\n" +
                "                                }\n" +
                "                            ]\n" +
                "                        },\n" +
                "                        \"fixed\": false\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"[footer]\": [\n" +
                "                    {\n" +
                "                        \"descriptor\": \"appview-process-buttons\",\n" +
                "                        \"values\": {\n" +
                "                            \"application\": {\n" +
                "                                \"path\": [\n" +
                "                                    \"application\"\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            \"isLocked\": {\n" +
                "                                \"path\": [\n" +
                "                                    \"isLocked\"\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            \"item\": {\n" +
                "                                \"path\": [\n" +
                "                                    \"itemModel\"\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            \"settings\": {\n" +
                "                                \"path\": [\n" +
                "                                    \"buttonSettings\"\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            \"runProcessAction\": {\n" +
                "                                \"path\": [\n" +
                "                                    \"runProcessAction\"\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            \"permissions\": {\n" +
                "                                \"path\": [\n" +
                "                                    \"permissions\"\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            \"useDefaultButtons\": {\n" +
                "                                \"path\": [\n" +
                "                                    \"useDefaultButtons\"\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            \"bpTemplateBuilder\": {\n" +
                "                                \"path\": [\n" +
                "                                    \"bpTemplateBuilder\"\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        },\n" +
                "                        \"content\": {},\n" +
                "                        \"fixed\": false\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"[sidebar]\": [\n" +
                "                    {\n" +
                "                        \"id\": \"662f08da-59da-4112-9ae1-763785ab8bf2\",\n" +
                "                        \"descriptor\": \"sidebar-widget\",\n" +
                "                        \"values\": {},\n" +
                "                        \"content\": {\n" +
                "                            \"\": [\n" +
                "                                {\n" +
                "                                    \"id\": \"1556d7a7-eaa5-41a2-b262-82ec3b86e64d\",\n" +
                "                                    \"descriptor\": \"user-guide\",\n" +
                "                                    \"values\": {},\n" +
                "                                    \"content\": {},\n" +
                "                                    \"fixed\": false\n" +
                "                                }\n" +
                "                            ]\n" +
                "                        },\n" +
                "                        \"fixed\": false\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"[headerControls]\": [],\n" +
                "                \"[headerCustomization]\": []\n" +
                "            },\n" +
                "            \"fixed\": false,\n" +
                "            \"descriptorVersion\": 2\n" +
                "        },\n" +
                "        \"fields\": [],\n" +
                "        \"types\": [\n" +
                "            \"form\"\n" +
                "        ],\n" +
                "        \"dataFieldCode\": \"item\"\n" +
                "    }\n" +
                "}",sectionName.toLowerCase(Locale.ROOT), appName.toLowerCase(Locale.ROOT), formName.toLowerCase(Locale.ROOT), formName);
        try {
            String response = new HttpClient()
                    .setUrl(standAndCompanyUrl + "api/widgets/create")
                    .setJsonBody(json)
                    .setProperty("Cookie", "vtoken=" + getAuthTokenAdmin())
                    .setProperty("Content-Type", "application/json")
                    .sendPost();
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            return jsonObject.get("__id").toString().replace("\"", "");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
