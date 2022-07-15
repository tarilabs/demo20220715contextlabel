package org.drools.demo20220715contextlabel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.JsonNode;

@Entity
@NamedNativeQueries({
    @NamedNativeQuery(
        name = "getAllByParent",
        query = "SELECT * FROM cecase WHERE mytag <@ text2ltree(:parent)",
        resultClass = CECase.class
    ),
    @NamedNativeQuery(
        name = "getAllByLQuery",
        query = "SELECT * FROM cecase WHERE mytag ~ CAST( :lquery AS lquery )",
        resultClass = CECase.class
    ),
})
public class CECase {
    @Id @GeneratedValue private Long id;
    
    private String ceuuid;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private JsonNode context;

    @Column(columnDefinition = "ltree[]")
    @Type(type = "LTreeType-array")
    private String[] mytag;

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getCeuuid() {
        return ceuuid;
    }
    public void setCeuuid(String ceuuid) {
        this.ceuuid = ceuuid;
    }
    public JsonNode getContext() {
        return context;
    }
    public void setContext(JsonNode context) {
        this.context = context;
    }
    public String[] getMytag() {
        return mytag;
    }
    public void setMytag(String[] mytag) {
        this.mytag = mytag;
    }

}