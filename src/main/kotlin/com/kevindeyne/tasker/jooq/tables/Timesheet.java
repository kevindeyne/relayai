/*
 * This file is generated by jOOQ.
*/
package com.kevindeyne.tasker.jooq.tables;


import com.kevindeyne.tasker.jooq.Indexes;
import com.kevindeyne.tasker.jooq.Keys;
import com.kevindeyne.tasker.jooq.Taskr;
import com.kevindeyne.tasker.jooq.tables.records.TimesheetRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
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
public class Timesheet extends TableImpl<TimesheetRecord> {

    private static final long serialVersionUID = 2069415950;

    /**
     * The reference instance of <code>taskr.timesheet</code>
     */
    public static final Timesheet TIMESHEET = new Timesheet();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TimesheetRecord> getRecordType() {
        return TimesheetRecord.class;
    }

    /**
     * The column <code>taskr.timesheet.id</code>.
     */
    public final TableField<TimesheetRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>taskr.timesheet.start_date</code>.
     */
    public final TableField<TimesheetRecord, Timestamp> START_DATE = createField("start_date", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>taskr.timesheet.end_date</code>.
     */
    public final TableField<TimesheetRecord, Timestamp> END_DATE = createField("end_date", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

    /**
     * The column <code>taskr.timesheet.avg_workday</code>.
     */
    public final TableField<TimesheetRecord, Integer> AVG_WORKDAY = createField("avg_workday", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("8", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>taskr.timesheet.issue_id</code>.
     */
    public final TableField<TimesheetRecord, Long> ISSUE_ID = createField("issue_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>taskr.timesheet.user_id</code>.
     */
    public final TableField<TimesheetRecord, Long> USER_ID = createField("user_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * Create a <code>taskr.timesheet</code> table reference
     */
    public Timesheet() {
        this(DSL.name("timesheet"), null);
    }

    /**
     * Create an aliased <code>taskr.timesheet</code> table reference
     */
    public Timesheet(String alias) {
        this(DSL.name(alias), TIMESHEET);
    }

    /**
     * Create an aliased <code>taskr.timesheet</code> table reference
     */
    public Timesheet(Name alias) {
        this(alias, TIMESHEET);
    }

    private Timesheet(Name alias, Table<TimesheetRecord> aliased) {
        this(alias, aliased, null);
    }

    private Timesheet(Name alias, Table<TimesheetRecord> aliased, Field<?>[] parameters) {
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
        return Arrays.<Index>asList(Indexes.TIMESHEET_ISSUE_ID, Indexes.TIMESHEET_PRIMARY, Indexes.TIMESHEET_TIMESHEET_DATE, Indexes.TIMESHEET_USER_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<TimesheetRecord, Long> getIdentity() {
        return Keys.IDENTITY_TIMESHEET;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<TimesheetRecord> getPrimaryKey() {
        return Keys.KEY_TIMESHEET_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<TimesheetRecord>> getKeys() {
        return Arrays.<UniqueKey<TimesheetRecord>>asList(Keys.KEY_TIMESHEET_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<TimesheetRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<TimesheetRecord, ?>>asList(Keys.TIMESHEET_IBFK_1, Keys.TIMESHEET_IBFK_2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timesheet as(String alias) {
        return new Timesheet(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timesheet as(Name alias) {
        return new Timesheet(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Timesheet rename(String name) {
        return new Timesheet(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Timesheet rename(Name name) {
        return new Timesheet(name, null);
    }
}
