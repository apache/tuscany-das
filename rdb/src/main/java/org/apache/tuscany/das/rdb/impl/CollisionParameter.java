package org.apache.tuscany.das.rdb.impl;

public class CollisionParameter extends ParameterImpl {

    private boolean isSet;

    public void setValue(Object value) {
        if (!isSet) {
            this.value = value;
            isSet = true;
        }
    }
}
