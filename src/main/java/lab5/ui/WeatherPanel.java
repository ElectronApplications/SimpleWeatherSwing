package lab5.ui;

import lab5.domain.WeatherRepository;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class WeatherPanel extends JPanel {
    private final WeatherRepository weatherModel;
    private final double lat;
    private final double lon;

    private final JPanel weatherPanel;

    WeatherPanel(WeatherRepository weatherModel, String name, double lat, double lon, RemoveCallback callback) {
        this.weatherModel = weatherModel;
        this.lat = lat;
        this.lon = lon;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(actionEvent -> callback.remove());

        JButton refreshButton = new JButton("Обновить");
        refreshButton.addActionListener(actionEvent -> this.refresh());

        JLabel header = new JLabel("<html>" + name + "</html>");
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setFont(new Font("Sans-Serif", Font.BOLD, 24));

        weatherPanel = new JPanel();
        weatherPanel.setLayout(new BorderLayout());
        weatherPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.X_AXIS));

        controlsPanel.add(closeButton);
        controlsPanel.add(refreshButton);

        this.add(controlsPanel);
        this.add(header);
        this.add(weatherPanel);

        this.refresh();
    }

    private void refresh() {
        this.weatherPanel.removeAll();
        LoadingComponent loadingComponent = new LoadingComponent(50, 12, Color.MAGENTA);
        this.weatherPanel.add(loadingComponent);
        this.weatherPanel.revalidate();
        this.weatherPanel.repaint();

        this.weatherModel.getWeather(this.lat, this.lon, new WeatherRepository.ResponseCallback() {
            @Override
            public void onResponse(WeatherRepository.WeatherResult result) {
                weatherPanel.remove(loadingComponent);

                var units = result.units();
                var stats = result.stats();

                JPanel weatherContent = new JPanel();
                weatherContent.setLayout(new BoxLayout(weatherContent, BoxLayout.Y_AXIS));

                JLabel airPressureLabel = new JLabel("Давление воздуха: " + stats.airPressureAtSeaLevel() + " " + units.airPressureAtSeaLevel());
                airPressureLabel.setFont(new Font("Sans-Serif", Font.BOLD, 18));
                airPressureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel airTemperatureLabel = new JLabel("Температура воздуха: " + stats.airTemperature() + " " + units.airTemperature());
                airTemperatureLabel.setFont(new Font("Sans-Serif", Font.BOLD, 18));
                airTemperatureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel cloudAreaFractionLabel = new JLabel("Облачность: " + stats.cloudAreaFraction() + " " + units.cloudAreaFraction());
                cloudAreaFractionLabel.setFont(new Font("Sans-Serif", Font.BOLD, 18));
                cloudAreaFractionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel relativeHumidityLabel = new JLabel("Относительная влажность: " + stats.relativeHumidity() + " " + units.relativeHumidity());
                relativeHumidityLabel.setFont(new Font("Sans-Serif", Font.BOLD, 18));
                relativeHumidityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel windFromDirectionLabel = new JLabel("Направление ветра: " + stats.windFromDirection() + " " + units.windFromDirection());
                windFromDirectionLabel.setFont(new Font("Sans-Serif", Font.BOLD, 18));
                windFromDirectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel windSpeedLabel = new JLabel("Скорость ветра: " + stats.windSpeed() + " " + units.windSpeed());
                windSpeedLabel.setFont(new Font("Sans-Serif", Font.BOLD, 18));
                windSpeedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                weatherContent.add(airPressureLabel);
                weatherContent.add(airTemperatureLabel);
                weatherContent.add(cloudAreaFractionLabel);
                weatherContent.add(relativeHumidityLabel);
                weatherContent.add(windFromDirectionLabel);
                weatherContent.add(windSpeedLabel);

                weatherPanel.add(weatherContent);

                weatherPanel.revalidate();
                weatherPanel.repaint();
            }

            @Override
            public void onError(String errorMessage) {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), errorMessage);
            }
        });
    }

    interface RemoveCallback {
        void remove();
    }

}
