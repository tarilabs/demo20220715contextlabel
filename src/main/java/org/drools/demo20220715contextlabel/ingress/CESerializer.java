package org.drools.demo20220715contextlabel.ingress;

import io.cloudevents.CloudEvent;
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;

public class CESerializer extends ObjectMapperSerializer<CloudEvent> {
    public CESerializer() {
        super();
    }
}