package application;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Classe MaPalette héritant de ToolBar
 * 
 * Possède les boutons présents en paramètres,
 *  ainsi qu'un slider et un colorPicker.
 * Ces objets permettent de savoir quelle action
 *  sera effectuée sur le canvas de ZoneDessin
 * 
 * @author MIGNOT - MATASSE
 *
 */
public class MaPalette extends ToolBar {
	private ToggleGroup group;
	private RadioButton penBtn;
	private RadioButton lineBtn;
	private RadioButton circleBtn;
	private RadioButton circleOutlineBtn;
	private RadioButton rectangleBtn;
	private RadioButton rectangleOutlineBtn;
	private RadioButton rognerBtn;
	private ColorPicker colorpicker;
	private Slider slider;
	
	/**
	 * Constructeur
	 * 
	 * Créé les RadioButton, le slider, le colorpicker
	 */
	public MaPalette() {
		group = new ToggleGroup();
	    penBtn = createRadioButton("pen.png", "Crayon");
	    lineBtn = createRadioButton("line.png", "Trait");
	    circleBtn = createRadioButton("circle.png", "Cercle");
	    circleOutlineBtn = createRadioButton("circleoutline.png", "Cercle vide");
	    rectangleBtn = createRadioButton("rectangle.png", "Rectangle");
	    rectangleOutlineBtn = createRadioButton("rectangleoutline.png", "Rectangle vide");
	    rognerBtn = createRadioButton("crop.png", "Extraire");
	    penBtn.setSelected(true);
		
		colorpicker = new ColorPicker();
        colorpicker.setValue(Color.BLACK);
		
		slider = new Slider(1,  100, 1);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		Label widthlabel = new Label(String.format("%d", 
				(int) slider.getValue()));
		slider.valueProperty().addListener(e->{
			widthlabel.setText(String.format("%d", 
				(int) slider.getValue()));
		});
		 
		this.getItems().addAll(
				penBtn, lineBtn, circleBtn, circleOutlineBtn, 
				rectangleBtn, rectangleOutlineBtn, rognerBtn,
				colorpicker, slider, widthlabel);
	}
	
	/**
	 * Créer un RadioButton suivant les paramètres suivant :
	 * @param fileName: String nom de l'icone utilisée
	 * @param tooltip: String affiché au survol du bouton
	 * @return RadioButton
	 */
	public RadioButton createRadioButton(String fileName, String tooltip) {
	    RadioButton btn = new RadioButton();
		ImageView iv = new ImageView(new Image("file:./img/" + fileName));
		iv.setPreserveRatio(true);
		iv.setFitHeight(25.0);
		btn.setGraphic(iv);
		btn.getStyleClass().remove("radio-button");
		btn.getStyleClass().add("toggle-button");
		btn.setTooltip(new Tooltip(tooltip));
		btn.setToggleGroup(group);
		return btn;
	}
	
	/**
	 * 
	 * @return RadioButton sélectionné
	 */
	public Toggle getSelected() {
		return group.getSelectedToggle();
	}

	public RadioButton getPenBtn() {
		return penBtn;
	}

	public ToggleGroup getGroup() {
		return group;
	}

	public RadioButton getLineBtn() {
		return lineBtn;
	}

	public RadioButton getCircleBtn() {
		return circleBtn;
	}

	public RadioButton getCircleOutlineBtn() {
		return circleOutlineBtn;
	}

	public RadioButton getRectangleBtn() {
		return rectangleBtn;
	}

	public RadioButton getRectangleOutlineBtn() {
		return rectangleOutlineBtn;
	}

	public RadioButton getRognerBtn() {
		return rognerBtn;
	}

	public Color getColorpickerValue() {
		return colorpicker.getValue();
	}

	public double getSliderValue() {
		return slider.getValue();
	}
}
