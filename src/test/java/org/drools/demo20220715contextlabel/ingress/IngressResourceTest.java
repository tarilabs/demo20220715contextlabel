package org.drools.demo20220715contextlabel.ingress;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.drools.demo20220715contextlabel.RestConstants;
import org.drools.demo20220715contextlabel.model.CECase;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

@QuarkusTest
public class IngressResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
        .when()
            .body("""
                {
                    "specversion": "1.0",
                    "id": "mm667e258a-8eb9-43b2-9313-22133f2c717e",
                    "source": "example",
                    "type": "demo20220715contextlabel.demotype",
                    "data": {"host": "basedidati.milano.local", "diskPerc": 70, "memPerc": 50, "cpuPerc": 20}
                }
                """)
            .contentType(ContentType.JSON)
            .post(RestConstants.INGRESS_PATH)
            .then()
                .statusCode(200);

        await()
        .atMost(10, TimeUnit.SECONDS)
        .pollDelay(2, TimeUnit.SECONDS)
        .untilAsserted(() -> {
            List<CECase> cases = given()
                .when()
                .get(RestConstants.PATH)
                .then()
                    .statusCode(200)
                    .extract().as(new TypeRef<List<CECase>>() {});
            assertThat(cases)
                .filteredOn(ce -> ce.getCeuuid().equals("mm667e258a-8eb9-43b2-9313-22133f2c717e"))
                .hasSize(1)
                .first()
                .extracting(ce -> ce.getMytag(), Assertions.as(InstanceOfAssertFactories.ARRAY))
                .hasSizeGreaterThan(0);
        });
    }
}