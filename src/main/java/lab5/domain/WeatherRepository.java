package lab5.domain;

public interface WeatherRepository {
    void getWeather(double lat, double lon, ResponseCallback callback);

    record WeatherUnits(String airPressureAtSeaLevel, String airTemperature, String cloudAreaFraction, String precipitationAmount, String relativeHumidity, String windFromDirection, String windSpeed) {}
    record WeatherStats(double airPressureAtSeaLevel, double airTemperature, double cloudAreaFraction, double relativeHumidity, double windFromDirection, double windSpeed) {}
    record WeatherResult(WeatherUnits units, WeatherStats stats) {}

    interface ResponseCallback {
        void onResponse(WeatherResult result);
        void onError(String errorMessage);
    }
}
