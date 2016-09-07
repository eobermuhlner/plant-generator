package ch.obermuhlner.plantgen.ui;

import java.util.Random;

import ch.obermuhlner.plantgen.ScriptPlant;
import ch.obermuhlner.plantgen.ui.turtle.TurtleGraphic;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

public class PlantGenGuiApp extends Application {

	private TurtleGraphic turtleGraphic;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Random Plant Browser");
        Group root = new Group();
        
        BorderPane mainBorderPane = new BorderPane();
        root.getChildren().add(mainBorderPane);

        Canvas canvas = new Canvas(1200, 600);
        mainBorderPane.setCenter(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        BorderPane editBorderPane = new BorderPane();
        mainBorderPane.setTop(editBorderPane);
        
        TextArea scriptTextArea= new TextArea();
        editBorderPane.setCenter(scriptTextArea);
        
        VBox buttonBox = new VBox();
        editBorderPane.setRight(buttonBox);
        
        Button runButton = new Button("Run");
        buttonBox.getChildren().add(runButton);
        runButton.addEventHandler(ActionEvent.ACTION, event -> {
        	drawPlant(scriptTextArea.getText(), gc);
        });

        Button randomButton = new Button("Random");
        buttonBox.getChildren().add(randomButton);
        randomButton.addEventHandler(ActionEvent.ACTION, event -> {
        	runRandomScript(gc, scriptTextArea);
        });

        runRandomScript(gc, scriptTextArea);
        
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
	}

	private void runRandomScript(GraphicsContext gc, TextArea scriptTextArea) {
		scriptTextArea.setText(RandomScriptGenerator.createRandomScript());
		drawPlant(scriptTextArea.getText(), gc);
	}

	private void drawPlant(String script, GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		
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
		
		gc.setLineCap(StrokeLineCap.ROUND);

		plant.initialize(turtleGraphic);
		turtleGraphic.execute(gc, description);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
