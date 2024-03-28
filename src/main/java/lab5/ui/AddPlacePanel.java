package lab5.ui;

import lab5.domain.GeoRepository;

import javax.swing.*;
import java.awt.*;

public class AddPlacePanel extends JPanel {

    AddPlacePanel(GeoRepository geoModel, AddCallback callback) {
        this.setLayout(new GridBagLayout());

        JTextField searchField = new JTextField("Иркутск");
        JButton searchButton = new JButton("Искать!");

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        searchButton.addActionListener(actionEvent -> {
            resultsPanel.removeAll();
            resultsPanel.add(new LoadingComponent(50, 12, Color.DARK_GRAY));
            resultsPanel.revalidate();
            resultsPanel.repaint();

            var searchTerm = searchField.getText();

            geoModel.getPlaces(searchTerm, new GeoRepository.ResponseCallback() {
                @Override
                public void onResponse(GeoRepository.GeoResult[] results) {
                    resultsPanel.removeAll();

                    if (results.length == 0) {
                        resultsPanel.add(new JLabel("Ничего не найдено :("));
                    } else {
                        for (var result : results) {
                            JPanel entryPanel = new JPanel();
                            entryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                            entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.X_AXIS));

                            JButton addButton = new JButton("+");
                            addButton.addActionListener(actionEvent1 -> callback.add(searchTerm, result.formattedName(), result.lat(), result.lon()));

                            JLabel placeLabel = new JLabel(result.formattedName());

                            entryPanel.add(addButton);
                            entryPanel.add(placeLabel);
                            resultsPanel.add(entryPanel);
                        }
                    }

                    resultsPanel.revalidate();
                    resultsPanel.repaint();
                }

                @Override
                public void onError(String errorMessage) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), errorMessage);
                }
            });
        });

        var gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(searchField, gbc);

        gbc.gridy = 2;
        this.add(searchButton, gbc);

        gbc.gridy = 3;
        gbc.weighty = 1;
        this.add(new JScrollPane(resultsPanel), gbc);
    }

    interface AddCallback {
        void add(String searchTerm, String formatted, double lat, double lng);
    }

}
