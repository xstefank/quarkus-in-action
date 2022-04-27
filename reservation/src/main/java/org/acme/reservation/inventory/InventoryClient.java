package org.acme.reservation.inventory;

import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;

@GraphQLClientApi(configKey = "inventory")
public interface InventoryClient {
}
