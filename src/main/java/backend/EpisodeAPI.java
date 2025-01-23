package backend;

import config.Config;
import io.restassured.response.Response;

public class EpisodeAPI extends PageObject {

    public Response getAllEpisodes() {
        return requestSpec.get(Config.EPISODE_ENDPOINT);
    }

    public Response getAllEpisodes(String callIteration) {
        return requestSpec.get(Config.EPISODE_ENDPOINT + callIteration);
    }

    public Response getEpisodeById(int id) {
        return requestSpec.get(Config.EPISODE_ENDPOINT + "/" + id);
    }

    public Response getMultipleEpisode(String ids) {
        return requestSpec.get(Config.EPISODE_ENDPOINT + "/" + ids);
    }

    public Response FilterByQueryParams(String param, String value) {
        return requestSpec.queryParam(param, value).get(Config.EPISODE_ENDPOINT);
    }
}
