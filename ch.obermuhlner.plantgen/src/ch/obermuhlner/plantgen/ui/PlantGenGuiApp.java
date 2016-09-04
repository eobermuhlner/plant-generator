package ch.obermuhlner.plantgen.ui;

import java.util.Random;

import ch.obermuhlner.plantgen.RandomStandardPlant;
import ch.obermuhlner.plantgen.TestPlant;
import ch.obermuhlner.plantgen.ui.turtle.TurtleGraphic;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

public class PlantGenGuiApp extends Application {

	private TurtleGraphic turtleGraphic;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Random Plant Browser");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setOnMouseClicked(mouseEvent -> drawPlant(gc));
        drawPlant(gc);

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
	}

	private void drawPlant(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		
		TurtleState initialState = new TurtleState();
		initialState.x = 400;
		initialState.y = 590;
		initialState.angle = -Math.PI / 2.0;
		initialState.thickness = 1.0;
		initialState.length = 10.0;
		turtleGraphic = new TurtleGraphic(initialState);

		Random random = new Random();
		
		//RandomStandardPlant plant = new RandomStandardPlant(random);
		TestPlant plant = new TestPlant(random);
		String description = plant.getDescription();
		
		gc.setLineCap(StrokeLineCap.ROUND);

		plant.initialize(turtleGraphic);
		turtleGraphic.execute(gc, description);
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
