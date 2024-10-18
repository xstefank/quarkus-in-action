@echo off
setlocal enabledelayedexpansion
set "MAVEN_OPTS=-Dquarkus.profile=prod,s2i -Dquarkus.openshift.deploy=true"

:: Main script starts here
call :checkFileExists users-service
call :checkFileExists reservation-service
call :checkFileExists rental-service
call :checkFileExists inventory-service
call :checkFileExists billing-service

call :deployS2I users-service
call :deployS2I reservation-service
call :deployS2I rental-service
call :deployS2I inventory-service
call :deployS2I billing-service

:: Function to check if the file or directory exists
:checkFileExists
if not exist "%~1" (
    echo Cannot find %~1. Make sure you are running this script from the correct directory containing all services.
    exit /b 1
)
exit /b 0

:: Function to deploy using Maven
:deployS2I
cd %~1
echo Deploying %~1 with s2i build...
call mvnw.cmd clean package %MAVEN_OPTS%
echo Deployment of %~1 is complete
cd ..
exit /b 0
