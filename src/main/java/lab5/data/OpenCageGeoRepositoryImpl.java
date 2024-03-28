package lab5.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab5.domain.GeoRepository;
import org.asynchttpclient.AsyncHttpClient;

import java.util.ArrayList;

public class OpenCageGeoRepositoryImpl implements GeoRepository {
    private final static String OPENCAGE_KEY = "YOUR_OPENCAGE_KEY";
    private final AsyncHttpClient asyncHttpClient;

    public OpenCageGeoRepositoryImpl(AsyncHttpClient asyncHttpClient) {
        this.asyncHttpClient = asyncHttpClient;
    }

    @Override
    public void getPlaces(String searchTerm, ResponseCallback callback) {
        var whenResponse = asyncHttpClient
                .prepareGet("https://api.opencagedata.com/geocode/v1/json")
                .addQueryParam("key", OPENCAGE_KEY)
                .addQueryParam("q", searchTerm)
                .execute();

        whenResponse.addListener(() -> {
            try {
                var response = whenResponse.get();
                var mapper = new ObjectMapper();
                var jsonResponse = mapper.readTree(response.getResponseBody());

                var results = jsonResponse.get("results");

                var parsedResults = new ArrayList<GeoResult>();
                if (!results.isEmpty()) {
                    for (var node : results) {
                        var lat = node.get("geometry").get("lat").asDouble();
                        var lon = node.get("geometry").get("lng").asDouble();
                        var formatted = node.get("formatted").asText();
                        parsedResults.add(new GeoResult(formatted, lat, lon));
                    }
                }
                callback.onResponse(parsedResults.toArray(new GeoResult[0]));
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }, null);
    }
}
