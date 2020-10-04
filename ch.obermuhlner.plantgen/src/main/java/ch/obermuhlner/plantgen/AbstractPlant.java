package ch.obermuhlner.plantgen;

import ch.obermuhlner.plantgen.lsystem.LindenmayerSystem;
import ch.obermuhlner.plantgen.ui.turtle.TurtleGraphic;
import ch.obermuhlner.plantgen.ui.turtle.command.AngleCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.CompositeCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.FlowerCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.LeafCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.LengthFactorCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.PopCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.PushCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.RandomChangeAngleCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.RandomForwardCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.RandomTurnCommand;
import ch.obermuhlner.plantgen.ui.turtle.command.ThicknessFactorCommand;
import javafx.scene.paint.Color;

import java.util.Random;

public abstract class AbstractPlant {

  protected Random random;

  private double turnAngle = Math.toRadians(45);
  private double standardDeviation = 0.0;
  private double initialThickness = 10.0;
  private double initialLength = 40.0;
  private double lengthFactor = 1.0;
  private double leafSize = 1.0;
  private double leafThicknessFactor = 2.0;
  private double leafLengthFactor = 2.0;
  private double leafWidthFactor = 2.0;
  private double leafWidthAngle = Math.PI / 4;
  private double leafColorCenterOffset = 0.1;
  private double leafColor2Offset = 0.5;
  private int steps = 5;
  private int petalCount = 6;
  private double petalSize = 1.0;
  private double petalThicknessFactor = 2.0;
  private double petalLengthFactor = 2.0;
  private double petalWidthFactor = 2.0;
  private double petalWidthAngle = Math.PI / 4;
  private double petalColorCenterOffset = 0.1;
  private double petalColor2Offset = 0.5;
  private double flowerCenterSize = 5;

  private Color trunkColor = Color.BROWN.darker();
  private Color branchColor = Color.BROWN;
  private Color leaf1Color = Color.rgb(0, 255, 0, 0.5);
  private Color leaf2Color = leaf1Color.darker();
  private Color petal1Color = Color.rgb(255, 0, 0, 0.9);
  private Color petal2Color = Color.rgb(0, 0, 255, 0.5);
  private Color flowerCenterColor = Color.YELLOW;

  public AbstractPlant(Random random) {
    this.random = random;
  }

  public void setTurnAngle(double turnAngle) {
    this.turnAngle = turnAngle;
  }

  public void setStandardDeviation(double standardDeviation) {
    this.standardDeviation = standardDeviation;
  }

  public void setInitialThickness(double initialThickness) {
    this.initialThickness = initialThickness;
  }

  public void setInitialLength(double initialLength) {
    this.initialLength = initialLength;
  }

  public void setLengthFactor(double lengthFactor) {
    this.lengthFactor = lengthFactor;
  }

  public void setLeafSize(double leafSize) {
    this.leafSize = leafSize;
  }

  public void setLeafThicknessFactor(double leafThicknessFactor) {
    this.leafThicknessFactor = leafThicknessFactor;
  }

  public void setLeafLengthFactor(double leafLengthFactor) {
    this.leafLengthFactor = leafLengthFactor;
  }

  public void setLeafWidthFactor(double leafWidthFactor) {
    this.leafWidthFactor = leafWidthFactor;
  }

  public void setLeafWidthAngle(double leafWidthAngle) {
    this.leafWidthAngle = leafWidthAngle;
  }

  public void setLeafColorCenterOffset(double leafColorCenterOffset) {
    this.leafColorCenterOffset = leafColorCenterOffset;
  }

  public void setLeafColor2Offset(double leafColor2Offset) {
    this.leafColor2Offset = leafColor2Offset;
  }

  public void setPetalCount(int petalCount) {
    this.petalCount = petalCount;
  }

  public void setPetalSize(double petalSize) {
    this.petalSize = petalSize;
  }

  public void setPetalThicknessFactor(double petalThicknessFactor) {
    this.petalThicknessFactor = petalThicknessFactor;
  }

  public void setPetalLengthFactor(double petalLengthFactor) {
    this.petalLengthFactor = petalLengthFactor;
  }

  public void setPetalWidthFactor(double petalWidthFactor) {
    this.petalWidthFactor = petalWidthFactor;
  }

  public void setPetalWidthAngle(double petalWidthAngle) {
    this.petalWidthAngle = petalWidthAngle;
  }

  public void setPetalColorCenterOffset(double petalColorCenterOffset) {
    this.petalColorCenterOffset = petalColorCenterOffset;
  }

  public void setPetalColor2Offset(double petalColor2Offset) {
    this.petalColor2Offset = petalColor2Offset;
  }

  public void setFlowerCenterSize(double flowerCenterSize) {
    this.flowerCenterSize = flowerCenterSize;
  }

  public void setSteps(int steps) {
    this.steps = steps;
  }

  public void setTrunkColor(Color trunkColor) {
    this.trunkColor = trunkColor;
  }

  public void setBranchColor(Color branchColor) {
    this.branchColor = branchColor;
  }

  public void setLeaf1Color(Color leaf1Color) {
    this.leaf1Color = leaf1Color;
  }

  public void setLeaf2Color(Color leaf2Color) {
    this.leaf2Color = leaf2Color;
  }

  public void setPetal1Color(Color petal1Color) {
    this.petal1Color = petal1Color;
  }

  public void setPetal2Color(Color petal2Color) {
    this.petal2Color = petal2Color;
  }

  public void setFlowerCenterColor(Color flowerCenterColor) {
    this.flowerCenterColor = flowerCenterColor;
  }

  private String createDescription() {
    LindenmayerSystem lindenmayerSystem = new LindenmayerSystem(random);

    String description = initialize(lindenmayerSystem);
    for (int i = 0; i < steps; i++) {
      description = lindenmayerSystem.expand(description);
    }

    return description;
  }

  protected abstract String initialize(LindenmayerSystem lindenmayerSystem);

  public void initialize(TurtleGraphic turtleGraphic) {
    turtleGraphic.getState().thickness = initialThickness;
    turtleGraphic.getState().length = initialLength;
    turtleGraphic.getState().update();

    turtleGraphic.addCommand('[', new PushCommand());
    turtleGraphic.addCommand(']', new PopCommand());
    turtleGraphic.addCommand('u', new AngleCommand(Math.toRadians(270)));
    turtleGraphic.addCommand('d', new AngleCommand(Math.toRadians(90)));
    turtleGraphic.addCommand('l', new AngleCommand(Math.toRadians(180)));
    turtleGraphic.addCommand('r', new AngleCommand(Math.toRadians(0)));
    turtleGraphic.addCommand('-', new RandomTurnCommand(random, -turnAngle, standardDeviation));
    turtleGraphic.addCommand('+', new RandomTurnCommand(random, turnAngle, standardDeviation));
    turtleGraphic.addCommand('~', new RandomChangeAngleCommand(random, turnAngle, standardDeviation));
    turtleGraphic.addCommand('T', new CompositeCommand(
        new ThicknessFactorCommand(0.9),
        new LengthFactorCommand(0.95),
        new RandomForwardCommand(random, trunkColor, lengthFactor, standardDeviation)
    ));
    turtleGraphic.addCommand('t', new CompositeCommand(
        new ThicknessFactorCommand(0.5),
        new LengthFactorCommand(0.9),
        new RandomForwardCommand(random, branchColor, lengthFactor, standardDeviation)
    ));
    turtleGraphic.addCommand('L', new CompositeCommand(
        new LeafCommand(
            leaf1Color,
            leaf2Color,
            leafColorCenterOffset,
            leafColor2Offset,
            leafSize,
            leafThicknessFactor,
            leafLengthFactor,
            leafWidthFactor,
            leafWidthAngle
        )));
    turtleGraphic.addCommand('F', new CompositeCommand(
        new FlowerCommand(
            petalCount,
            new LeafCommand(petal1Color,
                petal2Color,
                petalColorCenterOffset,
                petalColor2Offset,
                petalSize,
                petalThicknessFactor,
                petalLengthFactor,
                petalWidthFactor,
                petalWidthAngle
            ),
            flowerCenterSize,
            flowerCenterColor
        )));
  }

  public String getDescription() {
    return createDescription();
  }
}
