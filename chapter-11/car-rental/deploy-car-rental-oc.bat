@echo off
setlocal enabledelayedexpansion

:: Main script starts here
call :checkFileExists users-service
call :checkFileExists reservation-service
call :checkFileExists rental-service
call :checkFileExists inventory-service
call :checkFileExists billing-service

call :deployManualManifestApply users-service
call :deployManualManifestApply reservation-service
call :deployManualManifestApply rental-service
call :deployManualManifestApply inventory-service
call :deployManualManifestApply billing-service

:: Function to check if the file or directory exists
:checkFileExists
if not exist "%~1" (
    echo Cannot find %~1. Make sure you are running this script from the correct directory containing all services.
    exit /b 1
)
exit /b 0

:: Function to deploy using Maven and apply OpenShift manifests
:deployManualManifestApply
cd %~1
echo Deploying %~1 with manual manifest oc apply...
call mvnw.cmd clean package
oc apply -f target\kubernetes\openshift.yml
echo Deployment of %~1 is complete
cd ..
exit /b 0
