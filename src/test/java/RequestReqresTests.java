import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import objects.AllData;
import objects.User;
import objects.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


public class RequestReqresTests extends BaseTest {


    @Test
    public void getSingleUsers() {

        requestSpec.pathParam("userId", 2);
        AllData dataUser = given(requestSpec)
                .get("/api/users/{userId}")
                .then()
                .spec(responseSpec)
                .log().body()
                .extract()
                .as(AllData.class);

        assertEquals(2, dataUser.getData().getId());
        assertEquals("janet.weaver@reqres.in", dataUser.getData().getEmail());
        assertEquals("Janet", dataUser.getData().getFirstName());
        assertEquals("Weaver", dataUser.getData().getLastName());
        assertEquals("https://reqres.in/img/faces/2-image.jpg", dataUser.getData().getAvatar());
        assertEquals("https://reqres.in/#support-heading", dataUser.getSupport().getUrl());
        assertEquals("To keep ReqRes free, contributions towards server costs are appreciated!", dataUser.getSupport().getText());
    }


    @Test
    @Tag("groovy")
    public void getListUsersWithGroovy() {
        given(requestSpec)
                .param("page", 2)
                .get("/api/users")
                .then()
                .spec(responseSpec)
                .log().body().
                body(
                        "page", is(2),
                        "data.size()", is(6),
                        "data.findAll { it.avatar.startsWith('https://reqres.in/img/faces/') }.size()", is(6),
                        "data.findAll { it.email =~/.*?@reqres.in/ }.email.flatten()", hasItems("michael.lawson@reqres.in", "rachel.howell@reqres.in")
                );
    }

    @Test
    public void postCreateUser() {

        setUp();

        Response response = createUser();
        User user = response.then()
                .contentType(JSON)
                .statusCode(201)
                .log().body()
                .extract()
                .as(User.class);

        assertEquals("morpheus", user.getName());
        assertEquals("zion resident", user.getJob());
        assertTrue(user.getId() > 0);
        assertTrue(user.getTimeCreated().getTime() > 0);
    }


    @Test
    public void putUpdateUser() {

        JSONObject body = new JSONObject();
        body.put("name", "morpheus");
        body.put("job", "zion resident");

        User user = RestAssured.given(requestSpec)
                .body(body.toString())
                .pathParam("userId", 2)
                .put("/api/users/{userId}")
                .then()
                .spec(responseSpec)
                .log().body()
                .extract().as(User.class);

        assertEquals("morpheus", user.getName());
        assertEquals("zion resident", user.getJob());
        Assertions.assertNotNull(user.getTimeUpdated());

    }

    @Test
    void registerUnsuccessful() {

        setUp();

        JSONObject body = new JSONObject();
        body.put("email", "sydney@fife");

        Response response = given(requestSpec)
                .body(body.toString())
                .post("/api/register")
                .then()
                .statusCode(400)
                .log().body().extract().response();

        assertTrue(response.path("error").equals("Missing password"));

        //.body("error", hasItem("Missing password"));
        // .body("error", is("Missing password"));
//        body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
//                hasItem("eve.holt@reqres.in"));
    }

    @Test
    public void deleteUser() {
        requestSpec.pathParam("userId", 2);
        given(requestSpec)
                .filter(new AllureRestAssured())
                .when()
                .delete("/api/users/{userId}")
                .then()
                .statusCode(204);
    }
}
