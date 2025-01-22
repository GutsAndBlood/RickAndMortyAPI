package backend;

import config.Config;
import io.restassured.response.Response;

public class CharacterAPI extends PageObject {

    public Response getAllCharacters() {
        return requestSpec.get(Config.CHARACTER_ENDPOINT);
    }

    public Response getAllCharacters(String callIteration) {
        return requestSpec.get(Config.CHARACTER_ENDPOINT + callIteration);
    }

    public Response getCharacterById(int id) {
        return requestSpec.get(Config.CHARACTER_ENDPOINT + "/" + id);
    }

    public Response getMultipleCharacters(String ids) {
        return requestSpec.get(Config.CHARACTER_ENDPOINT + "/" + ids);
    }

    public Response FilterByQueryParams(String param, String value) {
        return requestSpec.queryParam(param, value).get(Config.CHARACTER_ENDPOINT);
    }


}
