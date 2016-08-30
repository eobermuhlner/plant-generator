package ch.obermuhlner.plantgen.ui;

import ch.obermuhlner.plantgen.Plant;
import ch.obermuhlner.plantgen.ui.turtle.TurtleGraphic;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import ch.obermuhlner.plantgen.ui.turtle.command.ColorCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.CompositeCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.ForwardCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.PopCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.PushCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.TurnCommand;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PlantGenGuiApp extends Application {

	private TurtleGraphic turtleGraphic;

	public PlantGenGuiApp() {
		TurtleState initialState = new TurtleState();
		initialState.x = 400;
		initialState.y = 590;
		initialState.angle = -Math.PI / 2.0;
		turtleGraphic = new TurtleGraphic(initialState);
		
		double turnAngle = 22.5 / 360 * 2 * Math.PI;
		double step = 20;
		
		turtleGraphic.addCommand('[', new PushCommand());
		turtleGraphic.addCommand(']', new PopCommand());
		turtleGraphic.addCommand('-', new TurnCommand(-turnAngle));
		turtleGraphic.addCommand('+', new TurnCommand(turnAngle));
		turtleGraphic.addCommand('T', new CompositeCommand(new ColorCommand(Color.BLACK), new ForwardCommand(step)));
		turtleGraphic.addCommand('B', new CompositeCommand(new ColorCommand(Color.BROWN), new ForwardCommand(step)));
		turtleGraphic.addCommand('L', new CompositeCommand(new ColorCommand(Color.GREEN), new ForwardCommand(step)));
		turtleGraphic.setDefaultCommand(new CompositeCommand(new ColorCommand(Color.BLACK), new ForwardCommand(step)));
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawPlant(gc);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
	}

	private void drawPlant(GraphicsContext gc) {
		Plant plant = new Plant();
		String description = plant.getDescription();
		
		gc.setStroke(Color.RED);
		gc.setLineWidth(4);
		turtleGraphic.execute(gc, description);
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
