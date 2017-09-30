package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
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
 * @author mignotl
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
		
		// Create ContextMenu
		ContextMenu contextMenu = new ContextMenu();

		final MenuItem menuEditionZoomUp = new MenuItem("Zoom +0.5");
		menuEditionZoomUp.setOnAction(editionZoomUpHandler());
		final MenuItem menuEditionZoomDown = new MenuItem("Zoom -0.5");
		menuEditionZoomDown.setOnAction(editionZoomDownHandler());
		final MenuItem menuEditionZoomAjuster = new MenuItem("Ajuster à la fenêtre");
		menuEditionZoomAjuster.setOnAction(editionZoomAjusterHandler());
		final MenuItem menuEditionZoomReset = new MenuItem("Annuler le zoom");
		menuEditionZoomReset.setOnAction(editionZoomResetHandler());
		contextMenu.getItems().addAll(menuEditionZoomUp, menuEditionZoomDown, menuEditionZoomAjuster, menuEditionZoomReset);
		sp.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				contextMenu.show(sp, event.getScreenX(), event.getScreenY());
			}
		});
	}
	
	/**
	 * Crée le menu Fichier et le renvoie
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
		menuFileOuvrir.setAccelerator(new KeyCharacterCombination("o", KeyCombination.CONTROL_DOWN));
		menuFile.getItems().add(menuFileOuvrir);
		
		final MenuItem menuFileSauvegarder = new MenuItem("Sauvegarder");
		menuFileSauvegarder.setOnAction(fileSauvHandler());
		menuFileSauvegarder.setAccelerator(new KeyCharacterCombination("s", KeyCombination.CONTROL_DOWN));
		menuFile.getItems().add(menuFileSauvegarder);
		
		final MenuItem menuFileQuitter = new MenuItem("Quitter");
		menuFileQuitter.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        System.out.print("Fermeture du programme...");
		    	Alert alert = new Alert(AlertType.CONFIRMATION);
		    	alert.setTitle("Fermer le programme ?");
		    	alert.setContentText("ëtes-vous sûr de vouloir quitter ?");

		    	Optional<ButtonType> result = alert.showAndWait();
		    	if (result.get() == ButtonType.OK){
			        primaryStage.close();
			        Platform.exit();
		    	} else {
			        System.out.println(" Annulée.");
		    	}
		    }
		});
		menuFileQuitter.setAccelerator(new KeyCharacterCombination("q", KeyCombination.CONTROL_DOWN));
		menuFile.getItems().add(menuFileQuitter);
		
		return menuFile;
	}

	
	/**
	 * Crée le menu Edition et le renvoie
	 * 
	 * Le menu renvoyé possède trois menuItem :
	 *  Ajuster, Noir et blanc et Annuler
	 *  
	 * @return Menu
	 */
	private Menu createMenuEdition() {
		final Menu menuEdition = new Menu("Édition");
		final Menu menuEditionZoom = new Menu("Zoom");
		final MenuItem menuEditionZoomUp = new MenuItem("Zoom +0.5");
		menuEditionZoomUp.setOnAction(editionZoomUpHandler());
		menuEditionZoomUp.setAccelerator(new KeyCharacterCombination("p", KeyCombination.CONTROL_DOWN));
		final MenuItem menuEditionZoomDown = new MenuItem("Zoom -0.5");
		menuEditionZoomDown.setOnAction(editionZoomDownHandler());
		menuEditionZoomDown.setAccelerator(new KeyCharacterCombination("m", KeyCombination.CONTROL_DOWN));
		final MenuItem menuEditionZoomAjuster = new MenuItem("Ajuster à la fenêtre");
		menuEditionZoomAjuster.setOnAction(editionZoomAjusterHandler());
		menuEditionZoomAjuster.setAccelerator(new KeyCharacterCombination("a", KeyCombination.CONTROL_DOWN));
		final MenuItem menuEditionZoomReset = new MenuItem("Annuler le zoom");
		menuEditionZoomReset.setOnAction(editionZoomResetHandler());
		menuEditionZoomReset.setAccelerator(new KeyCharacterCombination("r", KeyCombination.CONTROL_DOWN));
		menuEditionZoom.getItems().addAll(menuEditionZoomUp, menuEditionZoomDown, menuEditionZoomAjuster, menuEditionZoomReset);
		menuEdition.getItems().add(menuEditionZoom);
		
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
	 * Crée l'écouteur pour l'ouverture d'un fichier
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
	 * Crée l'écouteur pour la sauvegarde du dessin
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
	 * Crée l'écouteur pour l'ajustement de l'image à la fenêtre
	 * 
	 * @return EventHandler<ActionEvent>
	 */
	private EventHandler<ActionEvent> editionZoomAjusterHandler() {
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
	 * Crée l'écouteur pour l'ajustement de l'image à la fenêtre
	 * 
	 * @return EventHandler<ActionEvent>
	 */
	private EventHandler<ActionEvent> editionZoomResetHandler() {
		return new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        System.out.print("Reset du zoom...");
                stackpane.setScaleX(1);
                stackpane.setScaleY(1);
    			System.out.println(" Terminé.");
		    }
		};
	}
	/**
	 * Crée l'écouteur pour l'ajustement de l'image à la fenêtre
	 * 
	 * @return EventHandler<ActionEvent>
	 */
	private EventHandler<ActionEvent> editionZoomUpHandler() {
		return new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        System.out.print("Zoom...");
                stackpane.setScaleX(stackpane.getScaleX() * 1.1);
                stackpane.setScaleY(stackpane.getScaleY() * 1.1);
    			System.out.println(" Terminé.");
		    }
		};
	}
	/**
	 * Crée l'écouteur pour l'ajustement de l'image à la fenêtre
	 * 
	 * @return EventHandler<ActionEvent>
	 */
	private EventHandler<ActionEvent> editionZoomDownHandler() {
		return new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        System.out.print("Dézoom...");
                stackpane.setScaleX(stackpane.getScaleX() * 0.9);
                stackpane.setScaleY(stackpane.getScaleY() * 0.9);
    			System.out.println(" Terminé.");
		    }
		};
	}

	/**
	 * Crée l'écouteur pour la transformation de l'image en norie et blanc
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
