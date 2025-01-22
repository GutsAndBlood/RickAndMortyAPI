package baseAPI;
import backend.CharacterAPI;
import backend.LocationAPI;
import backend.baseAPI.BaseAPI;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterTest;

public class TestBase extends BaseAPI {

    protected CharacterAPI characterAPI;
    protected LocationAPI locationAPI;

    @BeforeMethod
    public void setup() {
        characterAPI = new CharacterAPI();
        locationAPI = new LocationAPI();
    }

    @AfterTest
    public void teardown() {
        RestAssured.reset();
    }
}

