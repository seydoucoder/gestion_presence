package com.gestion_presence.gestion_presence;

import com.gestion_presence.gestion_presence.Dao.EmargementsDao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;

import java.net.URL;
import java.util.Map;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private BarChart<String, Number> barChartProfesseurs;

    @FXML
    private LineChart<String, Number> lineChartEmargements;

    @FXML
    private PieChart pieChartCours;

   EmargementsDao emargementsDao = new EmargementsDao();



    private void loadProfesseursStats() {
        Map<String, Long> stats = emargementsDao.getEmargementsCountByProfesseur();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Présences");

        for (Map.Entry<String, Long> entry : stats.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChartProfesseurs.getData().clear();
        barChartProfesseurs.getData().add(series);
    }


    private void loadEvolutionStats() {
        Map<String, Long> evolutionStats = emargementsDao.getEmargementsEvolution();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Présences par période");

        for (Map.Entry<String, Long> entry : evolutionStats.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }


        lineChartEmargements.getData().clear();
        lineChartEmargements.getData().add(series);
    }


    private void loadPresenceParCoursStats() {
        Map<String, Double> presenceParCours = emargementsDao.getPresenceParCours();

        pieChartCours.getData().clear();
        for (Map.Entry<String, Double> entry : presenceParCours.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChartCours.getData().add(slice);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProfesseursStats();
        loadEvolutionStats();
        loadPresenceParCoursStats();
    }
}
