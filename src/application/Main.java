package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Classe Main,
 * @author mignotl
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
			primaryStage.getIcons().add(new Image("file:./img/icon.png"));
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

			Scene scene = new Scene(root, 800, 500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

			//initialisation de la taille du canvas
	        Canvas canvas = stackpane.getCanvas();
	        ScrollPane scrollpane = (ScrollPane) canvas.getScene().lookup("#scrollpane");
	        double x = (scrollpane.getWidth() - 5) / canvas.getWidth();
	        double u = (scrollpane.getHeight() - 5) / canvas.getHeight();
            stackpane.setScaleX(x > u ? u : x);
            stackpane.setScaleY(x > u ? u : x);
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
