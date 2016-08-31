package ch.obermuhlner.plantgen.ui;

import java.util.Random;

import ch.obermuhlner.plantgen.Plant;
import ch.obermuhlner.plantgen.ui.turtle.TurtleGraphic;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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
		turtleGraphic = new TurtleGraphic(initialState);

		Random random = new Random();
		
		Plant plant = new Plant(random);
		String description = plant.getDescription();
		
		gc.setStroke(Color.RED);
		gc.setLineWidth(4);
		plant.initialize(turtleGraphic);
		turtleGraphic.execute(gc, description);
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
