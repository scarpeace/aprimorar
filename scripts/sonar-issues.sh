#!/bin/bash

ENV_FILE="$(dirname "$0")/../.env.local"
if [ -f "$ENV_FILE" ]; then
  source "$ENV_FILE"
fi

SONAR_HOST="http://localhost:9000"
PROJECT_KEY="${1:-aprimorar-server}"
TOKEN="${SONAR_TOKEN}"

if [ -z "$TOKEN" ]; then
  echo "ERROR: SONAR_TOKEN environment variable is not set."
  echo "Run: export SONAR_TOKEN=<your-token>"
  exit 1
fi

echo "Fetching issues for project: $PROJECT_KEY"
echo "============================================"

ISSUES=$(curl -s -u "$TOKEN:" \
  "$SONAR_HOST/api/issues/search?componentKeys=$PROJECT_KEY&types=BUG,CODE_SMELL,VULNERABILITY&ps=500&statuses=OPEN,CONFIRMED,REOPENED" \
  -H "Accept: application/json")

TOTAL=$(echo "$ISSUES" | jq -r '.total')
echo "Total issues: $TOTAL"
echo ""

if [ "$TOTAL" -eq 0 ] 2>/dev/null; then
  echo "No issues found. Great job!"
  exit 0
fi

echo "$ISSUES" | jq -r '.issues[] | 
  "[" + .severity + "] " + .type + " - " + .message + " (" + .component + ":" + (if .textRange then (.textRange.startLine | tostring) else "?" end) + ")"' | sort
