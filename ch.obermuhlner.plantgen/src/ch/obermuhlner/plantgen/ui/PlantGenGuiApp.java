package ch.obermuhlner.plantgen.ui;

import java.util.Random;

import ch.obermuhlner.plantgen.ScriptPlant;
import ch.obermuhlner.plantgen.ui.turtle.TurtleGraphic;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
	private DoubleProperty standardDeviation = new SimpleDoubleProperty(0.0);

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
        
        fieldsGridPane.add(new Text("Turn Angle"), 0, 0);
        Slider turnAngleSlider = new Slider(0, 90, 45);
        Bindings.bindBidirectional(turnAngle, turnAngleSlider.valueProperty());
		fieldsGridPane.add(turnAngleSlider, 1, 0);
		
        fieldsGridPane.add(new Text("Standard Deviation"), 0, 1);
        Slider standardDeviationSlider = new Slider(0, 0.5, 0);
        Bindings.bindBidirectional(standardDeviation, standardDeviationSlider.valueProperty());
		fieldsGridPane.add(standardDeviationSlider, 1, 1);
		
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

        // value events
        turnAngleSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
        	drawPlant(scriptTextArea.getText(), gc);
        });
        standardDeviationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
        	drawPlant(scriptTextArea.getText(), gc);
        });
		
        runRandomScript(gc, scriptTextArea);
        
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
	}

	private void runRandomScript(GraphicsContext gc, TextArea scriptTextArea) {
		scriptTextArea.setText(RandomScriptGenerator.createRandomScript());

		Random random = new Random();
		turnAngle.set(random.nextDouble() * 60 + 10);
		standardDeviation.set(random.nextDouble() * 0.0 + 0.1);
		
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
		String description = plant.getDescription();
		
		plant.setTurnAngle(Math.toRadians(turnAngle.get()));
		plant.setStandardDeviation(standardDeviation.get());
		plant.initialize(turtleGraphic);
		
		turtleGraphic.execute(gc, description);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
