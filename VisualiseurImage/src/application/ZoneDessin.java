package application;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.util.Pair;

/**
 * Classe ZoneDessin héritant de StackPane
 * 
 * Possède :
 * - la palette d'outils MaPalette
 * - le canvas sur lequel on dessine
 * - le GraphicsContext du canvas
 * - une variable pour sauvegarder la position initiale
 *  de la souris.
 * @author MIGNOT - MATASSE
 *
 */
public class ZoneDessin extends StackPane {
	private MaPalette toolBar;
	private Canvas canvas;
    private Pair<Double, Double> initialTouch;
    private GraphicsContext gc;
	
    /**
     * Constructeur
     * @param toolBar: MaPalette
     */
	public ZoneDessin(MaPalette toolBar) {
        this.setStyle("-fx-background: #FF0000;");
		this.toolBar = toolBar;
		canvas = new Canvas(750, 400);
        this.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        gc.setStroke(toolBar.getColorpickerValue());
        gc.setLineWidth(toolBar.getSliderValue());
        gc.setFill(Color.WHITE);
    	gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    	//adding listeners
        addActionMousePressed();
        addActionMouseDragged();
        addActionMouseClicked();
	}
	
	/**
	 * Ajout l'écouteur pour lorsqu'on appuie
	 * sur le bouton gauche de la souris
	 */
	private void addActionMousePressed() {
		this.setOnMousePressed(e->{
			if (!e.isPrimaryButtonDown())
				return;
            //quel RadioButton est activé ?
	    	if (toolBar.getSelected() == toolBar.getPenBtn()) {
	        	gc.beginPath();
				gc.setStroke(toolBar.getColorpickerValue());
	            gc.setLineWidth(toolBar.getSliderValue());
	        	gc.lineTo(e.getX(), e.getY());
	        	gc.setLineCap(StrokeLineCap.ROUND);
	        	gc.stroke();
	    	} else if (toolBar.getSelected() == toolBar.getLineBtn()
	    			|| toolBar.getSelected() == toolBar.getCircleBtn()
	    			|| toolBar.getSelected() == toolBar.getCircleOutlineBtn()
	    			|| toolBar.getSelected() == toolBar.getRectangleBtn()
	    			|| toolBar.getSelected() == toolBar.getRectangleOutlineBtn()
	    			|| toolBar.getSelected() == toolBar.getRognerBtn()) {
	            Canvas newLayer = new Canvas(canvas.getWidth(), canvas.getHeight());
	            canvas = newLayer;
	            this.getChildren().add(1, newLayer);
	            initialTouch = new Pair<>(e.getX(), e.getY());
	    	}
	    });
	}

	/**
	 * Ajout l'écouteur pour lorsqu'on reste appuyé
	 * sur le bouton gauche de la souris (drag)
	 */
	private void addActionMouseDragged() {
		this.setOnMouseDragged(e->{
			if (!e.isPrimaryButtonDown())
				return;
            //quel RadioButton est activé ?
	    	if (toolBar.getSelected() == toolBar.getPenBtn()) {
	        	gc.lineTo(e.getX(), e.getY());
	        	gc.stroke();
	    	} else {
	            GraphicsContext context = canvas.getGraphicsContext2D();
	            context.setStroke(toolBar.getColorpickerValue());
	            context.setLineWidth(toolBar.getSliderValue());
	            context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	        	if (toolBar.getSelected() == toolBar.getLineBtn()) {
	                context.setLineCap(StrokeLineCap.ROUND);
	                context.strokeLine(initialTouch.getKey(), initialTouch.getValue(), e.getX(), e.getY());
	        	} else if (toolBar.getSelected() == toolBar.getCircleBtn()) {
	                context.setFill(toolBar.getColorpickerValue());
	                context.fillOval(initialTouch.getKey(), initialTouch.getValue(), 
	                		e.getX() - initialTouch.getKey(), e.getY() - initialTouch.getValue());
	        	} else if (toolBar.getSelected() == toolBar.getCircleOutlineBtn()) {
	                context.strokeOval(initialTouch.getKey(), initialTouch.getValue(), 
	                		e.getX() - initialTouch.getKey(), e.getY() - initialTouch.getValue());
	        	} else if (toolBar.getSelected() == toolBar.getRectangleBtn()) {
	                context.setFill(toolBar.getColorpickerValue());
	                context.fillRect(initialTouch.getKey(), initialTouch.getValue(), 
	                		e.getX() - initialTouch.getKey(), e.getY() - initialTouch.getValue());
	        	} else if (toolBar.getSelected() == toolBar.getRectangleOutlineBtn()) {
	                context.strokeRect(initialTouch.getKey(), initialTouch.getValue(), 
	                		e.getX() - initialTouch.getKey(), e.getY() - initialTouch.getValue());
	        	} else if (toolBar.getSelected() == toolBar.getRognerBtn()) {
	        		context.setFill(Color.rgb(0, 0, 0, 0.5));
	                context.fillRect(initialTouch.getKey(), initialTouch.getValue(), 
	                		e.getX() - initialTouch.getKey(), e.getY() - initialTouch.getValue());
	        	}
	    	}
		});
	}

	/**
	 * Ajout l'écouteur pour lorsqu'on a fini
	 * d'appuyer sur le bouton gauche de la souris
	 */
	private void addActionMouseClicked() {
		this.setOnMouseClicked(e->{
			if (e.getButton() != MouseButton.PRIMARY)
				return;
			gc.setStroke(toolBar.getColorpickerValue());
            gc.setLineWidth(toolBar.getSliderValue());
            //quel RadioButton est activé ?
        	if (toolBar.getSelected() == toolBar.getPenBtn()) {
	        	gc.stroke();
	        	gc.closePath();
        	} else {
        		canvas = (Canvas) this.getChildren().get(0);
        		this.getChildren().removeIf(c -> c != canvas);
	        	if (toolBar.getSelected() == toolBar.getLineBtn()) {
	                gc.setLineCap(StrokeLineCap.ROUND);
	                gc.strokeLine(initialTouch.getKey(), initialTouch.getValue(), e.getX(), e.getY());
	        	} else if (toolBar.getSelected() == toolBar.getCircleBtn()) {
	                gc.setFill(toolBar.getColorpickerValue());
	                gc.fillOval(initialTouch.getKey(), initialTouch.getValue(), 
	                		e.getX() - initialTouch.getKey(), e.getY() - initialTouch.getValue());
	        	} else if (toolBar.getSelected() == toolBar.getCircleOutlineBtn()) {
	                gc.strokeOval(initialTouch.getKey(), initialTouch.getValue(), 
	                		e.getX() - initialTouch.getKey(), e.getY() - initialTouch.getValue());
	        	} else if (toolBar.getSelected() == toolBar.getRectangleBtn()) {
	                gc.setFill(toolBar.getColorpickerValue());
	                gc.fillRect(initialTouch.getKey(), initialTouch.getValue(), 
	                		e.getX() - initialTouch.getKey(), e.getY() - initialTouch.getValue());
	        	} else if (toolBar.getSelected() == toolBar.getRectangleOutlineBtn()) {
	                gc.strokeRect(initialTouch.getKey(), initialTouch.getValue(), 
	                		e.getX() - initialTouch.getKey(), e.getY() - initialTouch.getValue());
	        	} else if (toolBar.getSelected() == toolBar.getRognerBtn()) {
	        		SnapshotParameters parameters = new SnapshotParameters();
	        		parameters.setViewport(new Rectangle2D(initialTouch.getKey(), initialTouch.getValue(), 
	                		e.getX() - initialTouch.getKey(), e.getY() - initialTouch.getValue()));
	                WritableImage snapshot = canvas.snapshot(parameters, null);

	                FileChooser fileChooser = new FileChooser();
	                fileChooser.setTitle("Sauvegarder");
	                
		            //Set extension filter
		            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		            FileChooser.ExtensionFilter extFilterJPEG = new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.JPEG");
		            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterJPEG, extFilterPNG);

		            //Show open file dialog
	                File outputFile = fileChooser.showSaveDialog(canvas.getScene().getWindow());
		            if (outputFile == null) {
		            	System.out.println(" Annulée.");
		            	return;
		            }

	                try {
						ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", outputFile);
					} catch (IOException e1) {
		            	System.out.println(" Annulée.");
		            	return;
					}
	        	}
        	}
        });
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
}
