#!/bin/sh
javac NeuralNetwork.java Network.java DataSet.java Node.java
java NeuralNetwork $1 $2 $4 $5 > $3
