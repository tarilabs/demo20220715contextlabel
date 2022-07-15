package org.drools.demo20220715contextlabel.jpa;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.type.descriptor.WrapperOptions;

import com.vladmihalcea.hibernate.type.array.internal.AbstractArrayTypeDescriptor;

public class LTreeArrayTypeDescriptor extends AbstractArrayTypeDescriptor<String[]> {

    public LTreeArrayTypeDescriptor() {
        super(String[].class);
    }

    @Override
    protected String getSqlArrayType() {
        return "ltree";
    }

    // TODO: maybe check if this is an appropriate impl 😅
    @Override
    public <X> String[] wrap(X value, WrapperOptions options) {
        if (value instanceof Array) {
            Array array = (Array) value;
            try {
                int length = ((Object[]) array.getArray()).length;
                String[] result = new String[length];
                ResultSet rs = array.getResultSet();
                for (int i = 0; i < length; i++) {
                    rs.next();
                    result[i] = rs.getString(2);
                }
                rs.close();
                return result;
            } catch (SQLException e) {
                throw new HibernateException(
                    new IllegalArgumentException(e)
                );
            }
        }
        return super.wrap(value, options);
    }    
}

