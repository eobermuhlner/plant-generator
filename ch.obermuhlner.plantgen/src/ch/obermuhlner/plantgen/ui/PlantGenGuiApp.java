package ch.obermuhlner.plantgen.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

import ch.obermuhlner.plantgen.ScriptPlant;
import ch.obermuhlner.plantgen.ui.turtle.TurtleGraphic;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class PlantGenGuiApp extends Application {

	private static final boolean SHOW_3D = false;

	private TurtleGraphic turtleGraphic;

	private StringProperty script = new SimpleStringProperty();
	private StringProperty expandedScript = new SimpleStringProperty();

	private LongProperty seed = new SimpleLongProperty();
	private DoubleProperty turnAngle = new SimpleDoubleProperty();
	private DoubleProperty standardDeviation = new SimpleDoubleProperty();
	private DoubleProperty initialThickness = new SimpleDoubleProperty();
	private DoubleProperty initialLength = new SimpleDoubleProperty();
	private DoubleProperty lengthFactor = new SimpleDoubleProperty();
	private DoubleProperty leafSize = new SimpleDoubleProperty();
	private DoubleProperty leafThicknessFactor = new SimpleDoubleProperty();
	private DoubleProperty leafLengthFactor = new SimpleDoubleProperty();
	private DoubleProperty leafWidthFactor = new SimpleDoubleProperty();
	private DoubleProperty leafWidthAngle = new SimpleDoubleProperty();
	private DoubleProperty steps = new SimpleDoubleProperty();

	private Group world;

	@Override
	public void start(Stage primaryStage) throws Exception {
        Font sourceFont = Font.font("Consolas");

        primaryStage.setTitle("Random Plant Browser");
        Group root = new Group();
        Scene scene = new Scene(root);
        
        BorderPane mainBorderPane = new BorderPane();
        root.getChildren().add(mainBorderPane);

        // tab pane
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        mainBorderPane.setCenter(tabPane);
        
        // 2D in tab pane
        Canvas canvas = new Canvas(1200, 600);
        tabPane.getTabs().add(new Tab("2D", canvas));
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 3D in tab pane
        if (SHOW_3D) {
        	StackPane node3dContainer = new StackPane();
        	node3dContainer.heightProperty();
        	tabPane.getTabs().add(new Tab("3D", node3dContainer));        
        	world = new Group();
        	Node node3d = createNode3D(node3dContainer, world);
        	node3dContainer.getChildren().add(node3d);
        }

        // source in tab pane
        TextArea expandedScriptTextArea = new TextArea();
		expandedScriptTextArea.setFont(sourceFont);
		expandedScriptTextArea.setEditable(false);
        tabPane.getTabs().add(new Tab("Source", expandedScriptTextArea));
        Bindings.bindBidirectional(expandedScript, expandedScriptTextArea.textProperty());

        // help in tab pane
        TextArea helpTextArea = new TextArea();
		helpTextArea.setEditable(false);
		helpTextArea.setText(loadTextResource("readme.txt"));
        tabPane.getTabs().add(new Tab("Help", helpTextArea));

        // editor border pane
        BorderPane editBorderPane = new BorderPane();
        mainBorderPane.setTop(editBorderPane);
        
        // fields in editor border pane
        GridPane fieldsGridPane = new GridPane();
        fieldsGridPane.setHgap(4);
        fieldsGridPane.setVgap(4);
        editBorderPane.setLeft(fieldsGridPane);
        BorderPane.setMargin(fieldsGridPane, new Insets(4));

        int gridRow = 0;

        addTextField(fieldsGridPane, gridRow++, "Random Seed", seed, 0, Long.MAX_VALUE, 1, "##0");
        addSlider(fieldsGridPane, gridRow++, "Turn Angle", turnAngle, 0, 120, 45, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Randomness", standardDeviation, 0, 0.5, 0, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Initial Thickness", initialThickness, 10, 40, 20, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Initial Length", initialLength, 10, 50, 25, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Length Factor", lengthFactor, 0.5, 2.0, 1.0, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Leaf Size", leafSize, 0, 20, 5, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Leaf Thickness", leafThicknessFactor, 0, 4, 2, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Leaf Length", leafLengthFactor, 1, 20, 2, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Leaf Width", leafWidthFactor, 1, 20, 2, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Leaf Width Angle", leafWidthAngle, 0, 120, 45, "##0.000");
        addSlider(fieldsGridPane, gridRow++, "Steps", steps, 1, 10, 5, "#0");

        Button randomizeButton = new Button("Randomize");
        fieldsGridPane.add(randomizeButton, 1, gridRow++);
        randomizeButton.addEventHandler(ActionEvent.ACTION, event -> {
        	randomizeValues();
        	drawPlant(gc);
        });

        // script in editor border pane
        TextArea scriptTextArea= new TextArea();
        scriptTextArea.setFont(sourceFont);
        editBorderPane.setCenter(scriptTextArea);
        BorderPane.setMargin(scriptTextArea, new Insets(4));
        Bindings.bindBidirectional(script, scriptTextArea.textProperty());
        
        // buttons in editor border pane
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
        for (ObservableValue<?> observableValue : Arrays.asList(seed, turnAngle, standardDeviation, initialThickness, initialLength, lengthFactor, leafSize, leafThicknessFactor, leafLengthFactor, leafWidthFactor, leafWidthAngle, steps)) {
        	observableValue.addListener((observable, oldValue, newValue) -> {
            	drawPlant(gc);
            });
        }
        
        runRandomScript(gc, scriptTextArea);
        
		primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	private String loadTextResource(String path) {
		StringBuilder text = new StringBuilder();
		
		InputStream in = getClass().getClassLoader().getResourceAsStream(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			String line = reader.readLine();
			while (line != null) {
				text.append(line);
				text.append("\n");
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return text.toString();
	}

	private Node createNode3D(Region container, Group world) {
        Box box = new Box();
        box.setMaterial(new PhongMaterial(Color.YELLOW));
        world.getChildren().add(box);
        
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(
        		new Rotate(-20, Rotate.Y_AXIS),
        		new Rotate(-20, Rotate.X_AXIS),
        		new Translate(0, 0, -100)
        		);
        world.getChildren().add(camera);

        SubScene subScene = new SubScene(world, 800, 600, false, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);
        subScene.setCamera(camera);
        subScene.heightProperty().bind(container.heightProperty());
        subScene.widthProperty().bind(container.widthProperty());
        
        return subScene;
	}

	private void addTextField(GridPane gridPane, int gridRow, String label, LongProperty longProperty, double min, double max, double value, String formatPattern) {
        gridPane.add(new Text(label), 0, gridRow);
        
        TextField textField = new TextField();
        Bindings.bindBidirectional(textField.textProperty(), longProperty, new DecimalFormat(formatPattern));
		gridPane.add(textField, 1, gridRow);
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
		seed.set(new Random().nextLong());
		
		Random random = new Random(seed.get());
		
		turnAngle.set(random.nextDouble() * 60 + 10);
		standardDeviation.set(random.nextDouble() * 0.0 + 0.1);
		initialThickness.set(random.nextDouble() * 10 + 10);
		initialLength.set(random.nextDouble() * 30 + 20);
		lengthFactor.set(1.0);
		leafSize.set(random.nextDouble() * 7.0 + 1.0);
		leafThicknessFactor.set(random.nextDouble() * 4.0);
		leafLengthFactor.set(random.nextDouble() * 10.0 + 1.0);
		leafWidthFactor.set(random.nextDouble() * 3.0 + 1.0);
		leafWidthAngle.set(random.nextDouble() * 90);
		steps.set(random.nextInt(5) + 2);
	}

	private void drawPlant(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		gc.setLineCap(StrokeLineCap.ROUND);
		
		if (SHOW_3D) {
			world.getChildren().clear();
		}
		
		TurtleState initialState = new TurtleState();
		initialState.x2d = gc.getCanvas().getWidth() / 2;
		initialState.y2d = gc.getCanvas().getHeight() - 10;
		initialState.angle = -Math.PI / 2.0;
		initialState.thickness = 1.0;
		initialState.length = 10.0;
		turtleGraphic = new TurtleGraphic(initialState);

		Random random = new Random(seed.get());
		
		ScriptPlant plant = new ScriptPlant(random, script.get());
		
		plant.setTurnAngle(Math.toRadians(turnAngle.get()));
		plant.setStandardDeviation(standardDeviation.get());
		plant.setInitialLength(initialLength.get());
		plant.setInitialThickness(initialThickness.get());
		plant.setLengthFactor(lengthFactor.get());
		plant.setLeafFactor(leafSize.get());
		plant.setLeafThicknessFactor(leafThicknessFactor.get());
		plant.setLeafLengthFactor(leafLengthFactor.get());
		plant.setLeafWidthFactor(leafWidthFactor.get());
		plant.setLeafWidthAngle(Math.toRadians(leafWidthAngle.get()));
		plant.setSteps((int)steps.get());

		String description = plant.getDescription();
		expandedScript.set(formatSimpleScript(description));

		plant.initialize(turtleGraphic);
		
		turtleGraphic.execute(description, gc, SHOW_3D ? world : null);
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
