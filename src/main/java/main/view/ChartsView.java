package main.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.controller.ControllerCharts;
import main.controller.utils.LanguageManager;
import main.model.Observer;

import java.io.IOException;

public class ChartsView implements Observer {
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private PieChart pieChart;

    private ControllerCharts chartController;
    public ChartsView(){
        chartController = new ControllerCharts();
        LanguageManager.getInstance().addObserver(this);
    }
    public void initialize() {
        chartController.populateLineChart(lineChart);
        chartController.populateBarChart(barChart);
        chartController.populatePieChart(pieChart);

        lineChart.setTitle("Number of Flights by Airport");
        lineChart.getXAxis().setLabel("Airports");
        lineChart.getYAxis().setLabel("Number of Flights");

        barChart.setTitle("Average Price per Flight");
        barChart.getXAxis().setLabel("Flights");
        barChart.getYAxis().setLabel("Average Price");

        pieChart.setTitle("Total Seats per Flight");

        update();
    }

    public static void show() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ChartsView.class.getResource("/main/charts-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage chartsStage = new Stage();
            chartsStage.initModality(Modality.APPLICATION_MODAL);
            chartsStage.setTitle("Charts View");
            chartsStage.setScene(new Scene(root));
            chartsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        lineChart.setTitle(LanguageManager.getInstance().getText("lineChartTitle"));
        lineChart.getXAxis().setLabel(LanguageManager.getInstance().getText("lineChartXLabel"));
        lineChart.getYAxis().setLabel(LanguageManager.getInstance().getText("lineChartYLabel"));

        barChart.setTitle(LanguageManager.getInstance().getText("barChartTitle"));
        barChart.getXAxis().setLabel(LanguageManager.getInstance().getText("barChartXLabel"));
        barChart.getYAxis().setLabel(LanguageManager.getInstance().getText("barChartYLabel"));

        pieChart.setTitle(LanguageManager.getInstance().getText("pieChartTitle"));
    }
}

