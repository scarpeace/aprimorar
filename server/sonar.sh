#!/usr/bin/env bash

set -euo pipefail

cd "$(dirname "$0")"

if [[ -f .env.local ]]; then
  set -a
  # shellcheck disable=SC1091
  source .env.local
  set +a
elif [[ -f .env ]]; then
  set -a
  # shellcheck disable=SC1091
  source .env
  set +a
fi

: "${SONAR_TOKEN:?SONAR_TOKEN não definido em .env.local ou .env}"
: "${SONAR_PROJECT_KEY:=aprimorar-server}"
: "${SONAR_HOST_URL:=http://localhost:9000}"

./mvnw verify org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970:sonar \
  -Dsonar.host.url="$SONAR_HOST_URL" \
  -Dsonar.token="$SONAR_TOKEN" \
  -Dsonar.projectKey="$SONAR_PROJECT_KEY"
