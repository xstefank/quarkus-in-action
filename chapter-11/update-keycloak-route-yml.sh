#!/bin/sh

set -euo pipefail

# this will fail the script if the route is not found
KEYCLOAK_ROUTE=$(oc get route keycloak -o jsonpath="{.spec.host}")

sed -i "s#value: http://keycloak-.*/realms/car-rental#value: http://$KEYCLOAK_ROUTE/realms/car-rental#g" ./car-rental.yml
