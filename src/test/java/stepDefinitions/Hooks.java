package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;

import java.io.IOException;

public class Hooks {

    @Before("@DeletePlace")
    public void beforeScenario() throws IOException {

        StepDefinition s = new StepDefinition();

        if(StepDefinition.placeID == null) {
            s.add_place_payload_with("Aya", "English", "Europe");
            s.user_calls_with_http_request("AddPlaceAPI", "Post");
            s.verify_place_id_created_that_maps_to_using("Aya", "GetPlaceAPI");
        }
    }

}
