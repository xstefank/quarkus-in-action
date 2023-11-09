package org.acme.metadata.extension.test;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;

import static org.hamcrest.Matchers.*;

public class MetadataExtensionTest {

    // Start unit test with your extension loaded
    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
        .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

    @Test
    public void writeYourOwnUnitTest() {
        RestAssured.when().get("/metadata")
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("name", equalTo("metadata-extension-deployment"))
                .body("version", equalTo("1.0.0-SNAPSHOT"))
                .body("dependencies", not(emptyArray()));
    }
}
