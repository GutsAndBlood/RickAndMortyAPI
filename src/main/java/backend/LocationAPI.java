package backend;

import config.Config;
import io.restassured.response.Response;

public class LocationAPI extends PageObject {

    public Response getAllLocations() {
        return requestSpec.get(Config.LOCATION_ENDPOINT);
    }

    public Response getAllLocations(String callIteration) {
        return requestSpec.get(Config.LOCATION_ENDPOINT + callIteration);
    }

    public Response getLocationById(int id) {
        return requestSpec.get(Config.LOCATION_ENDPOINT + "/" + id);
    }

    public Response getMultipleLocations(String ids) {
        return requestSpec.get(Config.LOCATION_ENDPOINT + "/" + ids);
    }

    public Response FilterByQueryParams(String param, String value) {
        return requestSpec.queryParam(param, value).get(Config.LOCATION_ENDPOINT);
    }

}
