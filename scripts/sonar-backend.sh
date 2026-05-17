#!/bin/bash

ENV_FILE="$(dirname "$0")/../.env.local"
if [ -f "$ENV_FILE" ]; then
  source "$ENV_FILE"
fi

cd "$(dirname "$0")/../server"
./mvnw clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=$SONAR_TOKEN \
  -Dsonar.projectKey=aprimorar-server \
  -Dsonar.projectName="Aprimorar Server"
