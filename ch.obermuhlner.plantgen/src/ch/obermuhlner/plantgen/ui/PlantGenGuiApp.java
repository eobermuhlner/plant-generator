package ch.obermuhlner.plantgen.ui;

import java.util.Arrays;
import java.util.Random;

import ch.obermuhlner.plantgen.ScriptPlant;
import ch.obermuhlner.plantgen.ui.turtle.TurtleGraphic;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PlantGenGuiApp extends Application {

	private TurtleGraphic turtleGraphic;
	
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
		primaryStage.setTitle("Random Plant Browser");
        Group root = new Group();
        
        BorderPane mainBorderPane = new BorderPane();
        root.getChildren().add(mainBorderPane);

        // canvas
        Canvas canvas = new Canvas(1200, 600);
        mainBorderPane.setCenter(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        BorderPane editBorderPane = new BorderPane();
        mainBorderPane.setTop(editBorderPane);
        
        // fields
        GridPane fieldsGridPane = new GridPane();
        editBorderPane.setLeft(fieldsGridPane);

        int gridRow = 0;
        
        addSlider(fieldsGridPane, gridRow++, "Turn Angle", turnAngle, 0, 90, 45);
        addSlider(fieldsGridPane, gridRow++, "Standard Deviation", standardDeviation, 0, 0.5, 0);
        addSlider(fieldsGridPane, gridRow++, "Initial Thickness", initialThickness, 10, 20, 15);
        addSlider(fieldsGridPane, gridRow++, "Initial Length", initialLength, 10, 50, 25);
        addSlider(fieldsGridPane, gridRow++, "Length Factor", lengthFactor, 0.8, 1.2, 1.0);
        addSlider(fieldsGridPane, gridRow++, "Leaf Factor", leafFactor, 0, 8, 3);
        addSlider(fieldsGridPane, gridRow++, "Leaf Thickness Factor", leafThicknessFactor, 0, 4, 2);
        addSlider(fieldsGridPane, gridRow++, "Steps", steps, 3, 10, 7);
		
        // script
        TextArea scriptTextArea= new TextArea();
        editBorderPane.setCenter(scriptTextArea);
        
        // buttons
        VBox buttonBox = new VBox();
        editBorderPane.setRight(buttonBox);
        
        Button runButton = new Button("Run");
        buttonBox.getChildren().add(runButton);
        runButton.addEventHandler(ActionEvent.ACTION, event -> {
        	drawPlant(scriptTextArea.getText(), gc);
        });

        Button randomScriptButton = new Button("Random Script");
        buttonBox.getChildren().add(randomScriptButton);
        randomScriptButton.addEventHandler(ActionEvent.ACTION, event -> {
        	runRandomScript(gc, scriptTextArea);
        });

        // property listeners
        for (ObservableValue<?> observableValue : Arrays.asList(turnAngle, standardDeviation, initialThickness, initialLength, lengthFactor, leafFactor, leafThicknessFactor, steps)) {
        	observableValue.addListener((observable, oldValue, newValue) -> {
            	drawPlant(scriptTextArea.getText(), gc);
            });
        }

        
        runRandomScript(gc, scriptTextArea);
        
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
	}
	
	private void addSlider(GridPane gridPane, int gridRow, String label, DoubleProperty doubleProperty, double min, double max, double value) {
        gridPane.add(new Text(label), 0, gridRow);
        Slider slider = new Slider(min, max, value);
        Bindings.bindBidirectional(doubleProperty, slider.valueProperty());
		gridPane.add(slider, 1, gridRow);
	}

	private void runRandomScript(GraphicsContext gc, TextArea scriptTextArea) {
		scriptTextArea.setText(RandomScriptGenerator.createRandomScript());

		Random random = new Random();
		turnAngle.set(random.nextDouble() * 60 + 10);
		standardDeviation.set(random.nextDouble() * 0.0 + 0.1);
		initialThickness.set(random.nextDouble() * 10 + 10);
		initialLength.set(random.nextDouble() * 30 + 20);
		lengthFactor.set(1.0);
		leafFactor.set(random.nextDouble() * 7.0 + 1.0);
		leafThicknessFactor.set(random.nextDouble() * 4.0);
		steps.set(random.nextInt(5) + 5);
		
		drawPlant(scriptTextArea.getText(), gc);
	}

	private void drawPlant(String script, GraphicsContext gc) {
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
		
		ScriptPlant plant = new ScriptPlant(random, script);
		
		plant.setTurnAngle(Math.toRadians(turnAngle.get()));
		plant.setStandardDeviation(standardDeviation.get());
		plant.setInitialLength(initialLength.get());
		plant.setInitialThickness(initialThickness.get());
		plant.setLengthFactor(lengthFactor.get());
		plant.setLeafFactor(leafFactor.get());
		plant.setLeafThicknessFactor(leafThicknessFactor.get());
		plant.setSteps((int)steps.get());

		String description = plant.getDescription();

		plant.initialize(turtleGraphic);
		
		turtleGraphic.execute(gc, description);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
