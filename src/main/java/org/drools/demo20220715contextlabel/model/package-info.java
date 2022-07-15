@TypeDefs({
    @TypeDef(
        name = "json", typeClass = JsonType.class
    ),
    @TypeDef(
        name = "LTreeType", typeClass = org.drools.demo20220715contextlabel.jpa.LTreeType.class
    ),
    @TypeDef(
        name = "LTreeType-array", typeClass = org.drools.demo20220715contextlabel.jpa.LTreeArrayType.class
    )
})
package org.drools.demo20220715contextlabel.model;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.json.JsonType;