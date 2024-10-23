package org.acme.metadata.extension.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class MetadataExtensionResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/metadata-extension")
                .then()
                .statusCode(200)
                .body(is("Hello metadata-extension"));
    }
}
