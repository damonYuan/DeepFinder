README
====

A file scanner to deep search strings inside a file / Zip file / folder.

# Build

`mvn clean package`

# Usage

`java -jar deepfinder.jar <PATH> <String1> <String2> ...`

eg.,
```
# java -jar target/deepfinder-1.0-SNAPSHOT.jar target/deepfinder-1.0-SNAPSHOT.jar  deepfinder

Scanning path is: /Users/damon/Desktop/deepfinder/target/deepfinder-1.0-SNAPSHOT.jar
Strings to find are: [deepfinder]
/Users/damon/Desktop/deepfinder/target/deepfinder-1.0-SNAPSHOT.jar$META-INF/MANIFEST.MF contains deepfinder
```