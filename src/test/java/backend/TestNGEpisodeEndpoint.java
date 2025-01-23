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
public class TestNGEpisodeEndpoint extends TestBase {

    protected int CurrentCharactersCount = 51;
    protected int actualCharactersCount;
    protected int actualPagesCount;
    protected String characterLink = "https://rickandmortyapi.com/api/episode?page=";
    private static final Logger logger = LoggerFactory.getLogger(TestNGEpisodeEndpoint.class);

    @Test
    public void tc001_validateEpisodeEndpointHeaderResponse() {
        Response response = episodeAPI.getAllEpisodes();

        response.then()
                .statusCode(200)
                .header("Access-Control-Allow-Origin", equalTo("*"))
                .header("Content-Type", equalTo("application/json; charset=utf-8"));

        logger.info("Logger to show 'good practices' for debugging purposes");
    }

    @Test
    public void tc002_validateEpisodeEndpointBodyParametersFirstCall() {
        Response response = episodeAPI.getAllEpisodes();

        response.then()
                .statusCode(200)
                .body("info.count", notNullValue())
                .body("info.pages", notNullValue())
                .body("info.next", notNullValue())
                .body("info.prev", nullValue())

                // results
                .body("results[0].id", notNullValue())
                .body("results[0].name", notNullValue())
                .body("results[0].air_date", notNullValue())
                .body("results[0].episode", notNullValue())
                .body("results[0].characters", notNullValue())
                .body("results[0].url", notNullValue())
                .body("results[0].created", notNullValue());
    }

    @Test
    public void tc003_validateEpisodeEndpointInfoParametersFirstCall() {
        Response response = episodeAPI.getAllEpisodes();

        actualCharactersCount = response.path("info.count");
        actualPagesCount = response.path("info.pages");
        String nextLink = response.path("info.next");
        String prevLink = response.path("info.prev");

        assertEquals(actualCharactersCount, CurrentCharactersCount, "Character Count dont Match with 826");
        assertEquals(actualPagesCount, 3, "Character Page dont Match with 42");
        assertEquals(nextLink, characterLink+2, "Next Link dont Match with expected Value");
        assertNull(prevLink, "Prev Link dont Match with expected Value");
    }

    @Test
    public void tc004_validateEpisodeEndpointInfoParametersSecondCall() {
        Response response = episodeAPI.getAllEpisodes("?page=2");

        actualCharactersCount = response.path("info.count");
        actualPagesCount = response.path("info.pages");
        String nextLink = response.path("info.next");
        String prevLink = response.path("info.prev");

        assertEquals(actualCharactersCount, CurrentCharactersCount, "Character Count dont Match with 826");
        assertEquals(actualPagesCount, 3, "Character Page dont Match with 42");
        assertEquals(nextLink, characterLink+3, "Next Link dont Match with expected Value");
        assertEquals(prevLink, characterLink+1, "Prev Link dont Match with expected Value");
    }

    @Test
    public void tc010_validateEpisodeEndpointResultsParametersFirstUsername() {
        Response response = episodeAPI.getAllEpisodes("?page=1");

        response.then()
                .statusCode(200)
                .body("results[0].id", equalTo(1))
                .body("results[0].name", equalTo("Pilot"))
                .body("results[0].air_date", equalTo("December 2, 2013"))
                .body("results[0].episode", equalTo("S01E01"))
                .body("results[0].characters[0]", equalTo("https://rickandmortyapi.com/api/character/1"))
                .body("results[0].url", equalTo("https://rickandmortyapi.com/api/episode/1"));
    }

    @Test
    public void tc011_validateEpisodeEndpointResultsErrorNumberOfPages() {
        Response response = episodeAPI.getAllEpisodes("?page=9");

        response.then()
                .statusCode(404)
                .body("error", equalTo("There is nothing here"));
    }

    @Test
    public void tc020_validateEpisodeEndpointFilterByID() {
        Response response = episodeAPI.getEpisodeById(2);

        response.then()
                .statusCode(200)
                .body("id", equalTo(2))
                .body("name", equalTo("Lawnmower Dog"))
                .body("air_date", equalTo("December 9, 2013"))
                .body("episode", equalTo("S01E02"))
                .body("characters[0]", equalTo("https://rickandmortyapi.com/api/character/1"))
                .body("url", equalTo("https://rickandmortyapi.com/api/episode/2"));
    }

    @Test
    public void tc021_validateEpisodeEndpointErrorFilteringByID() {
        Response response = episodeAPI.getEpisodeById(222);

        response.then()
                .statusCode(404)
                .body("error", equalTo("Episode not found"));
    }

    @Test
    public void tc030_validateEpisodeEndpointFilterByMultipleIDs() {
        Response response = episodeAPI.getMultipleEpisode("10,22");

        response.then()
                .statusCode(200)
                .body("[0].id", equalTo(10))
                .body("[0].name", equalTo("Close Rick-counters of the Rick Kind"))
                .body("[0].air_date", equalTo("April 7, 2014"))
                .body("[0].episode", equalTo("S01E10"))
                .body("[0].characters[0]", equalTo("https://rickandmortyapi.com/api/character/1"))
                //second ID
                .body("[1].id", equalTo(22))
                .body("[1].name", equalTo("The Rickshank Rickdemption"))
                .body("[1].air_date", equalTo("April 1, 2017"))
                .body("[1].episode", equalTo("S03E01"))
                .body("[1].characters[0]", equalTo("https://rickandmortyapi.com/api/character/1"))
                //validate no more Values are populated
                .body("[2]", nullValue());
    }

    @Test
    public void tc031_validateCEpisodeEndpointFilterByMultipleIDsFiveValues() {
        Response response = episodeAPI.getMultipleEpisode("1,22,23,30,32");

        response.then()
                .statusCode(200);

        int totalCount = response.path("results.size()");
        assertEquals(totalCount, 5, "We are expecting 5 Characters but got " + totalCount);
    }

    @Test
    public void tc032_validateEpisodeEndpointFilterByMultipleIDsErrorOnFirstID() {
        Response response = episodeAPI.getMultipleEpisode("100,22");

        response.then()
                .statusCode(200)
                .body("[0].id", equalTo(22))
                .body("[0].name", equalTo("The Rickshank Rickdemption"))
                .body("[0].air_date", equalTo("April 1, 2017"))
                .body("[0].episode", equalTo("S03E01"))
                .body("[0].characters[0]", equalTo("https://rickandmortyapi.com/api/character/1"))
                //validate just one Value was populated
                .body("[1]", nullValue());
    }

    @Test
    public void tc033_validateEpisodeEndpointFilterByMultipleIDsErrorOnSecondID() {
        Response response = episodeAPI.getMultipleEpisode("10,322");

        response.then()
                .statusCode(200)
                .body("[0].id", equalTo(10))
                .body("[0].name", equalTo("Close Rick-counters of the Rick Kind"))
                .body("[0].air_date", equalTo("April 7, 2014"))
                .body("[0].episode", equalTo("S01E10"))
                .body("[0].characters[0]", equalTo("https://rickandmortyapi.com/api/character/1"))
                //validate just one Value was populated
                .body("[1]", nullValue());
    }

    @Test
    public void tc040_validateEpisodeEndpointFilterByQueryParams() {
        Response response = episodeAPI.FilterByQueryParams("name", "Ricks");

        int totalCount = response.path("results.size()");
        assertEquals(totalCount, 3, "We are expecting 3 Episodes");
    }

    @Test
    public void tc041_validateEpisodeEndpointErrorFilteringByQueryParams() {
        Response response = characterAPI.FilterByQueryParams("name", "Ricks Simpson");

        response.then()
                .statusCode(404)
                .body("error", equalTo("There is nothing here"));
    }

    @Test
    public void tc050_validateEpisodeEndpointFilterByMultipleQueryParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "Ricks");
        map.put("air_date", "April");
        Response response = characterAPI.FilterByMultipleQueryParams(map, "episode");

        int infoCount = response.path("info.count");
        int resultCount = response.path("results.size()");
        assertEquals(infoCount, resultCount, "Information from Info, and Results should match");
    }

    @Test
    public void tc051_validateEpisodeEndpointErrorFilteringByMultipleQueryParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "Morty");
        map.put("episode", "00");
        Response response = characterAPI.FilterByMultipleQueryParams(map, "episode");

        response.then()
                .statusCode(404)
                .body("error", equalTo("There is nothing here"));
    }

}
