#!/bin/sh

set -euo pipefail

deployManualManifestApply() {
  cd $1
  echo "Deploying $1 with manual manifest oc apply..."
  ./mvnw clean package
  oc apply -f target/kubernetes/openshift.yml
  echo "Deployment of $1 is complete"
  cd ..
}

deployManualManifestApply users-service

deployManualManifestApply reservation-service

deployManualManifestApply rental-service

deployManualManifestApply inventory-service

deployManualManifestApply billing-service

