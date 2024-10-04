#!/bin/bash

#
# /**
# *@Author Akbar Riyan Nugroho
# */
#

echo "Deploy Run .."
# echo "Clean Install .."
# mvn clean package -DskipTests
echo "Build .."
docker build -t arnugroho/fraud_bfs .
echo "stop .."
docker stop fraud_bfs
echo "remove .."
docker rm fraud_bfs
echo "docker run .."

docker run -d -it --restart --name=fraud_bfs -p 34234:8080  arnugroho/fraud_bfs

