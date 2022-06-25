# Music Player

Simple music player Java application made using JavaFX.


## Remarks
When using Maven's jlink will return with error, because the org.json dependency does not have module-info inside the jar. In order to fix this you have to inject module-info into said jar. How to do it can be found [here](https://stackoverflow.com/questions/47222226/how-to-inject-module-declaration-into-jar#comment82872402_47222302).
