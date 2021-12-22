package com.seekerhut.model.mysqlModel;

import java.io.Serializable;

/**
 * relation
 * @author 
 */
public class Relation implements Serializable {
    private Long relationId;

    private Long relationFrom;

    private Long relationTo;

    private static final long serialVersionUID = 1L;

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public Long getRelationFrom() {
        return relationFrom;
    }

    public void setRelationFrom(Long relationFrom) {
        this.relationFrom = relationFrom;
    }

    public Long getRelationTo() {
        return relationTo;
    }

    public void setRelationTo(Long relationTo) {
        this.relationTo = relationTo;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Relation other = (Relation) that;
        return (this.getRelationId() == null ? other.getRelationId() == null : this.getRelationId().equals(other.getRelationId()))
            && (this.getRelationFrom() == null ? other.getRelationFrom() == null : this.getRelationFrom().equals(other.getRelationFrom()))
            && (this.getRelationTo() == null ? other.getRelationTo() == null : this.getRelationTo().equals(other.getRelationTo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRelationId() == null) ? 0 : getRelationId().hashCode());
        result = prime * result + ((getRelationFrom() == null) ? 0 : getRelationFrom().hashCode());
        result = prime * result + ((getRelationTo() == null) ? 0 : getRelationTo().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", relationId=").append(relationId);
        sb.append(", relationFrom=").append(relationFrom);
        sb.append(", relationTo=").append(relationTo);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}