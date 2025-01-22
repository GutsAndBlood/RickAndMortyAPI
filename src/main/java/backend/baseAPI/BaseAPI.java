package backend.baseAPI;
import config.Config;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class BaseAPI {

    protected RequestSpecification requestSpec;

    public BaseAPI() {
        RestAssured.baseURI = Config.BASE_URL;
        requestSpec = RestAssured.given();
    }

}
