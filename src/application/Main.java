package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Classe Main,
 * @author MIGNOT - MATASSE
 */
public class Main extends Application {
	/**
	 * starting method
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setMinHeight(300);
			primaryStage.setMinWidth(600);
			primaryStage.setTitle("Visualiseur d'image");
			BorderPane root = new BorderPane();

		    MaPalette toolBar = new MaPalette();
			VBox menus = new VBox();
	        ZoneDessin stackpane = new ZoneDessin(toolBar);
			menus.getChildren().addAll(new MonMenu(primaryStage, stackpane), toolBar);
			root.setTop(menus);

			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setId("scrollpane");
			Group group = new Group(stackpane);
	        scrollPane.setContent(group);
	        scrollPane.setStyle("-fx-background: #424242;");
			root.setCenter(scrollPane);
			
			primaryStage.setScene(new Scene(root, 800, 500));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * method where the program start
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
