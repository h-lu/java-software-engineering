#!/usr/bin/env bash
set -euo pipefail

mvn -q package
java -cp target/classes com.campusflow.Main
