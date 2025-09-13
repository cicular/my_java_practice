package practice.line;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class LinePostCountBarChart extends Application{

    @Override
    public void start(Stage stage) {

        File readFile = new File("上記の出力ファイル.txt.csv");

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        stage.setTitle(readFile.getName());
//        xAxis.setLabel("アカウント名");

        final BarChart<String,Number> barChart = new BarChart<>(xAxis,yAxis);
        barChart.setTitle(readFile.getName());

        XYChart.Series series = new XYChart.Series();
        series.setName("解析結果");

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(readFile), "SJIS"));

            String line;
            while ((line = br.readLine()) != null) {
                String[] strArray = line.split(",");
                series.getData().add(new XYChart.Data(strArray[0], Integer.valueOf(strArray[1])));
            }

            br.close();

        }catch(IOException e) {
            e.printStackTrace();
        }

        Scene scene  = new Scene(barChart,2100,600);
        barChart.getData().add(series);
//        barChart.setCategoryGap(10);

        stage.setScene(scene);
        scene.getStylesheets().add("ChartLine.css");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
