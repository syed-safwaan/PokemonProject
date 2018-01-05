#!/bin/bash

javac *.java
echo "Main-Class: PokemonArena" > manifest.txt
jar cfm PokemonArena.jar manifest.txt *.class *.txt
rm *.class
