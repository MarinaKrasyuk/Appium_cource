

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Execution(ExecutionMode.CONCURRENT)
public class IOSBaseTest {

    public IOSDriver<IOSElement> driver;
    public String username;
    public String accessKey;
    public DesiredCapabilities capabilities;

    public static JSONObject config;
    private static JSONParser parser = new JSONParser();


    private static <JSONParser> Stream<Integer> devices() throws IOException, ParseException {

        List<Integer> taskIDs = new ArrayList<Integer>();

        config = (JSONObject) parser.parse(new FileReader("src/test/resources/first.conf.json"));
        int envs = ((JSONArray) config.get("environments")).size();

        for (int i = 0; i < envs; i++) {
            taskIDs.add(i);
        }

        return taskIDs.stream();
    }

    public void createConnection(int taskId) throws MalformedURLException {
        JSONArray envs = (JSONArray) config.get("environments");

        Map<String, String> envCapabilities = (Map<String, String>) envs.get(taskId);
        Iterator it = envCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
        }

        driver = new IOSDriver(new URL("http://" + username + ":" + accessKey + "@" + config.get("server") + "/wd/hub"), capabilities);
    }

    @BeforeEach
    public void setup() {
        capabilities = new DesiredCapabilities();

        Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
        Iterator it = commonCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (capabilities.getCapability(pair.getKey().toString()) == null) {
                capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }
        }

        username = System.getenv("BROWSERSTACK_USERNAME");
        if (username == null) {
            username = (String) config.get("username");
        }

        accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if (accessKey == null) {
            accessKey = (String) config.get("access_key");
        }

        String app = System.getenv("BROWSERSTACK_APP_ID");
        if (app != null && !app.isEmpty()) {
            capabilities.setCapability("app", app);
        }
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
