
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static filters.CustomLogFilter.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;


public class ShopTests {

    private static Cookies cookies;

    @Test
    @Tag("demowebshop")
//    @Disabled("Example test code for further test development")
    @DisplayName("Successful authorization to some demowebshop , adding two products and chek basket")
    void addToCartAndCheckBasket() {
        step("Get cookie by api and set it to browser", () -> {
            String body = String.format("//json");
            cookies = given()
                    .filter(customLogFilter().withCustomTemplates())
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .formParam("Email", "qaguru@qa.guru")
                    .formParam("Password", "qaguru@qa.guru1")
                    .when()
                    .post("http://demowebshop.tricentis.com/login")
                    .then()
                    .statusCode(302)
                    .extract()
                    .response().getDetailedCookies();
        });
        step("Add new product to basket", () -> {
            given()
                    .filter(customLogFilter().withCustomTemplates())
                    .when()
                    .cookie(cookies.toString())
                    .post("http://demowebshop.tricentis.com/addproducttocart/catalog/31/1/1")
                    .then()
                    .statusCode(200)
                    .body("success", is(true),
                            "updatetopcartsectionhtml", is("(1)"));
        });

        step("Add similar product to basket", () -> {
            given()
                    .filter(customLogFilter().withCustomTemplates())
                    .when()
                    .cookie(cookies.toString())
                    .post("http://demowebshop.tricentis.com/addproducttocart/catalog/31/1/1")
                    .then()
                    .statusCode(200)
                    .body("success", is(true),
                            "updatetopcartsectionhtml", is("(2)"));
        });

        step("Check cart", () -> {
            Response response = given()
                    .filter(customLogFilter().withCustomTemplates())
                    .when()
                    .cookie(cookies.toString())
                    .get("http://demowebshop.tricentis.com/cart")
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();
            String bodyTxt = response.htmlPath().getString("body");
            System.out.println(bodyTxt);
            Assertions.assertTrue(bodyTxt.contains("cart(2)"), "Not contains");
        });
    }
}

