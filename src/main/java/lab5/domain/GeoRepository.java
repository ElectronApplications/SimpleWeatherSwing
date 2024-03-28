package lab5.domain;

public interface GeoRepository {
    void getPlaces(String searchTerm, ResponseCallback callback);

    record GeoResult(String formattedName, double lat, double lon) {}
    interface ResponseCallback {
        void onResponse(GeoResult[] results);
        void onError(String errorMessage);
    }
}
