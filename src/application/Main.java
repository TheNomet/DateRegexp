package application;
	
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;



public class Main extends Application {
	
	
	
	@Override
	public void start(Stage stage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getResource("fxmlLayout.fxml"));
       
        Scene scene = new Scene(root, 500, 700);
        
        ColumnConstraints c1 = new ColumnConstraints(60);
        ColumnConstraints c2 = new ColumnConstraints(100);
        GridPane gp = (GridPane) scene.lookup("#grid");
        gp.getColumnConstraints().add(c1);
        gp.getColumnConstraints().add(c1);
        gp.getColumnConstraints().add(c1);
        
        TextArea ta = (TextArea) scene.lookup("#exceptions");
        ta.setEditable(false);
        
        
        ChoiceBox<String> format = (ChoiceBox<String>) scene.lookup("#format");
        
        format.setItems(FXCollections.observableArrayList("YY/MM/DD","DD/MM/YY"));
        format.getSelectionModel().selectFirst();
        
        ChoiceBox<String> separator = (ChoiceBox<String>) scene.lookup("#separator");
        
        separator.setItems(FXCollections.observableArrayList("-","/",":"));
        separator.getSelectionModel().selectFirst();
        
        ChoiceBox<String> even = (ChoiceBox<String>) scene.lookup("#even");
        
        even.setItems(FXCollections.observableArrayList("normal","even","odd"));
        even.getSelectionModel().selectFirst();
        
        init();
       
        
        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}