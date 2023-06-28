package org.acme.rental.invoice.data;

import io.vertx.core.json.JsonObject;

public class Invoice {

    public boolean paid;
    public JsonObject details;

    @Override
    public String toString() {
        return "Invoice{paid=%s, details=%s}".formatted(paid, details);
    }
}
