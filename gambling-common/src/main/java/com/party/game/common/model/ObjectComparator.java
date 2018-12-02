package com.party.game.common.model;

import java.io.Serializable;

/**
 * @author wenqiang.luo date:15-11-9
 */
public class ObjectComparator<T> implements Serializable {

    private static final long serialVersionUID = 7948779553410711156L;

    private T objectOrigin;

    private T objectUpdated;

    /* construct */

    public ObjectComparator(T objectOrigin, T objectUpdated) {
        this.objectOrigin = objectOrigin;
        this.objectUpdated = objectUpdated;
    }

    /* get and set */

    public Object getObjectOrigin() {
        return objectOrigin;
    }

    public void setObjectOrigin(T objectOrigin) {
        this.objectOrigin = objectOrigin;
    }

    public Object getObjectUpdated() {
        return objectUpdated;
    }

    public void setObjectUpdated(T objectUpdated) {
        this.objectUpdated = objectUpdated;
    }
}
