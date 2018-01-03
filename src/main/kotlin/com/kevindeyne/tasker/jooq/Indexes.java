/*
 * This file is generated by jOOQ.
*/
package com.kevindeyne.tasker.jooq;


import com.kevindeyne.tasker.jooq.tables.Comments;
import com.kevindeyne.tasker.jooq.tables.Event;
import com.kevindeyne.tasker.jooq.tables.Invitation;
import com.kevindeyne.tasker.jooq.tables.Issue;
import com.kevindeyne.tasker.jooq.tables.Knowledge;
import com.kevindeyne.tasker.jooq.tables.Project;
import com.kevindeyne.tasker.jooq.tables.ProjectUsers;
import com.kevindeyne.tasker.jooq.tables.SchemaVersion;
import com.kevindeyne.tasker.jooq.tables.Search;
import com.kevindeyne.tasker.jooq.tables.Sprint;
import com.kevindeyne.tasker.jooq.tables.Tag;
import com.kevindeyne.tasker.jooq.tables.Tagcloud;
import com.kevindeyne.tasker.jooq.tables.Timesheet;
import com.kevindeyne.tasker.jooq.tables.User;
import com.kevindeyne.tasker.jooq.tables.UserRole;

import javax.annotation.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling indexes of tables of the <code>taskr</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index COMMENTS_ISSUE_ID = Indexes0.COMMENTS_ISSUE_ID;
    public static final Index COMMENTS_PRIMARY = Indexes0.COMMENTS_PRIMARY;
    public static final Index COMMENTS_USER_ID = Indexes0.COMMENTS_USER_ID;
    public static final Index EVENT_PRIMARY = Indexes0.EVENT_PRIMARY;
    public static final Index INVITATION_PRIMARY = Indexes0.INVITATION_PRIMARY;
    public static final Index ISSUE_PRIMARY = Indexes0.ISSUE_PRIMARY;
    public static final Index ISSUE_PROJECT_ID = Indexes0.ISSUE_PROJECT_ID;
    public static final Index ISSUE_SPRINT_ID = Indexes0.ISSUE_SPRINT_ID;
    public static final Index KNOWLEDGE_PRIMARY = Indexes0.KNOWLEDGE_PRIMARY;
    public static final Index KNOWLEDGE_TAG_ID = Indexes0.KNOWLEDGE_TAG_ID;
    public static final Index KNOWLEDGE_USER_ID = Indexes0.KNOWLEDGE_USER_ID;
    public static final Index PROJECT_PRIMARY = Indexes0.PROJECT_PRIMARY;
    public static final Index PROJECT_USERS_PRIMARY = Indexes0.PROJECT_USERS_PRIMARY;
    public static final Index PROJECT_USERS_PROJECT_ID = Indexes0.PROJECT_USERS_PROJECT_ID;
    public static final Index PROJECT_USERS_USER_ID = Indexes0.PROJECT_USERS_USER_ID;
    public static final Index SCHEMA_VERSION_PRIMARY = Indexes0.SCHEMA_VERSION_PRIMARY;
    public static final Index SCHEMA_VERSION_SCHEMA_VERSION_S_IDX = Indexes0.SCHEMA_VERSION_SCHEMA_VERSION_S_IDX;
    public static final Index SEARCH_PRIMARY = Indexes0.SEARCH_PRIMARY;
    public static final Index SEARCH_SEARCH_INDEX = Indexes0.SEARCH_SEARCH_INDEX;
    public static final Index SPRINT_PRIMARY = Indexes0.SPRINT_PRIMARY;
    public static final Index SPRINT_PROJECT_ID = Indexes0.SPRINT_PROJECT_ID;
    public static final Index TAG_PRIMARY = Indexes0.TAG_PRIMARY;
    public static final Index TAG_TAG = Indexes0.TAG_TAG;
    public static final Index TAGCLOUD_ISSUE_ID = Indexes0.TAGCLOUD_ISSUE_ID;
    public static final Index TAGCLOUD_PRIMARY = Indexes0.TAGCLOUD_PRIMARY;
    public static final Index TAGCLOUD_TAG_ID = Indexes0.TAGCLOUD_TAG_ID;
    public static final Index TIMESHEET_ISSUE_ID = Indexes0.TIMESHEET_ISSUE_ID;
    public static final Index TIMESHEET_PRIMARY = Indexes0.TIMESHEET_PRIMARY;
    public static final Index TIMESHEET_TIMESHEET_DATE = Indexes0.TIMESHEET_TIMESHEET_DATE;
    public static final Index TIMESHEET_USER_ID = Indexes0.TIMESHEET_USER_ID;
    public static final Index USER_PRIMARY = Indexes0.USER_PRIMARY;
    public static final Index USER_ROLE_PRIMARY = Indexes0.USER_ROLE_PRIMARY;
    public static final Index USER_ROLE_USER_ID = Indexes0.USER_ROLE_USER_ID;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 extends AbstractKeys {
        public static Index COMMENTS_ISSUE_ID = createIndex("issue_id", Comments.COMMENTS, new OrderField[] { Comments.COMMENTS.ISSUE_ID }, false);
        public static Index COMMENTS_PRIMARY = createIndex("PRIMARY", Comments.COMMENTS, new OrderField[] { Comments.COMMENTS.ID }, true);
        public static Index COMMENTS_USER_ID = createIndex("user_id", Comments.COMMENTS, new OrderField[] { Comments.COMMENTS.USER_ID }, false);
        public static Index EVENT_PRIMARY = createIndex("PRIMARY", Event.EVENT, new OrderField[] { Event.EVENT.ID }, true);
        public static Index INVITATION_PRIMARY = createIndex("PRIMARY", Invitation.INVITATION, new OrderField[] { Invitation.INVITATION.ID }, true);
        public static Index ISSUE_PRIMARY = createIndex("PRIMARY", Issue.ISSUE, new OrderField[] { Issue.ISSUE.ID }, true);
        public static Index ISSUE_PROJECT_ID = createIndex("project_id", Issue.ISSUE, new OrderField[] { Issue.ISSUE.PROJECT_ID }, false);
        public static Index ISSUE_SPRINT_ID = createIndex("sprint_id", Issue.ISSUE, new OrderField[] { Issue.ISSUE.SPRINT_ID }, false);
        public static Index KNOWLEDGE_PRIMARY = createIndex("PRIMARY", Knowledge.KNOWLEDGE, new OrderField[] { Knowledge.KNOWLEDGE.ID }, true);
        public static Index KNOWLEDGE_TAG_ID = createIndex("tag_id", Knowledge.KNOWLEDGE, new OrderField[] { Knowledge.KNOWLEDGE.TAG_ID }, false);
        public static Index KNOWLEDGE_USER_ID = createIndex("user_id", Knowledge.KNOWLEDGE, new OrderField[] { Knowledge.KNOWLEDGE.USER_ID }, false);
        public static Index PROJECT_PRIMARY = createIndex("PRIMARY", Project.PROJECT, new OrderField[] { Project.PROJECT.ID }, true);
        public static Index PROJECT_USERS_PRIMARY = createIndex("PRIMARY", ProjectUsers.PROJECT_USERS, new OrderField[] { ProjectUsers.PROJECT_USERS.ID }, true);
        public static Index PROJECT_USERS_PROJECT_ID = createIndex("project_id", ProjectUsers.PROJECT_USERS, new OrderField[] { ProjectUsers.PROJECT_USERS.PROJECT_ID }, false);
        public static Index PROJECT_USERS_USER_ID = createIndex("user_id", ProjectUsers.PROJECT_USERS, new OrderField[] { ProjectUsers.PROJECT_USERS.USER_ID }, false);
        public static Index SCHEMA_VERSION_PRIMARY = createIndex("PRIMARY", SchemaVersion.SCHEMA_VERSION, new OrderField[] { SchemaVersion.SCHEMA_VERSION.INSTALLED_RANK }, true);
        public static Index SCHEMA_VERSION_SCHEMA_VERSION_S_IDX = createIndex("schema_version_s_idx", SchemaVersion.SCHEMA_VERSION, new OrderField[] { SchemaVersion.SCHEMA_VERSION.SUCCESS }, false);
        public static Index SEARCH_PRIMARY = createIndex("PRIMARY", Search.SEARCH, new OrderField[] { Search.SEARCH.ID }, true);
        public static Index SEARCH_SEARCH_INDEX = createIndex("search_index", Search.SEARCH, new OrderField[] { Search.SEARCH.SRCVAL }, false);
        public static Index SPRINT_PRIMARY = createIndex("PRIMARY", Sprint.SPRINT, new OrderField[] { Sprint.SPRINT.ID }, true);
        public static Index SPRINT_PROJECT_ID = createIndex("project_id", Sprint.SPRINT, new OrderField[] { Sprint.SPRINT.PROJECT_ID }, false);
        public static Index TAG_PRIMARY = createIndex("PRIMARY", Tag.TAG, new OrderField[] { Tag.TAG.ID }, true);
        public static Index TAG_TAG = createIndex("tag", Tag.TAG, new OrderField[] { Tag.TAG.TAG_ }, false);
        public static Index TAGCLOUD_ISSUE_ID = createIndex("issue_id", Tagcloud.TAGCLOUD, new OrderField[] { Tagcloud.TAGCLOUD.ISSUE_ID }, false);
        public static Index TAGCLOUD_PRIMARY = createIndex("PRIMARY", Tagcloud.TAGCLOUD, new OrderField[] { Tagcloud.TAGCLOUD.ID }, true);
        public static Index TAGCLOUD_TAG_ID = createIndex("tag_id", Tagcloud.TAGCLOUD, new OrderField[] { Tagcloud.TAGCLOUD.TAG_ID }, false);
        public static Index TIMESHEET_ISSUE_ID = createIndex("issue_id", Timesheet.TIMESHEET, new OrderField[] { Timesheet.TIMESHEET.ISSUE_ID }, false);
        public static Index TIMESHEET_PRIMARY = createIndex("PRIMARY", Timesheet.TIMESHEET, new OrderField[] { Timesheet.TIMESHEET.ID }, true);
        public static Index TIMESHEET_TIMESHEET_DATE = createIndex("timesheet_date", Timesheet.TIMESHEET, new OrderField[] { Timesheet.TIMESHEET.START_DATE }, false);
        public static Index TIMESHEET_USER_ID = createIndex("user_id", Timesheet.TIMESHEET, new OrderField[] { Timesheet.TIMESHEET.USER_ID }, false);
        public static Index USER_PRIMARY = createIndex("PRIMARY", User.USER, new OrderField[] { User.USER.ID }, true);
        public static Index USER_ROLE_PRIMARY = createIndex("PRIMARY", UserRole.USER_ROLE, new OrderField[] { UserRole.USER_ROLE.ID }, true);
        public static Index USER_ROLE_USER_ID = createIndex("user_id", UserRole.USER_ROLE, new OrderField[] { UserRole.USER_ROLE.USER_ID }, false);
    }
}
