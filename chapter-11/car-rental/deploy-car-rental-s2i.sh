#!/bin/sh

set -euo pipefail

checkFileExists() {
  if [ ! -e "$1" ]; then
    echo "Cannot find $1. Make sure you are running this script from a correct directory containing all services."
    return 1
  fi
}

deployS2I() {
  cd $1
  echo "Deploying $1 with s2i build..."
  ./mvnw clean package -Dquarkus.profile=prod,s2i -Dquarkus.openshift.deploy=true
  echo "Deployment of $1 is complete"
  cd ..
}

checkFileExists users-service
checkFileExists reservation-service
checkFileExists rental-service
checkFileExists inventory-service
checkFileExists billing-service

deployS2I users-service
deployS2I reservation-service
deployS2I rental-service
deployS2I inventory-service
deployS2I billing-service

