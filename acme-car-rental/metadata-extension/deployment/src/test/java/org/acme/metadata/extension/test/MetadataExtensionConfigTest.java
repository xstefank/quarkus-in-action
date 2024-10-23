package org.acme.metadata.extension.test;

import io.quarkus.test.QuarkusUnitTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class MetadataExtensionConfigTest {

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
        .overrideConfigKey("quarkus.metadata.path", "/some-other-path");

    @Test
    public void testMetadataConfigPathChange() {
        RestAssured.when().get("/some-other-path")
            .prettyPeek()
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("name", equalTo("metadata-extension-deployment"))
            .body("version", equalTo("1.0.0-SNAPSHOT"))
            .body("dependencies", not(emptyArray()));
    }
}
