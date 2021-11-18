import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RequestReqresTests extends BaseTest {


    @Test
    public void getListUsers() {

        specification.pathParam("userId", 2);
        Response response = given(specification)
                .get("/api/users/{userId}")
                .then()
                .statusCode(200)
                .contentType(JSON).extract().response();
        assertEquals(response.path("data.id").toString(), "2");
        assertEquals(response.path("data.email"), "janet.weaver@reqres.in");
        assertEquals(response.path("data.first_name"), "Janet");
        assertEquals(response.path("data.last_name"), "Weaver");
        assertEquals(response.path("data.avatar"), "https://reqres.in/img/faces/2-image.jpg");
        assertEquals(response.path("support.url"), "https://reqres.in/#support-heading");
        assertEquals(response.path("support.text"), "To keep ReqRes free, contributions towards server costs are appreciated!");
        response.print();

    }

    @Test
    public void postCreateUser() {

        setUp();
        Response response = createUser();
        response.then()
                .contentType(JSON)
                .statusCode(201)
                .body("name", is("morpheus")
                        , "job", is("zion resident")
                        , "id", is(notNullValue())
                        , "createdAt", is(notNullValue()));
        response.print();
    }

    @Test
    public void putUpdateUser() {

        JSONObject body = new JSONObject();
        body.put("name", "morpheus");
        body.put("job", "zion resident");

        Response responseUpdate = RestAssured.given(specification)
                .contentType(JSON)
                .body(body.toString())
                .queryParam("userId", 2)
                .put("/api/users/{userId}")
                .then().statusCode(200).extract()
                .response();

        assertEquals(responseUpdate.path("name"), "morpheus");
        assertEquals(responseUpdate.path("job"), "zion resident");
        Assertions.assertNotNull(responseUpdate.path("updatedAt"));
        responseUpdate.print();

    }

    @Test
    void registerUnsuccessful() {
        given()
                .contentType(JSON)
                .body("{\"email\": \"sydney@fife\"}")
                .when()
                .post(("https://reqres.in/api/register"))
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    public void deleteUser() {
        given()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204);
    }
}
