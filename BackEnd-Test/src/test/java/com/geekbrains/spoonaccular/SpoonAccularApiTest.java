package com.geekbrains.spoonaccular;

import com.geekbrains.BaseTest;
import io.restassured.RestAssured;
import net.javacrumbs.jsonunit.JsonAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class SpoonAccularApiTest extends BaseTest {

    private static final String API_KEY = "a55230883b224de4b7840764146d0f9e";
    private static final String BASE_URL = "https://api.spoonacular.com";

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    void testGetComplexSearch() throws IOException {

        String actually = RestAssured.given()
                .queryParam("apiKey", API_KEY)
                .queryParam("query", "pasta")
                .queryParam("cuisine", "italian")
                .log()
                .uri()
                .expect()
                .log()
                .all()
                .statusCode(200)
                .time(lessThan(2500L))
                .body("results[0].id", Matchers.notNullValue())
                .body("offset", is(0))
                .body("number", is(10))
                .body("totalResults", is(127))
                .when()
                .get("recipes/complexSearch")
                .body()
                .asPrettyString();

        //System.out.println(actually);
        String expected = getResourceAsString("expected.json");

        JsonAssert.assertJsonEquals(
                expected,
                actually,
                JsonAssert.when(IGNORING_ARRAY_ORDER)
        );
    }

        @Test

        void testPostClassifyGroceryProduct() throws IOException {

            String actuallyPost = given()
                    .queryParam("apiKey", API_KEY)
                    .body("title": "Kroger Vitamin A & D Reduced Fat 2% Milk", "upc": "", "plu_code": "" )
                    .log()
                    .uri()
                    .expect()
                    .log()
                    .all()
                    .statusCode(200)
                    .time(lessThan(2500L))
                    .when()
                    .post("/food/products/classify")
                    .body()
                    .asPrettyString();
    }


    String id = "183433";
    @Test
    void addMealTest() {
        id = given()
                .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
                .queryParam("apiKey", API_KEY)
                .body("{\n"
                        + " \"date\": 1644881179,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"1 banana\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/mealplanner/geekbrains/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }

    @AfterEach
    void tearDown() {
        given()
                .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
                .queryParam("apiKey", API_KEY)
                .delete("https://api.spoonacular.com/mealplanner/geekbrains/items/" + id)
                .then()
                .statusCode(200);
    }

}


