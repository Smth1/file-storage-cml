#!/bin/sh

cd ../../

mvn clean install

rm -rf bin

mkdir bin

cp  -v target/* bin

