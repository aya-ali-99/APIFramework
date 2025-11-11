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
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class StepDefinition extends Utils {

    RequestSpecification reqSpec;
    Response response;
    TestDataBuild data = new TestDataBuild();
    static String placeID;


    @Given("Add Place Payload with {string} {string} {string}")
    public void add_place_payload_with(String name, String language, String address) throws IOException {

        reqSpec = given()
                .spec(requestSpecification())
                .body(data.addPlacePayload(name, language, address));

    }
    @When("user calls {string} with {string} http request")
    public void user_calls_with_http_request(String resource, String httpMethod) {
        APIResources resourceAPI = APIResources.valueOf(resource);

        if (httpMethod.equalsIgnoreCase("Post")) {
            response = reqSpec
                    .when()
                    .post(resourceAPI.getResource());
        }
        else if (httpMethod.equalsIgnoreCase("Get")) {
            response = reqSpec
                    .when()
                    .get(resourceAPI.getResource());
        }
        else if (httpMethod.equalsIgnoreCase("Delete")) {
            response = reqSpec
                    .when()
                    .delete(resourceAPI.getResource());
        }

    }

    @Then("the API call is success with status code {int}")
    public void the_api_call_is_success_with_status_code(Integer int1) {
        assertEquals(200, response.getStatusCode());
    }
    @Then("{string} in response body is {string}")
    public void in_response_body_is(String key, String expectedValue) {

        assertEquals(expectedValue, getJsonPath(response, key));
    }

    @Then("verify place_id created that maps to {string} using {string}")
    public void verify_place_id_created_that_maps_to_using(String expectedName, String resource) throws IOException {

        placeID = getJsonPath(response, "place_id");
        reqSpec = given()
                .spec(requestSpecification())
                .queryParam("place_id", placeID);

        user_calls_with_http_request(resource, "Get");
        String actualPlaceName = getJsonPath(response, "name");
        assertEquals(expectedName, actualPlaceName);
    }

    @Given("DeletePlace Payload")
    public void delete_place_payload() throws IOException {
        reqSpec = given()
                .spec(requestSpecification())
                .body(data.deletePlacePayload(placeID));
    }
}
