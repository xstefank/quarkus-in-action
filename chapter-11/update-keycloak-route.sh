#!/bin/sh

set -euo pipefail

# this will fail the script if the route is not found
KEYCLOAK_ROUTE=$(oc get route keycloak -o jsonpath="{.spec.host}")

sed -i "s#quarkus.openshift.env.vars.quarkus-oidc-auth-server-url=.*#quarkus.openshift.env.vars.quarkus-oidc-auth-server-url=http://$KEYCLOAK_ROUTE/realms/car-rental#" src/main/resources/application.properties

