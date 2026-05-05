#!/usr/bin/env sh
set -e
MAVEN_WRAPPER_JAR=.mvn/wrapper/maven-wrapper.jar
MAVEN_WRAPPER_URL="https://repo1.maven.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar"

if [ ! -f "$MAVEN_WRAPPER_JAR" ]; then
  echo "Downloading maven wrapper jar..."
  mkdir -p .mvn/wrapper
  if command -v curl >/dev/null 2>&1; then
    curl -s -f -L -o "$MAVEN_WRAPPER_JAR" "$MAVEN_WRAPPER_URL"
  elif command -v wget >/dev/null 2>&1; then
    wget -q -O "$MAVEN_WRAPPER_JAR" "$MAVEN_WRAPPER_URL"
  else
    echo "curl or wget is required to download the maven wrapper jar"
    exit 1
  fi
fi

exec java -jar "$MAVEN_WRAPPER_JAR" "$@"
