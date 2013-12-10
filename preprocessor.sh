#!/bin/sh
gcc -o preprocess preprocess.c -Wall 
./preprocess $1 $2 $3
