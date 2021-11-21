import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    protected static RequestSpecification requestSpec;
    protected final static String BASEURL = "https://reqres.in";

    @BeforeAll
    static void setUp() {
        requestSpec = new RequestSpecBuilder().setBaseUri(BASEURL).setContentType(ContentType.JSON).addFilter(new AllureRestAssured()).build();
    }

    protected static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectContentType(ContentType.JSON)
            .build();

    protected Response createUser() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "morpheus");
        requestParams.put("job", "zion resident");
        return RestAssured.given(requestSpec).filter(new AllureRestAssured()).contentType(ContentType.JSON).body(requestParams.toString())
                .post("/api/users");
    }

}
