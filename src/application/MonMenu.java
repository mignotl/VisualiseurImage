package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Classe MonMenu, héritant de MenuBar
 * 
 * Créer une barre de menu possèdant :
 *  - un menu File
 *  	- Ouvrir (une image)
 *  	- Sauvegarder (le dessin)
 *  	- Quitter (le programme)
 *  - un menu Edition
 *  	- Ajuster à la fenêtre
 *  	- mettre en noir et blanc
 *  	- annuler dernière action (non-implémenté)
 * 
 * La création de chaque menu est présent dans les méthodes 
 *  commençant par "create".
 * 
 * La gestion des actions sont gérées par les méthodes
 *  terminant par "Handler".
 * 
 * Cette classe a besoin du stage et d'une instance de la
 *  classe ZoneDessin pour pouvoir créer les méthodes
 *  gérant les actions.
 *  
 * @author MIGNOT - MATASSE
 *
 */
public class MonMenu extends MenuBar {
	private Stage primaryStage;
	private ZoneDessin stackpane;
	
	/**
	 * Constructeur
	 * 
	 * @param stage: Stage
	 * @param sp: ZoneDessin
	 */
	public MonMenu(Stage stage, ZoneDessin sp) {
		primaryStage = stage;
		stackpane = sp;
		
		this.getMenus().addAll(createMenuFile(), createMenuEdition());
	}
	
	/**
	 * Créé le menu Fichier et le renvoie
	 * 
	 * Le menu renvoyé possède trois menuItem :
	 *  Ouvrir, Sauvegarder et Quitter
	 *  
	 * @return Menu
	 */
	private Menu createMenuFile() {
		final Menu menuFile = new Menu("Fichier");
		final MenuItem menuFileOuvrir = new MenuItem("Ouvrir");
		menuFileOuvrir.setOnAction(fileOuvrirHandler());
		menuFile.getItems().add(menuFileOuvrir);
		
		final MenuItem menuFileSauvegarder = new MenuItem("Sauvegarder");
		menuFileSauvegarder.setOnAction(fileSauvHandler());
		menuFile.getItems().add(menuFileSauvegarder);
		
		final MenuItem menuFileQuitter = new MenuItem("Quitter");
		menuFileQuitter.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        System.out.println("Fermeture du programme...");
		        primaryStage.close();
		        Platform.exit();
		    }
		});
		menuFile.getItems().add(menuFileQuitter);
		return menuFile;
	}

	
	/**
	 * Créé le menu Edition et le renvoie
	 * 
	 * Le menu renvoyé possède trois menuItem :
	 *  Ajuster, Noir et blanc et Annuler
	 *  
	 * @return Menu
	 */
	private Menu createMenuEdition() {
		final Menu menuEdition = new Menu("Édition");
		final MenuItem menuEditionAjuster = new MenuItem("Ajuster à la fenêtre");
		menuEditionAjuster.setOnAction(editionAjusterHandler());
		menuEdition.getItems().add(menuEditionAjuster);
		
		final MenuItem menuEditionNoirBlanc = new MenuItem("Mettre en noir et blanc");
		menuEditionNoirBlanc.setOnAction(editionNoirBlancHandler());
		menuEdition.getItems().add(menuEditionNoirBlanc);
		
		final MenuItem menuEditionAnnuler = new MenuItem("Annuler dernière modification");
		menuEditionAnnuler.setDisable(true);
		menuEditionAnnuler.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        System.out.println("Annulation de la dernière modification...");
		    }
		});
		menuEdition.getItems().add(menuEditionAnnuler);
		return menuEdition;
	}

	/**
	 * Créé l'écouteur pour l'ouverture d'un fichier
	 * 
	 * @return EventHandler<ActionEvent>
	 */
	private EventHandler<ActionEvent> fileOuvrirHandler() {
		return new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        System.out.print("Sélection de l'image...");

	            FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Ouvrir Image");
	             
	            //Set extension filter
	            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
	            FileChooser.ExtensionFilter extFilterJPEG = new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.JPEG");
	            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
	            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterJPEG, extFilterPNG);
	              
	            //Show open file dialog
	            File file = fileChooser.showOpenDialog(null);
	            if (file == null) {
	            	System.out.println(" Annulée.");
	            	return;
	            }
                BufferedImage bufferedImage;
				try {
					bufferedImage = ImageIO.read(file);
	                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
	                stackpane.getCanvas().setWidth(image.getWidth());
	                stackpane.getCanvas().setHeight(image.getHeight());
	                stackpane.getCanvas().getGraphicsContext2D().drawImage(image, 0, 0);
	    			primaryStage.setTitle("Visualiseur d'image - " + file.getName());
	    			System.out.println(" Terminée.");
				} catch (IOException e1) {
			        System.out.println(" Erreur lors de l'importation de l'image.");
					e1.printStackTrace();
				}
		    }
		};
	}

	/**
	 * Créé l'écouteur pour la sauvegarde du dessin
	 * 
	 * @return EventHandler<ActionEvent>
	 */
	private EventHandler<ActionEvent> fileSauvHandler() {
		return new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        System.out.println("Sauvegarde de l'image...");
	
	            FileChooser fileChooser = new FileChooser();
	            fileChooser.setTitle("Sauvegarder");
	            
	            //Set extension filter
	            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
	            FileChooser.ExtensionFilter extFilterJPEG = new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.JPEG");
	            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
	            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterJPEG, extFilterPNG);
	
	            //Show open file dialog
	            File outputFile = fileChooser.showSaveDialog(primaryStage);
	            if (outputFile == null) {
	            	System.out.println(" Annulée.");
	            	return;
	            }
	            
		        BufferedImage bImage = SwingFXUtils.fromFXImage(stackpane.getCanvas().snapshot(null, null), null);
	            if (outputFile != null) {
			        try {
			            ImageIO.write(bImage, "png", outputFile);
			        } catch (IOException ex) {
			            throw new RuntimeException(ex);
			        }
	            }
		    }
		};
	}

	/**
	 * Créé l'écouteur pour l'ajustement de l'image à la fenêtre
	 * 
	 * @return EventHandler<ActionEvent>
	 */
	private EventHandler<ActionEvent> editionAjusterHandler() {
		return new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        System.out.print("Ajustement de l'image à la fenêtre...");
		        Canvas canvas = stackpane.getCanvas();
		        ScrollPane scrollpane = (ScrollPane) canvas.getScene().lookup("#scrollpane");
		        double x = (scrollpane.getWidth() - 5) / canvas.getWidth();
		        double u = (scrollpane.getHeight() - 5) / canvas.getHeight();
                stackpane.setScaleX(x > u ? u : x);
                stackpane.setScaleY(x > u ? u : x);
    			System.out.println(" Terminé.");
		    }
		};
	}

	/**
	 * Créé l'écouteur pour la transformation de l'image en norie et blanc
	 * 
	 * @return EventHandler<ActionEvent>
	 */
	private EventHandler<ActionEvent> editionNoirBlancHandler() {
		return new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        System.out.print("Transformation en noir et blanc de l'image...");
		        ColorAdjust colorAdjust = new ColorAdjust();
		        colorAdjust.setSaturation(-1.0);
		        stackpane.getCanvas().getGraphicsContext2D().applyEffect(colorAdjust);
    			System.out.println(" Terminée.");
		    }
		};
	}
}
