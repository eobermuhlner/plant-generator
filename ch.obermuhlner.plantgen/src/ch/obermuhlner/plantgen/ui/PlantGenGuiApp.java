package ch.obermuhlner.plantgen.ui;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

import ch.obermuhlner.plantgen.ScriptPlant;
import ch.obermuhlner.plantgen.ui.turtle.TurtleGraphic;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PlantGenGuiApp extends Application {

	private TurtleGraphic turtleGraphic;

	private StringProperty script = new SimpleStringProperty();
	private StringProperty expandedScript = new SimpleStringProperty();
	
	private DoubleProperty turnAngle = new SimpleDoubleProperty();
	private DoubleProperty standardDeviation = new SimpleDoubleProperty();
	private DoubleProperty initialThickness = new SimpleDoubleProperty();
	private DoubleProperty initialLength = new SimpleDoubleProperty();
	private DoubleProperty lengthFactor = new SimpleDoubleProperty();
	private DoubleProperty leafFactor = new SimpleDoubleProperty();
	private DoubleProperty leafThicknessFactor = new SimpleDoubleProperty();
	private DoubleProperty steps = new SimpleDoubleProperty();

	@Override
	public void start(Stage primaryStage) throws Exception {
        Font sourceFont = Font.font("Consolas");

        primaryStage.setTitle("Random Plant Browser");
        Group root = new Group();
        
        BorderPane mainBorderPane = new BorderPane();
        root.getChildren().add(mainBorderPane);

        // tab pane
        TabPane tabPane = new TabPane();
        mainBorderPane.setCenter(tabPane);
        
        // canvas in tab pane
        Canvas canvas = new Canvas(1200, 600);
        tabPane.getTabs().add(new Tab("2D", canvas));
        GraphicsContext gc = canvas.getGraphicsContext2D();

        TextArea expandedScriptTextArea = new TextArea();
		expandedScriptTextArea.setFont(sourceFont);
        tabPane.getTabs().add(new Tab("Source", expandedScriptTextArea));
        Bindings.bindBidirectional(expandedScript, expandedScriptTextArea.textProperty());
        
        BorderPane editBorderPane = new BorderPane();
        mainBorderPane.setTop(editBorderPane);
        
        // fields
        GridPane fieldsGridPane = new GridPane();
        fieldsGridPane.setHgap(4);
        fieldsGridPane.setVgap(4);
        editBorderPane.setLeft(fieldsGridPane);
        BorderPane.setMargin(fieldsGridPane, new Insets(4));

        int gridRow = 0;
        
        addSlider(fieldsGridPane, gridRow++, "Turn Angle", turnAngle, 0, 120, 45, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Randomness", standardDeviation, 0, 0.5, 0, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Initial Thickness", initialThickness, 10, 40, 20, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Initial Length", initialLength, 10, 50, 25, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Length Factor", lengthFactor, 0.5, 2.0, 1.0, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Leaf Size", leafFactor, 0, 20, 5, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Leaf Relative Size", leafThicknessFactor, 0, 4, 2, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Steps", steps, 3, 10, 7, "#0");

        Button randomizeButton = new Button("Randomize");
        fieldsGridPane.add(randomizeButton, 1, gridRow++);
        randomizeButton.addEventHandler(ActionEvent.ACTION, event -> {
        	randomizeValues();
        	drawPlant(gc);
        });

        // script
        TextArea scriptTextArea= new TextArea();
        scriptTextArea.setFont(sourceFont);
        editBorderPane.setCenter(scriptTextArea);
        BorderPane.setMargin(scriptTextArea, new Insets(4));
        Bindings.bindBidirectional(script, scriptTextArea.textProperty());
        
        // buttons
        VBox buttonBox = new VBox();
        buttonBox.setSpacing(4);
        editBorderPane.setRight(buttonBox);
        BorderPane.setMargin(buttonBox, new Insets(4));
        
        Button runButton = new Button("Run Script");
        buttonBox.getChildren().add(runButton);
        runButton.addEventHandler(ActionEvent.ACTION, event -> {
        	drawPlant(gc);
        });

        Button randomPlantButton = new Button("Random Plant");
        buttonBox.getChildren().add(randomPlantButton);
        randomPlantButton.addEventHandler(ActionEvent.ACTION, event -> {
        	runRandomScript(gc, scriptTextArea);
        });

        // property listeners
        for (ObservableValue<?> observableValue : Arrays.asList(turnAngle, standardDeviation, initialThickness, initialLength, lengthFactor, leafFactor, leafThicknessFactor, steps)) {
        	observableValue.addListener((observable, oldValue, newValue) -> {
            	drawPlant(gc);
            });
        }

        
        runRandomScript(gc, scriptTextArea);
        
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
	}
	
	private void addSlider(GridPane gridPane, int gridRow, String label, DoubleProperty doubleProperty, double min, double max, double value, String formatPattern) {
        gridPane.add(new Text(label), 0, gridRow);
        
        Slider slider = new Slider(min, max, value);
        Bindings.bindBidirectional(doubleProperty, slider.valueProperty());
		gridPane.add(slider, 1, gridRow);
		
		Text valueText = new Text();
		Bindings.bindBidirectional(valueText.textProperty(), doubleProperty, new DecimalFormat(formatPattern));
		gridPane.add(valueText, 2, gridRow);
	}

	private void runRandomScript(GraphicsContext gc, TextArea scriptTextArea) {
		scriptTextArea.setText(RandomScriptGenerator.createRandomScript());

		randomizeValues();
		drawPlant(gc);
	}

	private void randomizeValues() {
		Random random = new Random();

		turnAngle.set(random.nextDouble() * 60 + 10);
		standardDeviation.set(random.nextDouble() * 0.0 + 0.1);
		initialThickness.set(random.nextDouble() * 10 + 10);
		initialLength.set(random.nextDouble() * 30 + 20);
		lengthFactor.set(1.0);
		leafFactor.set(random.nextDouble() * 7.0 + 1.0);
		leafThicknessFactor.set(random.nextDouble() * 4.0);
		steps.set(random.nextInt(5) + 5);
	}

	private void drawPlant(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		gc.setLineCap(StrokeLineCap.ROUND);
		
		TurtleState initialState = new TurtleState();
		initialState.x = gc.getCanvas().getWidth() / 2;
		initialState.y = gc.getCanvas().getHeight() - 10;
		initialState.angle = -Math.PI / 2.0;
		initialState.thickness = 1.0;
		initialState.length = 10.0;
		turtleGraphic = new TurtleGraphic(initialState);

		Random random = new Random();
		
		ScriptPlant plant = new ScriptPlant(random, script.get());
		
		plant.setTurnAngle(Math.toRadians(turnAngle.get()));
		plant.setStandardDeviation(standardDeviation.get());
		plant.setInitialLength(initialLength.get());
		plant.setInitialThickness(initialThickness.get());
		plant.setLengthFactor(lengthFactor.get());
		plant.setLeafFactor(leafFactor.get());
		plant.setLeafThicknessFactor(leafThicknessFactor.get());
		plant.setSteps((int)steps.get());

		String description = plant.getDescription();
		expandedScript.set(formatSimpleScript(description));

		plant.initialize(turtleGraphic);
		
		turtleGraphic.execute(gc, description);
	}
	
	private String formatSimpleScript(String script) {
		StringBuilder formatted = new StringBuilder();
		char[] chars = script.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			formatted.append(chars[i]);
			if (i % 80 == 80 - 1) {
				formatted.append('\n');
			}
		}
		return formatted.toString();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
