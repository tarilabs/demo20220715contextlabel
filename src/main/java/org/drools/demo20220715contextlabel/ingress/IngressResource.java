package org.drools.demo20220715contextlabel.ingress;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drools.demo20220715contextlabel.RestConstants;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.cloudevents.CloudEvent;

@Path(RestConstants.INGRESS_PATH)
public class IngressResource {

    @Channel("cecase-out")
    Emitter<CloudEvent> myEmitter; 

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestBody(content={@Content(example = """
            {
                "specversion": "1.0",
                "id": "667e258a-8eb9-43b2-9313-22133f2c717e",
                "source": "example",
                "type": "demo20220715contextlabel.demotype",
                "data": {"host": "basedidati.milano.local", "diskPerc": 70, "memPerc": 50, "cpuPerc": 20}
            }
            """)})
    public Response ingress(CloudEvent event) {
        myEmitter.send(event);
        return Response.ok().build();
    }
}
