#!/bin/bash
mvn clean package
mv target/forum-1.0.0.jar output/forum.jar
cp src/main/resources/application_out.yml output/application.yml
cd output
docker build -f Dockerfile -t goodboy95/k8s-test:0.0.2 .
