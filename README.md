# Rainforest Robot
A robot is controlled with the following instructions:

* N, S, E, W: the robot moves one unit of distance in the direct​ion specified
(N increases the y coordinate value, E increases the x coordinate value).
* P: pick up one bag of sugar­free gummy bears from a crate.
* D: drop the bags of sugar­free gummy bears that the robot currently has in its
possession onto the conveyor­belt feeder.

A couple of issues with the robot have been found:

* If the robot tries to retrieve a bag from a position where a crate doesn’t
reside, it falls over and short­circuits. From this point onwards it no longer
responds to instructions.
* If the robot tries to drop bags off at a position that is any place other than
the conveyor­belt feeder, the bags get caught in its wheels and it short­circuits.
In this instance it also no longer responds to instructions.

This application takes in the following input:

1. The x, y coordinates of the position of the conveyor­belt feeder
2. The x, y coordinates of the start position of the robot
3. comma separated descriptions of the crates. Each crate has an x coord, y
coord and quantity.
4. A set of instructions for the robot to perform.

The application responds with the total number of bags dropped on the
conveyor­belt feeder, and the final position and health of the robot
(either 'still functioning' or 'short circuited')

<b>Note</b>: Since the input fields are updates to reflect the new state on submission, one can enter instructions in either one go e.g PPN or sequentially e.g. P followed by P followed by N.

### Further Assumptions
The robot does not short-circuit if it attempts to pick up a bag from an empty crate.
The robot does not short-circuit if it attempts to drop bags on the conveyor belt when it is not carrying any bags.
When the robot short circuits, it does not drop its bags.
Once short circuited, the robot can be reset by submitting the form again.
