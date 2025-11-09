package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;
import resources.TestDataBuild;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class StepDefinition {

    RequestSpecification postReq;
    RequestSpecification reqSpec;
    ResponseSpecification resSpec;
    Response response;
    TestDataBuild data = new TestDataBuild();

    String postEndPoint = "/maps/api/place/add/json";
    String getEndPoint = "/maps/api/place/get/json";

    @Given("Add Place Payload")
    public void add_place_payload() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";


        postReq = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key", "qaclick123")
                .setContentType(ContentType.JSON)
                .build();

        reqSpec = given()
                .spec(postReq)
                .body(data.addPlacePayload());

        resSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();


    }
    @When("user calls {string} with Post http request")
    public void user_calls_with_post_http_request(String string) {
        response = reqSpec
                .when()
                .post(postEndPoint)
                .then()
                .spec(resSpec)
                .log().all()
                .extract().response();
    }
    @Then("the API call is success with status code {int}")
    public void the_api_call_is_success_with_status_code(Integer int1) {
        assertEquals(response.getStatusCode(), 200);
    }
    @Then("{string} in response body is {string}")
    public void in_response_body_is(String key, String expectedValue) {
        String responseString = response.asString();
        JsonPath js = new JsonPath(responseString);
        assertEquals(js.get(key), expectedValue);
    }
}
