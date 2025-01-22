package backend;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import baseAPI.TestBase;
import java.util.HashMap;
import listener.TestListener;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Listeners(TestListener.class)
public class TestNGCharacterEndpoint extends TestBase {

    protected int CurrentCharactersCount = 826;
    protected int actualCharactersCount;
    protected int actualPagesCount;
    protected String characterLink = "https://rickandmortyapi.com/api/character?page=";
    private static final Logger logger = LoggerFactory.getLogger(TestNGCharacterEndpoint.class);

    @Test
    public void tc001_validateCharacterEndpointHeaderResponse() {
        Response response = characterAPI.getAllCharacters();

        response.then()
                .statusCode(200)
                .header("Access-Control-Allow-Origin", equalTo("*"))
                .header("Content-Type", equalTo("application/json; charset=utf-8"));

        logger.info("Logger to show 'good practices' for debugging purposes");
    }

    @Test
    public void tc002_validateCharacterEndpointBodyParametersFirstCall() {
        Response response = characterAPI.getAllCharacters();

        response.then()
            .statusCode(200)
            .body("info.count", notNullValue())
            .body("info.pages", notNullValue())
            .body("info.next", notNullValue())
            .body("info.prev", nullValue())

            // results
            .body("results[0].id", notNullValue())
            .body("results[0].name", notNullValue())
            .body("results[0].status", notNullValue())
            .body("results[0].species", notNullValue())
            .body("results[0].type", notNullValue())
            .body("results[0].gender", notNullValue())
            .body("results[0].origin.name", notNullValue())
            .body("results[0].origin.url", notNullValue())
            .body("results[0].location.name", notNullValue())
            .body("results[0].location.url", notNullValue())
            .body("results[0].image", notNullValue())
            .body("results[0].episode", notNullValue())
            .body("results[0].url", notNullValue())
            .body("results[0].created", notNullValue());
    }

    @Test
    public void tc003_validateCharacterEndpointInfoParametersFirstCall() {
        Response response = characterAPI.getAllCharacters();

        actualCharactersCount = response.path("info.count");
        actualPagesCount = response.path("info.pages");
        String nextLink = response.path("info.next");
        String prevLink = response.path("info.prev");

        assertEquals(actualCharactersCount, CurrentCharactersCount, "Character Count dont Match with 826");
        assertEquals(actualPagesCount, 42, "Character Page dont Match with 42");
        assertEquals(nextLink, characterLink+2, "Next Link dont Match with expected Value");
        assertNull(prevLink, "Prev Link dont Match with expected Value");
    }

    @Test
    public void tc004_validateCharacterEndpointInfoParametersSecondCall() {
        Response response = characterAPI.getAllCharacters("?page=2");

        actualCharactersCount = response.path("info.count");
        actualPagesCount = response.path("info.pages");
        String nextLink = response.path("info.next");
        String prevLink = response.path("info.prev");

        assertEquals(actualCharactersCount, CurrentCharactersCount, "Character Count dont Match with 826");
        assertEquals(actualPagesCount, 42, "Character Page dont Match with 42");
        assertEquals(nextLink, characterLink+3, "Next Link dont Match with expected Value");
        assertEquals(prevLink, characterLink+1, "Prev Link dont Match with expected Value");
    }

    @Test
    public void tc010_validateCharacterEndpointResultsParametersFirstUsername() {
        Response response = characterAPI.getAllCharacters("?page=1");

        response.then()
                .statusCode(200)
                .body("results[0].id", equalTo(1))
                .body("results[0].name", equalTo("Rick Sanchez"))
                .body("results[0].status", equalTo("Alive"))
                .body("results[0].species", equalTo("Human"))
                .body("results[0].gender", equalTo("Male"))
                .body("results[0].origin.name", equalTo("Earth (C-137)"));
    }

    @Test
    public void tc011_validateCharacterEndpointResultsErrorNumberOfPages() {
        Response response = characterAPI.getAllCharacters("?page=55");

        response.then()
                .statusCode(404)
                .body("error", equalTo("There is nothing here"));
    }

    @Test
    public void tc020_validateCharacterEndpointFilterByID() {
        Response response = characterAPI.getCharacterById(2);

        response.then()
                .statusCode(200)
                .body("id", equalTo(2))
                .body("name", equalTo("Morty Smith"))
                .body("status", equalTo("Alive"))
                .body("species", equalTo("Human"))
                .body("gender", equalTo("Male"))
                .body("origin.name", equalTo("unknown"));
    }

    @Test
    public void tc021_validateCharacterEndpointErrorFilteringByID() {
        Response response = characterAPI.getCharacterById(966);

        response.then()
                .statusCode(404)
                .body("error", equalTo("Character not found"));
    }

    @Test
    public void tc030_validateCharacterEndpointFilterByMultipleIDs() {
        Response response = characterAPI.getMultipleCharacters("200,369");

        response.then()
                .statusCode(200)
                .body("[0].id", equalTo(200))
                .body("[0].name", equalTo("Lawyer Morty"))
                .body("[0].status", equalTo("unknown"))
                .body("[0].species", equalTo("Human"))
                .body("[0].gender", equalTo("Male"))
                .body("[0].origin.name", equalTo("unknown"))
                //second ID
                .body("[1].id", equalTo(369))
                .body("[1].name", equalTo("Tusked Assassin"))
                .body("[1].status", equalTo("unknown"))
                .body("[1].species", equalTo("Alien"))
                .body("[1].type", equalTo("Tuskfish"))
                .body("[1].gender", equalTo("Male"))
                .body("[1].origin.name", equalTo("Resort Planet"))
                //validate no more Values are populated
                .body("[2]", nullValue());
    }

    @Test
    public void tc031_validateCharacterEndpointFilterByMultipleIDsFiveValues() {
        Response response = characterAPI.getMultipleCharacters("200,369,23,42,55");

        response.then()
                .statusCode(200);

        int totalCount = response.path("results.size()");
        assertEquals(totalCount, 5, "We are expecting 5 Characters but got " + totalCount);
    }

    @Test
    public void tc032_validateCharacterEndpointFilterByMultipleIDsErrorOnFirstID() {
        Response response = characterAPI.getMultipleCharacters("922,369");

        response.then()
                .statusCode(200)
                .body("[0].id", equalTo(369))
                .body("[0].name", equalTo("Tusked Assassin"))
                .body("[0].status", equalTo("unknown"))
                .body("[0].species", equalTo("Alien"))
                .body("[0].type", equalTo("Tuskfish"))
                .body("[0].gender", equalTo("Male"))
                .body("[0].origin.name", equalTo("Resort Planet"))
                //validate just one Value was populated
                .body("[1]", nullValue());
    }

    @Test
    public void tc033_validateCharacterEndpointFilterByMultipleIDsErrorOnSecondID() {
        Response response = characterAPI.getMultipleCharacters("369,922");

        response.then()
                .statusCode(200)
                .body("[0].id", equalTo(369))
                .body("[0].name", equalTo("Tusked Assassin"))
                .body("[0].status", equalTo("unknown"))
                .body("[0].species", equalTo("Alien"))
                .body("[0].type", equalTo("Tuskfish"))
                .body("[0].gender", equalTo("Male"))
                .body("[0].origin.name", equalTo("Resort Planet"))
                //validate just one Value was populated
                .body("[1]", nullValue());
    }

    @Test
    public void tc040_validateCharacterEndpointFilterByQueryParams() {
        Response response = characterAPI.FilterByQueryParams("name", "Beth Smith");

        int totalCount = response.path("results.size()");
        assertEquals(totalCount, 4, "We are expecting 4 Beth Smith");
    }

    @Test
    public void tc041_validateCharacterEndpointErrorFilteringByQueryParams() {
        Response response = characterAPI.FilterByQueryParams("name", "Beth Simpson");

        response.then()
                .statusCode(404)
                .body("error", equalTo("There is nothing here"));
    }

    @Test
    public void tc050_validateCharacterEndpointFilterByMultipleQueryParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "Beth Smith");
        map.put("type", "Soulless Puppet");
        Response response = characterAPI.FilterByMultipleQueryParams(map, "character");

        int infoCount = response.path("info.count");
        int resultCount = response.path("results.size()");
        assertEquals(infoCount, resultCount, "Information from Info, and Results should match");
    }

    @Test
    public void tc051_validateCharacterEndpointErrorFilteringByMultipleQueryParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "Beth Smith");
        map.put("type", "Fire");
        Response response = characterAPI.FilterByMultipleQueryParams(map, "character");

        response.then()
                .statusCode(404)
                .body("error", equalTo("There is nothing here"));
    }



}
