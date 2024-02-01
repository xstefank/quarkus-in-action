#!/bin/sh

set -euo pipefail

# this will fail the script if the route is not found
KEYCLOAK_ROUTE=$(oc get route keycloak -o jsonpath="{.spec.host}") || true

if [ -z "$KEYCLOAK_ROUTE" ]
then
  echo "Couldn't find keycloak route with 'oc get route'. Make sure to first deploy dependent services with 'oc apply -f openshift-dependent-services.yml'. This script didn't modify application.properties."
  return 0
fi

sed -i "s#quarkus.openshift.env.vars.quarkus-oidc-auth-server-url=http://<your-keycloak-route>/realms/car-rental#quarkus.openshift.env.vars.quarkus-oidc-auth-server-url=http://$KEYCLOAK_ROUTE/realms/car-rental#" src/main/resources/application.properties

