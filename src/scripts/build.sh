#!/bin/sh

cd ../../

mvn clean install

rmdir bin

mkdir bin

cp  -v target/* bin

