import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    protected static RequestSpecification specification;
    private final static String BASEURL = ("https://reqres.in");

    @BeforeAll
    static void setUp() {
        specification = new RequestSpecBuilder().setBaseUri(BASEURL).build();

    }

    protected Response createUser() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "morpheus");
        requestParams.put("job", "zion resident");
        return RestAssured.given(specification).filter(new AllureRestAssured()).contentType(ContentType.JSON).body(requestParams.toString())
                .post("/api/users");
    }

}
