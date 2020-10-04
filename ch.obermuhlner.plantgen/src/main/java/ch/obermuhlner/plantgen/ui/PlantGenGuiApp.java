package ch.obermuhlner.plantgen.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ch.obermuhlner.plantgen.ScriptPlant;
import ch.obermuhlner.plantgen.ui.turtle.TurtleGraphic;
import ch.obermuhlner.plantgen.ui.turtle.TurtleState;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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

	private static final int CANVAS_WIDTH = 1200;
	private static final int CANVAS_HEIGHT = 600;

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
	private DoubleProperty leafColorCenterOffset = new SimpleDoubleProperty();
	private DoubleProperty leafColor2Offset = new SimpleDoubleProperty();
	private DoubleProperty petalCount = new SimpleDoubleProperty();
	private DoubleProperty petalSize = new SimpleDoubleProperty();
	private DoubleProperty petalThicknessFactor = new SimpleDoubleProperty();
	private DoubleProperty petalLengthFactor = new SimpleDoubleProperty();
	private DoubleProperty petalWidthFactor = new SimpleDoubleProperty();
	private DoubleProperty petalWidthAngle = new SimpleDoubleProperty();
	private DoubleProperty petalColorCenterOffset = new SimpleDoubleProperty();
	private DoubleProperty petalColor2Offset = new SimpleDoubleProperty();
	private DoubleProperty flowerCenterSize = new SimpleDoubleProperty();
	private DoubleProperty steps = new SimpleDoubleProperty();

	private ObjectProperty<Color> trunkColor = new SimpleObjectProperty<>();
	private ObjectProperty<Color> branchColor = new SimpleObjectProperty<>();
	private ObjectProperty<Color> leaf1Color = new SimpleObjectProperty<>();
	private ObjectProperty<Color> leaf2Color = new SimpleObjectProperty<>();
	private ObjectProperty<Color> petal1Color = new SimpleObjectProperty<>();
	private ObjectProperty<Color> petal2Color = new SimpleObjectProperty<>();
	private ObjectProperty<Color> flowerCenterColor = new SimpleObjectProperty<>();
	
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
        
        // Plant 2D in tab pane
        Canvas plantCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        tabPane.getTabs().add(new Tab("Plant", plantCanvas));
        GraphicsContext plantGc = plantCanvas.getGraphicsContext2D();

        // Leaf 2D in tab pane
        Canvas leafCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        tabPane.getTabs().add(new Tab("Leaf", leafCanvas));
        GraphicsContext leafGc = leafCanvas.getGraphicsContext2D();

        // Flower 2D in tab pane
        Canvas flowerCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        tabPane.getTabs().add(new Tab("Flower", flowerCanvas));
        GraphicsContext flowerGc = flowerCanvas.getGraphicsContext2D();

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
				String helpText = loadTextResource("readme.txt");
				if (helpText != null) {
					helpTextArea.setText(helpText);
				}
        tabPane.getTabs().add(new Tab("Help", helpTextArea));

        // main border pane
        BorderPane editBorderPane = new BorderPane();
        mainBorderPane.setTop(editBorderPane);
        
        // editor
        HBox editorBox = new HBox(4);
        editBorderPane.setLeft(editorBox);
        
        Button randomPlantButton;
        
        // fields in editor border pane
        {
	        GridPane fieldsGridPane = createFieldsGridPane();
	        editorBox.getChildren().add(fieldsGridPane);
	
	        int gridRow = 0;

	        randomPlantButton = new Button("Random Plant");
	        fieldsGridPane.add(randomPlantButton, 1, gridRow++);

	        Button randomizeButton = new Button("Randomize Values");
	        fieldsGridPane.add(randomizeButton, 1, gridRow++);
	        randomizeButton.addEventHandler(ActionEvent.ACTION, event -> {
	        	randomizeValues();
	        	drawPlant(plantGc, leafGc, flowerGc);
	        });

	        addTextField(fieldsGridPane, gridRow++, "Random Seed", seed, 0, Long.MAX_VALUE, "##0");
	        addSlider(fieldsGridPane, gridRow++, "Steps", steps, 1, 10, "#0");
        }
	
        {
	        GridPane fieldsGridPane = createFieldsGridPane();
	        editorBox.getChildren().add(fieldsGridPane);
	
	        int gridRow = 0;
	
	        addSlider(fieldsGridPane, gridRow++, "Randomness", standardDeviation, 0, 0.5, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Turn Angle", turnAngle, 0, 120, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Initial Thickness", initialThickness, 10, 40, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Initial Length", initialLength, 10, 50, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Length Factor", lengthFactor, 0.5, 2.0, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Leaf Size", leafSize, 0, 20, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Leaf Thickness", leafThicknessFactor, -1, 1, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Leaf Length", leafLengthFactor, 1, 20, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Leaf Width", leafWidthFactor, 1, 20, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Leaf Width Angle", leafWidthAngle, 0, 120, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Leaf Color Center Offset", leafColorCenterOffset, 0, 0.2, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Leaf Color 2 Offset", leafColor2Offset, 0.3, 1.0, "##0.000");
        }

        {
	        GridPane fieldsGridPane = createFieldsGridPane();
	        editorBox.getChildren().add(fieldsGridPane);
	
	        int gridRow = 0;
	
	        addSlider(fieldsGridPane, gridRow++, "Petal Count", petalCount, 3, 20, "##0");
	        addSlider(fieldsGridPane, gridRow++, "Petal Size", petalSize, 0, 20, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Petal Thickness", petalThicknessFactor, 0, 4, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Petal Length", petalLengthFactor, 1, 20, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Petal Width", petalWidthFactor, 1, 20, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Petal Width Angle", petalWidthAngle, 0, 120, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Petal Color Center Offset", petalColorCenterOffset, 0, 0.2, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Petal Color 2 Offset", petalColor2Offset, 0.3, 1.0, "##0.000");
	        addSlider(fieldsGridPane, gridRow++, "Flower Center Size", flowerCenterSize, 0, 0.5, "##0.000");
	    }
	        
        {
	        GridPane fieldsGridPane = createFieldsGridPane();
	        editorBox.getChildren().add(fieldsGridPane);
	
	        int gridRow = 0;
	
	        addColorPicker(fieldsGridPane, gridRow++, "Trunk Color", trunkColor);
	        addColorPicker(fieldsGridPane, gridRow++, "Branch Color", branchColor);
	        addColorPicker(fieldsGridPane, gridRow++, "Leaf Center Color", leaf1Color);
	        addColorPicker(fieldsGridPane, gridRow++, "Leaf Tip Color", leaf2Color);
	        addColorPicker(fieldsGridPane, gridRow++, "Petal Center Color", petal1Color);
	        addColorPicker(fieldsGridPane, gridRow++, "Petal Tip Color", petal2Color);
	        addColorPicker(fieldsGridPane, gridRow++, "Flower Center Color", flowerCenterColor);
	    }
        
        // script in editor border pane
        TextArea scriptTextArea= new TextArea();
        scriptTextArea.setFont(sourceFont);
        editBorderPane.setCenter(scriptTextArea);
        BorderPane.setMargin(scriptTextArea, new Insets(4));
        Bindings.bindBidirectional(script, scriptTextArea.textProperty());
        
        randomPlantButton.addEventHandler(ActionEvent.ACTION, event -> {
        	runRandomScript(plantGc, leafGc, flowerGc, scriptTextArea);
        });

        // buttons in editor border pane
        VBox buttonBox = new VBox();
        buttonBox.setSpacing(4);
        editBorderPane.setRight(buttonBox);
        BorderPane.setMargin(buttonBox, new Insets(4));
        
        Button runButton = new Button("Run Script");
        buttonBox.getChildren().add(runButton);
        runButton.addEventHandler(ActionEvent.ACTION, event -> {
        	drawPlant(plantGc, leafGc, flowerGc);
        });

        // property listeners
        List<Property<? extends Object>> properties = Arrays.asList(
        		seed,
        		turnAngle,
        		standardDeviation,
        		initialThickness,
        		initialLength,
        		lengthFactor,
        		leafSize,
        		leafThicknessFactor,
        		leafLengthFactor,
        		leafWidthFactor,
        		leafWidthAngle,
        		leafColorCenterOffset,
        		leafColor2Offset,
        		petalCount,
        		petalSize,
        		petalThicknessFactor,
        		petalLengthFactor,
        		petalWidthFactor,
        		petalWidthAngle,
        		petalColorCenterOffset,
        		petalColor2Offset,
        		flowerCenterSize,
        		steps,
        		trunkColor,
        		branchColor,
        		leaf1Color,
        		leaf2Color,
        		petal1Color,
        		petal2Color,
        		flowerCenterColor);
		for (ObservableValue<?> observableValue : properties) {
        	observableValue.addListener((observable, oldValue, newValue) -> {
            	drawPlant(plantGc, leafGc, flowerGc);
            });
        }
        
        runRandomScript(plantGc, leafGc, flowerGc, scriptTextArea);
        
		primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	private String loadTextResource(String path) {
		StringBuilder text = new StringBuilder();
		
		try {
			InputStream in = getClass().getClassLoader().getResourceAsStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = reader.readLine();
			while (line != null) {
				text.append(line);
				text.append("\n");
				line = reader.readLine();
			}
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
			return null;
		}
		
		return text.toString();
	}

	private GridPane createFieldsGridPane() {
        GridPane fieldsGridPane = new GridPane();
        fieldsGridPane.setHgap(4);
        fieldsGridPane.setVgap(4);
        BorderPane.setMargin(fieldsGridPane, new Insets(4));
        return fieldsGridPane;
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

	private void addSlider(GridPane gridPane, int gridRow, String label, DoubleProperty doubleProperty, double min, double max, String formatPattern) {
        gridPane.add(new Text(label), 0, gridRow);
        
        Slider slider = new Slider(min, max, min);
        Bindings.bindBidirectional(doubleProperty, slider.valueProperty());
		gridPane.add(slider, 1, gridRow);
		
		Text valueText = new Text();
		Bindings.bindBidirectional(valueText.textProperty(), doubleProperty, new DecimalFormat(formatPattern));
		gridPane.add(valueText, 2, gridRow);
	}

	private void addTextField(GridPane gridPane, int gridRow, String label, LongProperty longProperty, double min, double max, String formatPattern) {
        gridPane.add(new Text(label), 0, gridRow);
        
        TextField textField = new TextField();
        Bindings.bindBidirectional(textField.textProperty(), longProperty, new DecimalFormat(formatPattern));
		gridPane.add(textField, 1, gridRow);
	}
	
	private void addColorPicker(GridPane gridPane, int gridRow, String label, ObjectProperty<Color> colorProperty) {
        gridPane.add(new Text(label), 0, gridRow);
        
        ColorPicker colorPicker = new ColorPicker();
        Bindings.bindBidirectional(colorPicker.valueProperty(), colorProperty);
		gridPane.add(colorPicker, 1, gridRow);
	}
	
	private void runRandomScript(GraphicsContext plantGc, GraphicsContext leafGc, GraphicsContext flowerGc, TextArea scriptTextArea) {
		scriptTextArea.setText(RandomScriptGenerator.createRandomScript());

		randomizeValues();
		drawPlant(plantGc, leafGc, flowerGc);
	}

	private void randomizeValues() {
		seed.set(new Random().nextLong());
		
		Random random = new Random(seed.get());
		
		turnAngle.set(random.nextDouble() * 60 + 10);
		standardDeviation.set(random.nextDouble() * 0.1 + 0.1);
		initialThickness.set(random.nextDouble() * 30 + 10);
		initialLength.set(random.nextDouble() * 30 + 20);
		lengthFactor.set(1.0);
		leafSize.set(random.nextDouble() * 7.0 + 4.0);
		leafThicknessFactor.set(random.nextDouble() * 2.0 - 1.0);
		leafLengthFactor.set(random.nextDouble() * 10.0 + 1.0);
		leafWidthFactor.set(random.nextDouble() * 3.0 + 1.0);
		leafWidthAngle.set(random.nextDouble() * 119 + 1);
		leafColorCenterOffset.set(random.nextDouble() * 0.2 + 0.0);
		leafColor2Offset.set(random.nextDouble() * 0.7 + 0.3);
		petalCount.set(random.nextDouble() < 0.8 ? random.nextInt(2) + 4 : random.nextInt(17) + 3);
		petalSize.set(random.nextDouble() * 2.0 + 1.0);
		petalThicknessFactor.set(random.nextDouble() * 0.1);
		petalLengthFactor.set(random.nextDouble() * 10.0 + 1.0);
		petalWidthFactor.set(random.nextDouble() * 3.0 + 1.0);
		petalWidthAngle.set(random.nextDouble() * 80 + 10);
		petalColorCenterOffset.set(random.nextDouble() * 0.2 + 0.0);
		petalColor2Offset.set(random.nextDouble() * 0.7 + 0.3);
		flowerCenterSize.set(random.nextDouble() * 0.5);
		steps.set(random.nextInt(5) + 2);
		
		trunkColor.set(Color.hsb(random.nextGaussian() * 5 + 5, random.nextDouble(), random.nextDouble() * 0.8 + 0.2));
		branchColor.set(Color.hsb(random.nextGaussian() * 5 + 5, random.nextDouble(), random.nextDouble() * 0.8 + 0.2));
		leaf1Color.set(Color.hsb(random.nextGaussian() * 20 + 110, random.nextDouble() * 0.8 + 0.2, random.nextDouble() * 0.6 + 0.4, random.nextDouble() * 0.4 + 0.6));
		leaf2Color.set(Color.hsb(random.nextGaussian() * 20 + 110, random.nextDouble() * 0.8 + 0.2, random.nextDouble() * 0.6 + 0.4, random.nextDouble() * 0.6 + 0.3));
		petal1Color.set(Color.hsb(random.nextDouble() * 360, random.nextDouble() * 0.8 + 0.2, random.nextDouble() * 0.2 + 0.8, random.nextDouble() * 0.4 + 0.6));
		petal2Color.set(Color.hsb(random.nextDouble() * 360, random.nextDouble() * 0.8 + 0.2, random.nextDouble() * 0.2 + 0.8, random.nextDouble() * 0.6 + 0.3));
		flowerCenterColor.set(Color.hsb(random.nextDouble() * 360, random.nextDouble() * 0.2 + 0.8, random.nextDouble() * 0.2 + 0.8));
	}

	private void drawPlant(GraphicsContext plantGc, GraphicsContext leafGc, GraphicsContext flowerGc) {
		drawPlant(script.get(), plantGc);
		drawPlant("S=TL;", leafGc);
		drawPlant("S=TTTTTF;", flowerGc);
	}
		
	private void drawPlant(String script, GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		gc.setLineCap(StrokeLineCap.ROUND);
		
		if (SHOW_3D) {
			world.getChildren().clear();
		}
		
		TurtleState initialState = new TurtleState();
		initialState.x = gc.getCanvas().getWidth() / 2;
		initialState.y = gc.getCanvas().getHeight() - 10;
		initialState.angle = -Math.PI / 2;
		initialState.thickness = 1.0;
		initialState.length = 10.0;
		initialState.update();
		turtleGraphic = new TurtleGraphic(initialState);

		Random random = new Random(seed.get());
		
		ScriptPlant plant = new ScriptPlant(random, script);
		
		plant.setTurnAngle(Math.toRadians(turnAngle.get()));
		plant.setStandardDeviation(standardDeviation.get());
		plant.setInitialLength(initialLength.get());
		plant.setInitialThickness(initialThickness.get());
		plant.setLengthFactor(lengthFactor.get());
		plant.setLeafSize(leafSize.get());
		plant.setLeafThicknessFactor(leafThicknessFactor.get());
		plant.setLeafLengthFactor(leafLengthFactor.get());
		plant.setLeafWidthFactor(leafWidthFactor.get());
		plant.setLeafWidthAngle(Math.toRadians(leafWidthAngle.get()));
		plant.setLeafColorCenterOffset(leafColorCenterOffset.get());
		plant.setLeafColor2Offset(leafColor2Offset.get());
		plant.setPetalCount((int)petalCount.get());
		plant.setPetalSize(petalSize.get());
		plant.setPetalThicknessFactor(petalThicknessFactor.get());
		plant.setPetalLengthFactor(petalLengthFactor.get());
		plant.setPetalWidthFactor(petalWidthFactor.get());
		plant.setPetalWidthAngle(Math.toRadians(petalWidthAngle.get()));
		plant.setPetalColorCenterOffset(petalColorCenterOffset.get());
		plant.setPetalColor2Offset(petalColor2Offset.get());
		plant.setFlowerCenterSize(flowerCenterSize.get());
		plant.setSteps((int)steps.get());
		
		plant.setTrunkColor(trunkColor.get());
		plant.setBranchColor(branchColor.get());
		plant.setLeaf1Color(leaf1Color.get());
		plant.setLeaf2Color(leaf2Color.get());
		plant.setPetal1Color(petal1Color.get());
		plant.setPetal2Color(petal2Color.get());
		plant.setFlowerCenterColor(flowerCenterColor.get());

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
