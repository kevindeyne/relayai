/*
 * This file is generated by jOOQ.
*/
package com.kevindeyne.tasker.jooq.tables;


import com.kevindeyne.tasker.jooq.Indexes;
import com.kevindeyne.tasker.jooq.Keys;
import com.kevindeyne.tasker.jooq.Taskr;
import com.kevindeyne.tasker.jooq.tables.records.ProjectRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Project extends TableImpl<ProjectRecord> {

    private static final long serialVersionUID = 85147153;

    /**
     * The reference instance of <code>taskr.project</code>
     */
    public static final Project PROJECT = new Project();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProjectRecord> getRecordType() {
        return ProjectRecord.class;
    }

    /**
     * The column <code>taskr.project.id</code>.
     */
    public final TableField<ProjectRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>taskr.project.title</code>.
     */
    public final TableField<ProjectRecord, String> TITLE = createField("title", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>taskr.project.key</code>.
     */
    public final TableField<ProjectRecord, String> KEY = createField("key", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>taskr.project.active_sprint_id</code>.
     */
    public final TableField<ProjectRecord, Long> ACTIVE_SPRINT_ID = createField("active_sprint_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("-1", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>taskr.project.sprint_length</code>.
     */
    public final TableField<ProjectRecord, Integer> SPRINT_LENGTH = createField("sprint_length", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("2", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * Create a <code>taskr.project</code> table reference
     */
    public Project() {
        this(DSL.name("project"), null);
    }

    /**
     * Create an aliased <code>taskr.project</code> table reference
     */
    public Project(String alias) {
        this(DSL.name(alias), PROJECT);
    }

    /**
     * Create an aliased <code>taskr.project</code> table reference
     */
    public Project(Name alias) {
        this(alias, PROJECT);
    }

    private Project(Name alias, Table<ProjectRecord> aliased) {
        this(alias, aliased, null);
    }

    private Project(Name alias, Table<ProjectRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Taskr.TASKR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PROJECT_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<ProjectRecord, Long> getIdentity() {
        return Keys.IDENTITY_PROJECT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<ProjectRecord> getPrimaryKey() {
        return Keys.KEY_PROJECT_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ProjectRecord>> getKeys() {
        return Arrays.<UniqueKey<ProjectRecord>>asList(Keys.KEY_PROJECT_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Project as(String alias) {
        return new Project(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Project as(Name alias) {
        return new Project(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Project rename(String name) {
        return new Project(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Project rename(Name name) {
        return new Project(name, null);
    }
}
