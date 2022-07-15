package org.drools.demo20220715contextlabel.model;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class CECaseRepository implements PanacheRepository<CECase> {

   // put your custom logic here as instance methods
}
