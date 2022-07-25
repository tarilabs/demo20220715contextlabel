package org.drools.demo20220715contextlabel.ingress;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.drools.demo20220715contextlabel.CERestResource;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cloudevents.CloudEvent;
import io.smallrye.reactive.messaging.annotations.Blocking;

@ApplicationScoped
public class IngressProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(IngressProcessor.class);

    @Inject
    CERestResource ceResource;

    @Incoming("cecase-in") 
    @Blocking             
    public void process(CloudEvent quoteRequest) throws Exception {
        LOG.info("Processing: {}", quoteRequest);
        ceResource.hello(quoteRequest);
        LOG.info("Processed: {}", quoteRequest);
    }
}

