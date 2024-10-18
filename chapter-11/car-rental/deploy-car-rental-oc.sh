#!/bin/sh

set -euo pipefail

checkFileExists() {
  if [ ! -e "$1" ]; then
    echo "Cannot find $1. Make sure you are running this script from a correct directory containing all services."
    return 1
  fi
}

deployManualManifestApply() {
  cd $1
  echo "Deploying $1 with manual manifest oc apply..."
  ./mvnw clean package
  oc apply -f target/kubernetes/openshift.yml
  echo "Deployment of $1 is complete"
  cd ..
}

checkFileExists users-service
checkFileExists reservation-service
checkFileExists rental-service
checkFileExists inventory-service
checkFileExists billing-service

deployManualManifestApply users-service
deployManualManifestApply reservation-service
deployManualManifestApply rental-service
deployManualManifestApply inventory-service
deployManualManifestApply billing-service
