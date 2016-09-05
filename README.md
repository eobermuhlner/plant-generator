# plant-generator

Create plants using a Lindenmayer system.

The goal of this project is to be a proof of concept that it is possible to create natural looking plants for a 3D terrain.

## Overview

The implementation uses a stochastic [Lindenmayer System](https://en.wikipedia.org/wiki/L-system).

The following example shows a grammar for a typical plant.
```
S=TTTP;
P=2:T[-TLP]P,
	2:T[TLP]P,
	3:T[-TLP][+TLP]P,
	1:TP;
```


## Screenshots

![Screenshot Plant](ch.obermuhlner.plantgen/docu/images/plant01.png?raw=true)

![Screenshot Plant](ch.obermuhlner.plantgen/docu/images/plant02.png?raw=true)

![Screenshot Plant](ch.obermuhlner.plantgen/docu/images/plant03.png?raw=true)

![Screenshot Plant](ch.obermuhlner.plantgen/docu/images/plant04.png?raw=true)

![Screenshot Plant](ch.obermuhlner.plantgen/docu/images/plant05.png?raw=true)

![Screenshot Plant](ch.obermuhlner.plantgen/docu/images/plant06.png?raw=true)
