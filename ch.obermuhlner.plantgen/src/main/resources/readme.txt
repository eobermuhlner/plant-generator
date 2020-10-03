The script language consists of simple rules:

```
start "=" {command} ";"
```

The rules are executed repeatedly, replacing the start symbol with the commands to the right of the colon.

To make things more interesting stochastic rules are also possible with different probabilities for different variations.

```
start "=" probability : {command} { "," probability : {command} } ";"
```


The following commands are available:
- `S`	The start seed.
- `[`	Pushes the current state on the stack.
- `]`	Pops the current state from the stack (essentially restoring the state to what it was at the last [ command).
- `u`	Sets the growth angle up-wards.
- `d`	Sets the growth angle down-wards.
- `l`	Sets the growth angle horizontally left-wards.
- `r`	Sets the growth angle horizontally right-wards.
- `-`	Random angle a bit to the left.
- `+`	Random angle a bit to the right.
- `~`	Random angle.
- `T`	Grows a bit of trunk.
- `t`	Grows a bit of a thinner trunk.
- `L`	Grows a leaf.
- `f` Grows a flower.

