package org.drools.demo20220715contextlabel;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drools.demo20220715contextlabel.model.CECase;
import org.drools.demo20220715contextlabel.model.CECaseRepository;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cloudevents.CloudEvent;

@Path(RestConstants.PATH)
@Transactional
public class CERestResource {

    @Inject
    ObjectMapper mapper;

    @Inject
    CECaseRepository panacheRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestBody(content={@Content(example = """
            {
                "specversion": "1.0",
                "id": "667e258a-8eb9-43b2-9313-22133f2c717e",
                "source": "example",
                "type": "demo20220715contextlabel.demotype",
                "data": { "host" : "milan.archivio" }
            }
            """)})
    public Response hello(CloudEvent event) throws Exception {
        String jsonString = new String(event.getData().toBytes());
        JsonNode node = mapper.readTree(jsonString);
        System.out.println(node);
        var cecase = new CECase();
        cecase.setCeuuid(event.getId());
        cecase.setContext(node);
        cecase.setMytag(new String[]{"location.eu.italy.milan", "type.db"});
        panacheRepository.persist(cecase);
        return Response.created(URI.create(RestConstants.PATH + "/" + cecase.getId())).build();
    }

    @GET
    public List<CECase> list() {
        return panacheRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public CECase get(@PathParam("id") Long id) {
        return panacheRepository.findById(id);
    }
}
