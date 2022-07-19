package org.drools.demo20220715contextlabel;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.drools.demo20220715contextlabel.model.CECase;

import io.quarkus.logging.Log;

@Path("/query")
public class CEQueryResource {
    @PersistenceContext
    EntityManager manager;

    @GET
    @Path("/under/{parent}")
    public List<CECase> get(@PathParam("parent") String parent) {
        try {
            return manager.createNamedQuery("getAllByParent", CECase.class)
                .setParameter("parent", parent)
                .getResultList();
        } catch (Exception e) {
            Log.error(e);
            return Collections.emptyList();
        }
    }

    @GET
    @Path("/match/{lquery}")
    public List<CECase> lquery(@PathParam("lquery") String lquery) {
        try {
            return manager.createNamedQuery("getAllByLQuery", CECase.class)
                .setParameter("lquery", lquery)
                .getResultList();
        } catch (Exception e) {
            Log.error(e);
            return Collections.emptyList();
        }
    }
}
