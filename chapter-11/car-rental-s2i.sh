#!/bin/sh

set -euo pipefail

deployS2I() {
  cd $1
  echo "Deploying $1 with s2i build..."
  ./mvnw clean package -Dquarkus.profile=prod,s2i -Dquarkus.openshift.deploy=true
  echo "Deployment of $1 is complete"
  cd ..
}

deployS2I users-service

deployS2I reservation-service

deployS2I rental-service

deployS2I inventory-service

deployS2I billing-service

