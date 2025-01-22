package backend;

import backend.baseAPI.BaseAPI;
import config.Config;
import io.restassured.response.Response;

import java.util.Map;

/*
    This a java class created for re-utilization ( Imagine that this is a big project )
*/

public abstract class PageObject extends BaseAPI {

    public Response FilterByMultipleQueryParams(Map<String, String> params, String endpoint) {
        String url = endpointMap.get(endpoint);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestSpec.queryParam(entry.getKey(), entry.getValue());
        }

        return requestSpec.get(url);
    }

    private static final Map<String, String> endpointMap = Map.of(
            "character", Config.CHARACTER_ENDPOINT,
            "location", Config.LOCATION_ENDPOINT,
            "episode", Config.EPISODE_ENDPOINT
    );

}
