package org.drools.demo20220715contextlabel.jpa;

import com.vladmihalcea.hibernate.type.array.internal.AbstractArrayType;

public class LTreeArrayType extends AbstractArrayType<String[]> {

    public static final LTreeArrayType INSTANCE = new LTreeArrayType();

    public LTreeArrayType() {
        super(
            new LTreeArrayTypeDescriptor()
        );
    }

    public String getName() {
        return "ltree-array";
    }
}
