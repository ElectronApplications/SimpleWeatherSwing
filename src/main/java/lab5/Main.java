package lab5;

import lab5.domain.GeoRepository;
import lab5.domain.WeatherRepository;
import lab5.data.MetNoWeatherRepositoryImpl;
import lab5.data.OpenCageGeoRepositoryImpl;
import lab5.ui.MainWindow;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();
        WeatherRepository weatherModel = new MetNoWeatherRepositoryImpl(asyncHttpClient);
        GeoRepository geoModel = new OpenCageGeoRepositoryImpl(asyncHttpClient);
        MainWindow mainWindow = new MainWindow(geoModel, weatherModel);

        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                try {
                    asyncHttpClient.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}