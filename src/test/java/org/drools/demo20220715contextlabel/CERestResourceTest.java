package org.drools.demo20220715contextlabel;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class CERestResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
        .when()
            .body("""
                {
                    "specversion": "1.0",
                    "id": "667e258a-8eb9-43b2-9313-22133f2c717e",
                    "source": "example",
                    "type": "demo20220715contextlabel.demotype",
                    "data": { "host" : "milan.archivio" }
                }
                """)
            .contentType(ContentType.JSON)
            .post(RestConstants.PATH)
            .then()
                .statusCode(201)
                .header("location", notNullValue());
    }

}