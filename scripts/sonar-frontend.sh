#!/bin/bash

ENV_FILE="$(dirname "$0")/../.env.local"
if [ -f "$ENV_FILE" ]; then
  source "$ENV_FILE"
fi
cd "$(dirname "$0")/../client"
docker run --rm --network=host \
  -e SONAR_HOST_URL=http://localhost:9000 \
  -e SONAR_TOKEN=$SONAR_TOKEN \
  -v "$(pwd):/usr/src" \
  sonarsource/sonar-scanner-cli
