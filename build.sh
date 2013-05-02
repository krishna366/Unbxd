#!/bin/sh

rm -rf dist
mkdir dist
javac -cp ".:src/" -d "dist" src/Unbxd.java

cp run.sh dist/
