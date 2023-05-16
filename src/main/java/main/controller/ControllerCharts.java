package main.controller;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import main.model.Flight.FlightDAO;
import main.model.Observer;

import java.util.Map;

public class ControllerCharts implements Observer {
    public void populateLineChart(LineChart<String, Number> lineChart) {
        FlightDAO flightDAO = new FlightDAO();
        Map<String, Integer> flightsByAirport = flightDAO.getNumberOfFlightsByAirport();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<String, Integer> entry : flightsByAirport.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().add(series);
    }

    public void populateBarChart(BarChart<String, Number> barChart) {
        FlightDAO flightDAO = new FlightDAO();
        Map<String, Double> averagePricePerAirline = flightDAO.getAveragePricePerFlight();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<String, Double> entry : averagePricePerAirline.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);
    }

    public void populatePieChart(PieChart pieChart) {
        FlightDAO flightDAO = new FlightDAO();
        Map<String, Integer> totalSeatsPerAirline = flightDAO.getTotalSeatsPerFlight();

        for (Map.Entry<String, Integer> entry : totalSeatsPerAirline.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChart.getData().add(slice);
        }
    }

    @Override
    public void update() {

    }
}

