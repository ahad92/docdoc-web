package environment;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;
import static environment.Helpers.randomNumber;
import static environment.TestBase.*;

public class ApiHelpers {

    private static String apiToken;
    private static ArrayList<String> apiTokens = new ArrayList<String>();


    public static void setApiToken() {
        apiToken = patientRegisterAndGetApiToken();
    }

    public static String getApiToken() {
        return apiToken;
    }

    public static void setApiTokens(int count) {
        for (int i = 0; i < count; i++) {
            apiTokens.add(patientRegisterAndGetApiToken());
        }
    }

    public static String patientRegisterAndGetApiToken() {
        JSONObject requestBody = new JSONObject();
        Faker faker = new Faker(new Locale("ru"));
        String name = faker.name().fullName();

        requestBody.put("full_name", name);
        requestBody.put("login", "+7900" + randomNumber(7));
        requestBody.put("password", "qwerty");
        requestBody.put("password_confirmation", "qwerty");
        requestBody.put("clinic_id", 1);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());
        Response response = request.post(API_URL + "/api/v3/docdoc/patient/register");
        return response.jsonPath().getString("api_token");
    }

}
