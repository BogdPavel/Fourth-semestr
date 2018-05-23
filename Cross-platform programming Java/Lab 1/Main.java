package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Rectangle");

        //Labels
        VBox layoutLabel = new VBox(5);
        Label widthLabel = new Label("Длинна");
        Label heightLabel = new Label("Высота");
        layoutLabel.getChildren().addAll(widthLabel, heightLabel);
        layoutLabel.setPadding(new Insets(0,60,0,15));

        //TextFields
        VBox layoutTextField = new VBox();
        TextField widthTextField = new TextField();
        widthTextField.setPromptText("5");
        widthTextField.addEventFilter(KeyEvent.KEY_TYPED , e -> {
            if(!e.getCharacter().matches("[0-9]") || ((TextField)e.getSource()).getText().length() >= 5)
                e.consume();
        });
        TextField heightTextField = new TextField();
        heightTextField.setPromptText("2");
        heightTextField.addEventFilter(KeyEvent.KEY_TYPED , e -> {
            if(!e.getCharacter().matches("[0-9]") || ((TextField)e.getSource()).getText().length() >= 5)
                e.consume();
        });
        layoutTextField.getChildren().addAll(widthTextField, heightTextField);
        layoutTextField.setMinWidth(250);

        //Interface
        HBox layoutInterface = new HBox(9);
        layoutInterface.setPadding(new Insets(10,0,0,0));

        Button buttonArea = new Button("Площадь");
        buttonArea.setMinWidth(65);

        Button buttonPerimeter = new Button("Периметр");
        buttonPerimeter.setMinWidth(65);

        Button buttonClear = new Button("Очистить");
        buttonClear.setOnAction(e -> {
            heightTextField.clear();
            widthTextField.clear();
        });
        buttonClear.setMinWidth(65);


        Label textArea = new Label();
        textArea.setMinWidth(65);

        Label textPerimeter = new Label();
        textPerimeter.setMinWidth(65);

        layoutInterface.getChildren().addAll(buttonArea, textArea, buttonPerimeter, textPerimeter, buttonClear);

        //Calculating
        buttonArea.setOnAction( e -> {
            if(errorCheck(widthTextField, heightTextField)) {
                int width = Integer.parseInt(widthTextField.getText());
                int height = Integer.parseInt(heightTextField.getText());
                textArea.setText("" + (width * height));
            }
            else textArea.setText("Error");
        });
        buttonPerimeter.setOnAction( e -> {
            if(errorCheck(widthTextField, heightTextField)) {
                int width = Integer.parseInt(widthTextField.getText());
                int height = Integer.parseInt(heightTextField.getText());
                textPerimeter.setText(""+ (2 * width + 2 * height));
            }
            else textPerimeter.setText("Error");
        });

        //Layouts
        HBox layoutInput = new HBox();
        layoutInput.getChildren().addAll(layoutLabel, layoutTextField);
        VBox layout = new VBox();
        layout.getChildren().addAll(layoutInput, layoutInterface);
        layout.setPadding(new Insets(5));

        Scene scene = new Scene(layout, 380, 87);

        layout.requestFocus();
        textArea.maxHeight(40);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private boolean errorCheck(TextField widthTextField, TextField heightTextField) {
        if(widthTextField.getText().length() == 0 || heightTextField.getText().length() == 0) return false;
        if(Integer.parseInt(widthTextField.getText()) == 0 || Integer.parseInt(heightTextField.getText()) == 0) return false;
        else return true;
    }
}
