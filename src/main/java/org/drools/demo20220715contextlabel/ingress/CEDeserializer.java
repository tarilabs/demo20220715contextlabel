package org.drools.demo20220715contextlabel.ingress;

import io.cloudevents.CloudEvent;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class CEDeserializer extends ObjectMapperDeserializer<CloudEvent> {
    public CEDeserializer() {
        super(CloudEvent.class);
    }
}