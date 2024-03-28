package lab5.ui;

import lab5.domain.GeoRepository;
import lab5.domain.WeatherRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {
    private final GeoRepository geoModel;
    private final WeatherRepository weatherModel;

    public MainWindow(GeoRepository geoModel, WeatherRepository weatherModel) {
        super("Lab5 Погода");

        this.geoModel = geoModel;
        this.weatherModel = weatherModel;

        this.setSize(800, 600);
        this.setMinimumSize(new Dimension(600, 400));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.showStartupLoading();
    }

    private void showStartupLoading() {
        LoadingComponent loadingComponent = new LoadingComponent(100, 15, Color.DARK_GRAY);

        this.add(loadingComponent);

        this.revalidate();
        this.repaint();

        var timer = new Timer(60000, actionEvent -> {
            this.getContentPane().removeAll();
            this.showMainMenu();
        });
        timer.setRepeats(false);
        timer.start();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                removeMouseListener(this);
                timer.stop();
                getContentPane().removeAll();
                showMainMenu();
            }
        });
    }

    private void showMainMenu() {
        SidebarPanel sidebarPanel = new SidebarPanel();

        final int[] currentId = {0};
        AddPlacePanel addPlacePanel = new AddPlacePanel(geoModel, (searchTerm, formatted, lat, lng) -> {
            String id = "entry_" + currentId[0];
            WeatherPanel weatherPanel = new WeatherPanel(weatherModel, formatted, lat, lng, () -> sidebarPanel.removeEntry(id));
            sidebarPanel.addEntry(id, searchTerm, weatherPanel);
            sidebarPanel.selectEntry(id);
            currentId[0] += 1;
        });

        sidebarPanel.addEntry("add_place", "Добавить место", addPlacePanel);

        this.add(sidebarPanel);

        this.revalidate();
        this.repaint();
    }

}
