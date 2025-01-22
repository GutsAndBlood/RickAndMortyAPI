package backend;

import baseAPI.TestBase;
import io.restassured.response.Response;
import listener.TestListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Listeners(TestListener.class)
public class TestNGLocationEndpoint extends TestBase {

    protected int CurrentCharactersCount = 126;
    protected int actualCharactersCount;
    protected int actualPagesCount;
    protected String characterLink = "https://rickandmortyapi.com/api/location?page=";


    @Test
    public void tc001_validateLocationEndpointHeaderResponse() {
        Response response = locationAPI.getAllLocations();

        response.then()
                .statusCode(200)
                .header("Access-Control-Allow-Origin", equalTo("*"))
                .header("Content-Type", equalTo("application/json; charset=utf-8"));
    }

    @Test
    public void tc002_validateLocationEndpointBodyParametersFirstCall() {
        Response response = locationAPI.getAllLocations();

        response.then()
            .statusCode(200)
            .body("info.count", notNullValue())
            .body("info.pages", notNullValue())
            .body("info.next", notNullValue())
            .body("info.prev", nullValue())

            // results
            .body("results[0].id", notNullValue())
            .body("results[0].name", notNullValue())
            .body("results[0].type", notNullValue())
            .body("results[0].dimension", notNullValue())
            .body("results[0].residents", notNullValue())
            .body("results[0].url", notNullValue())
            .body("results[0].created", notNullValue());
    }

    @Test
    public void tc003_validateLocationEndpointInfoParametersFirstCall() {
        Response response = locationAPI.getAllLocations();

        actualCharactersCount = response.path("info.count");
        actualPagesCount = response.path("info.pages");
        String nextLink = response.path("info.next");
        String prevLink = response.path("info.prev");

        assertEquals(actualCharactersCount, CurrentCharactersCount, "Character Count dont Match with 126");
        assertEquals(actualPagesCount, 7, "Character Page dont Match with 42");
        assertEquals(nextLink, characterLink+2, "Next Link dont Match with expected Value");
        assertNull(prevLink, "Prev Link dont Match with expected Value");
    }

    @Test
    public void tc004_validateLocationEndpointInfoParametersSecondCall() {
        Response response = locationAPI.getAllLocations("?page=2");

        actualCharactersCount = response.path("info.count");
        actualPagesCount = response.path("info.pages");
        String nextLink = response.path("info.next");
        String prevLink = response.path("info.prev");

        assertEquals(actualCharactersCount, CurrentCharactersCount, "Character Count dont Match with 126");
        assertEquals(actualPagesCount, 7, "Character Page dont Match with 42");
        assertEquals(nextLink, characterLink+3, "Next Link dont Match with expected Value");
        assertEquals(prevLink, characterLink+1, "Prev Link dont Match with expected Value");
    }

    @Test
    public void tc010_validateLocationEndpointResultsParametersFirstUsername() {
        Response response = locationAPI.getAllLocations("?page=1");

        response.then()
                .statusCode(200)
                .body("results[0].id", equalTo(1))
                .body("results[0].name", equalTo("Earth (C-137)"))
                .body("results[0].type", equalTo("Planet"))
                .body("results[0].dimension", equalTo("Dimension C-137"));
    }

    @Test
    public void tc011_validateLocationEndpointResultsErrorNumberOfPages() {
        Response response = locationAPI.getAllLocations("?page=29");

        response.then()
                .statusCode(404)
                .body("error", equalTo("There is nothing here"));
    }

    @Test
    public void tc020_validateLocationEndpointFilterByID() {
        Response response = locationAPI.getLocationById(3);

        response.then()
                .statusCode(200)
                .body("id", equalTo(3))
                .body("name", equalTo("Citadel of Ricks"))
                .body("type", equalTo("Space station"))
                .body("dimension", equalTo("unknown"))
                .body("residents[0]", equalTo("https://rickandmortyapi.com/api/character/8"))
                .body("url", equalTo("https://rickandmortyapi.com/api/location/3"));
    }

    @Test
    public void tc021_validateLocationEndpointErrorFilteringByID() {
        Response response = locationAPI.getLocationById(139);

        response.then()
                .statusCode(404)
                .body("error", equalTo("Location not found"));
    }

    @Test
    public void tc030_validateLocationEndpointFilterByMultipleIDs() {
        Response response = locationAPI.getMultipleLocations("55,101");

        response.then()
                .statusCode(200)
                .body("[0].id", equalTo(55))
                .body("[0].name", equalTo("Pawn Shop Planet"))
                .body("[0].type", equalTo("Planet"))
                .body("[0].dimension", equalTo("Replacement Dimension"))
                .body("[0].residents[0]", equalTo("https://rickandmortyapi.com/api/character/258"))
                .body("[0].url", equalTo("https://rickandmortyapi.com/api/location/55"))
                //second ID
                .body("[1].id", equalTo(101))
                .body("[1].name", equalTo("Glorzo Asteroid"))
                .body("[1].type", equalTo("Asteroid"))
                .body("[1].dimension", equalTo("Replacement Dimension"))
                .body("[1].residents[0]", equalTo("https://rickandmortyapi.com/api/character/640"))
                .body("[1].url", equalTo("https://rickandmortyapi.com/api/location/101"))
                //validate no more Values are populated
                .body("[2]", nullValue());
    }

    @Test
    public void tc031_validateLocationEndpointFilterByMultipleIDsFiveValues() {
        Response response = locationAPI.getMultipleLocations("1,22,29,42,55");

        response.then()
                .statusCode(200);

        int totalCount = response.path("results.size()");
        assertEquals(totalCount, 5, "We are expecting 5 Locations but got " + totalCount);
    }

    @Test
    public void tc032_validateLocationEndpointFilterByMultipleIDsErrorOnFirstID() {
        Response response = locationAPI.getMultipleLocations("131,101");

        response.then()
                .statusCode(200)
                .body("[0].id", equalTo(101))
                .body("[0].name", equalTo("Glorzo Asteroid"))
                .body("[0].type", equalTo("Asteroid"))
                .body("[0].dimension", equalTo("Replacement Dimension"))
                .body("[0].residents[0]", equalTo("https://rickandmortyapi.com/api/character/640"))
                .body("[0].url", equalTo("https://rickandmortyapi.com/api/location/101"))
                //validate no more Values are populated
                .body("[1]", nullValue());
    }

    @Test
    public void tc033_validateLocationEndpointFilterByMultipleIDsErrorOnSecondID() {
        Response response = locationAPI.getMultipleLocations("22,333");

        response.then()
                .statusCode(200)
                .body("[0].id", equalTo(22))
                .body("[0].name", equalTo("Signus 5 Expanse"))
                .body("[0].type", equalTo("unknown"))
                .body("[0].dimension", equalTo("Cromulon Dimension"))
                .body("[0].residents[0]", equalTo("https://rickandmortyapi.com/api/character/24"))
                .body("[0].url", equalTo("https://rickandmortyapi.com/api/location/22"))
                //validate no more Values are populated
                .body("[1]", nullValue());
    }

    @Test
    public void tc040_validateLocationEndpointFilterByQueryParams() {
        Response response = locationAPI.FilterByQueryParams("name", "Abadango");

        int id = response.path("results[0].id");
        assertEquals(id, 2, "We are expecting 1 Abadango");
    }

    @Test
    public void tc041_validateLocationEndpointErrorFilteringByQueryParams() {
        Response response = locationAPI.FilterByQueryParams("name", "Abacdango");

        response.then()
                .statusCode(404)
                .body("error", equalTo("There is nothing here"));
    }

    @Test
    public void tc050_validateLocationEndpointFilterByMultipleQueryParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "Worldender's lair");
        map.put("type", "Planet");
        Response response = characterAPI.FilterByMultipleQueryParams(map, "location");

        int infoCount = response.path("info.count");
        int resultCount = response.path("results.size()");
        assertEquals(infoCount, resultCount, "Information from Info, and Results should match");
    }

    @Test
    public void tc051_validateLocationEndpointErrorFilteringByMultipleQueryParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "Worldender's lair");
        map.put("type", "Asteroid");
        Response response = characterAPI.FilterByMultipleQueryParams(map, "location");

        response.then()
                .statusCode(404)
                .body("error", equalTo("There is nothing here"));
    }

}
