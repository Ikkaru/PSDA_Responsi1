@echo off
REM Maven Wrapper (simple): download wrapper jar if missing, then run it
set MAVEN_WRAPPER_JAR=.mvn\wrapper\maven-wrapper.jar
set MAVEN_WRAPPER_URL=https://repo1.maven.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar

if not exist "%MAVEN_WRAPPER_JAR%" (
  echo Downloading maven wrapper jar...
  if not exist .mvn\wrapper mkdir .mvn\wrapper
  powershell -Command "try { (New-Object System.Net.WebClient).DownloadFile('%MAVEN_WRAPPER_URL%', '%MAVEN_WRAPPER_JAR%') } catch { exit 1 }"
  if %ERRORLEVEL% NEQ 0 (
    echo Failed to download maven wrapper jar. Make sure PowerShell is available.
    exit /b 1
  )
)

java -jar "%MAVEN_WRAPPER_JAR%" %*
