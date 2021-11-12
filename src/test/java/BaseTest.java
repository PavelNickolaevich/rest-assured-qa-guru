import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    protected static RequestSpecification specification;

    @BeforeAll
    static void setUp() {
        specification = new RequestSpecBuilder().setBaseUri("https://reqres.in").build();

    }

    protected Response createUser() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "morpheus");
        requestParams.put("job", "zion resident");
        return RestAssured.given(specification).contentType(ContentType.JSON).body(requestParams.toString())
                .post("/api/users");

    }
//
//    protected  createUser() {
//        JSONObject requestParams = new JSONObject();
//        requestParams.put("name", "morpheus");
//        requestParams.put("job", "zion resident");
//        return RestAssured.given(specification).contentType(ContentType.JSON).body(requestParams.toString())
//                .post("/api/users");


}
