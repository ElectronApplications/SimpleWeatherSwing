package lab5.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lab5.domain.WeatherRepository;
import org.asynchttpclient.AsyncHttpClient;

import java.util.concurrent.ExecutionException;

public class MetNoWeatherRepositoryImpl implements WeatherRepository {
    private final AsyncHttpClient asyncHttpClient;

    public MetNoWeatherRepositoryImpl(AsyncHttpClient asyncHttpClient) {
        this.asyncHttpClient = asyncHttpClient;
    }

    @Override
    public void getWeather(double lat, double lon, ResponseCallback callback) {
        var whenResponse = asyncHttpClient
                .prepareGet("https://api.met.no/weatherapi/locationforecast/2.0/compact")
                .setHeader("User-Agent", "Weather App https://github.com/ElectronApplications/SimpleWeatherSwing")
                .addQueryParam("lat", String.valueOf(lat))
                .addQueryParam("lon", String.valueOf(lon))
                .execute();

        whenResponse.addListener(() -> {
            try {
                var response = whenResponse.get();
                var mapper = new ObjectMapper().setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
                var jsonResponse = mapper.readTree(response.getResponseBody());
                System.out.println(jsonResponse.toPrettyString());

                var units = jsonResponse.get("properties").get("meta").get("units");
                var weatherUnits = mapper.convertValue(units, WeatherUnits.class);

                var timeseries = jsonResponse.get("properties").get("timeseries");
                var dataNow = timeseries.get(0).get("data").get("instant").get("details");

                var weatherStats = mapper.convertValue(dataNow, WeatherStats.class);

                callback.onResponse(new WeatherResult(weatherUnits, weatherStats));
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }, null);
    }
}
