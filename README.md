AgentSim
========

### Requirements
* Java 7 or higher
* maven, only tested with version 3

### How to run on Linux
* Execute `run.sh` to update, compile and execute the program

### Goal
Simulate a small goblin population in a cave.
The population should slowly build up and produce resources.
The goblins should be independent agents that only communicate when they meet.
For important information, a ranged communication may be implemented.
Agents all have their own set of intends they need to achieve, each of which has its own age.
This way, intends can be exchanged by merging and keeping the one with the newest age.
The same goes for map information.

### Understanding the code
If you want to read the code, you should start with the [AgentSim](https://github.com/ISibboI/AgentSim/blob/master/src/main/java/de/isibboi/agentsim/AgentSim.java) class. It contains the main method.
Of course, I should add some more information here, but I probably won't until someone requests it.
