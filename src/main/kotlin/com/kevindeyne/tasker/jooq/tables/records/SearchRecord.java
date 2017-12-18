/*
 * This file is generated by jOOQ.
*/
package com.kevindeyne.tasker.jooq.tables.records;


import com.kevindeyne.tasker.jooq.tables.Search;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SearchRecord extends UpdatableRecordImpl<SearchRecord> implements Record6<Long, Long, String, String, String, Long> {

    private static final long serialVersionUID = -1056949896;

    /**
     * Setter for <code>taskr.search.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>taskr.search.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>taskr.search.project_id</code>.
     */
    public void setProjectId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>taskr.search.project_id</code>.
     */
    public Long getProjectId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>taskr.search.type</code>.
     */
    public void setType(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>taskr.search.type</code>.
     */
    public String getType() {
        return (String) get(2);
    }

    /**
     * Setter for <code>taskr.search.srcval</code>.
     */
    public void setSrcval(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>taskr.search.srcval</code>.
     */
    public String getSrcval() {
        return (String) get(3);
    }

    /**
     * Setter for <code>taskr.search.name</code>.
     */
    public void setName(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>taskr.search.name</code>.
     */
    public String getName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>taskr.search.linked_id</code>.
     */
    public void setLinkedId(Long value) {
        set(5, value);
    }

    /**
     * Getter for <code>taskr.search.linked_id</code>.
     */
    public Long getLinkedId() {
        return (Long) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Long, Long, String, String, String, Long> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Long, Long, String, String, String, Long> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Search.SEARCH.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return Search.SEARCH.PROJECT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Search.SEARCH.TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Search.SEARCH.SRCVAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Search.SEARCH.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field6() {
        return Search.SEARCH.LINKED_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component2() {
        return getProjectId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getSrcval();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component6() {
        return getLinkedId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value2() {
        return getProjectId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getSrcval();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value6() {
        return getLinkedId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchRecord value2(Long value) {
        setProjectId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchRecord value3(String value) {
        setType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchRecord value4(String value) {
        setSrcval(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchRecord value5(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchRecord value6(Long value) {
        setLinkedId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchRecord values(Long value1, Long value2, String value3, String value4, String value5, Long value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SearchRecord
     */
    public SearchRecord() {
        super(Search.SEARCH);
    }

    /**
     * Create a detached, initialised SearchRecord
     */
    public SearchRecord(Long id, Long projectId, String type, String srcval, String name, Long linkedId) {
        super(Search.SEARCH);

        set(0, id);
        set(1, projectId);
        set(2, type);
        set(3, srcval);
        set(4, name);
        set(5, linkedId);
    }
}
