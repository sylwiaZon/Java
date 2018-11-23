package Graph;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Graph extends Application {
    double sample,a,b,numOfParam;
    double []parameters;
    GridPane panel;
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createChartGrid(800, 600, stage)));
        stage.show();
    }

    private Pane createChartGrid(double width, double height, Stage stage) {
        GridPane grid = new GridPane();
        panel = createPanel(width, height, stage);
        grid.addColumn(0,
                createPanel(width, height, stage),
                createBlankChart()
        );
        grid.setPrefSize(width,height);
        return grid;
    }

    private Pane updateChartGrid(double width, double height) {
        GridPane grid = new GridPane();
        grid.addColumn(0,
                panel,
                createChart()
        );
        grid.setPrefSize(width,height);
        return grid;
    }

    private LineChart<Number, Number> createChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        double distance = Math.abs((a-b)/sample);
        LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setTitle("Graph");
        XYChart.Series series = new XYChart.Series();
        series.setName("Function");
        for(double i = a; i < b; i = i + distance){
            series.getData().add(new XYChart.Data(i, function(i)));
        }
        lineChart.setCreateSymbols(false);
        lineChart.getData().add(series);
        return lineChart;
    }

    private LineChart<Number, Number> createBlankChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setTitle("Graph");
        XYChart.Series series = new XYChart.Series();
        series.setName("Function");
        lineChart.setCreateSymbols(false);
        lineChart.getData().add(series);
        return lineChart;
    }

    private GridPane createPanel(double width, double height, Stage stage){
        GridPane gridPane = new GridPane();
        Text text = new Text("Number of params");
        gridPane.add(text, 0,0);

        TextField textField = new TextField();
        gridPane.add(textField, 1, 0);

        Button button = new Button("Submit");
        gridPane.add(button, 2,0);

        TextField textFields[] = new TextField[6];
        Text param1 = new Text("a1");
        gridPane.add(param1, 0, 1);

        textFields[0] = new TextField();
        gridPane.add(textFields[0], 0, 2);

        Text param2 = new Text("a2");
        gridPane.add(param2, 0, 3);

        textFields[1] = new TextField();
        gridPane.add(textFields[1], 0, 4);

        Text param3 = new Text("a3");
        gridPane.add(param3, 1, 1);

        textFields[2] = new TextField();
        gridPane.add(textFields[2], 1, 2);

        Text param4 = new Text("a4");
        gridPane.add(param4, 1, 3);

        textFields[3] = new TextField();
        gridPane.add(textFields[3], 1, 4);

        Text param5 = new Text("a5");
        gridPane.add(param5, 2, 1);

        textFields[4] = new TextField();
        gridPane.add(textFields[4], 2, 2);

        Text param6 = new Text("a6");
        gridPane.add(param6, 2, 3);

        textFields[5] = new TextField();
        gridPane.add(textFields[5], 2, 4);

        Text aVal = new Text("Start");
        gridPane.add(aVal, 3, 1);

        TextField textFieldA = new TextField();
        gridPane.add(textFieldA, 3, 2);

        Text bVal = new Text("End");
        gridPane.add(bVal, 3, 3);

        TextField textFieldB = new TextField();
        gridPane.add(textFieldB, 3, 4);

        Text sampleText = new Text("Sample");
        gridPane.add(sampleText, 4, 1);

        TextField textFieldSample = new TextField();
        gridPane.add(textFieldSample, 4, 2);

        gridPane.setMaxSize(width,height/4);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        button.setOnAction(value ->  {
            if(textField.getText() != null){
                numOfParam = stringToDouble(textField.getText());
                parameters = new double[(int)numOfParam];
                for(int i=0;i<numOfParam;i++){
                    parameters[i] = stringToDouble(textFields[i].getText());
                }
                a = stringToDouble(textFieldA.getText());
                b = stringToDouble(textFieldB.getText());
                sample = stringToDouble(textFieldSample.getText());
            }
            stage.setScene(new Scene(updateChartGrid(width, height)));
            stage.show();
        });
        return gridPane;
    }


    private double function(double x){
        double y = 0;
        int pow = parameters.length - 1;
        for(double param: parameters){
            y = y + param * Math.pow(x,pow);
            pow = pow - 1;
        }
        return y;
    }

    private double stringToDouble(String string){
        double resp = 0;
        double flag = 1;
        for(int i=0; i<string.length(); i++){
            if(string.charAt(i) == '-'){
                flag = -1;
            } else {
                resp = resp * 10 + string.charAt(i) - '0';
            }
        }
        resp = resp * flag;
        return resp;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
