/*
 * This file is generated by jOOQ.
*/
package com.kevindeyne.tasker.jooq;


import com.kevindeyne.tasker.jooq.tables.Event;
import com.kevindeyne.tasker.jooq.tables.Issue;
import com.kevindeyne.tasker.jooq.tables.Knowledge;
import com.kevindeyne.tasker.jooq.tables.Project;
import com.kevindeyne.tasker.jooq.tables.ProjectUsers;
import com.kevindeyne.tasker.jooq.tables.SchemaVersion;
import com.kevindeyne.tasker.jooq.tables.Search;
import com.kevindeyne.tasker.jooq.tables.Sprint;
import com.kevindeyne.tasker.jooq.tables.Tag;
import com.kevindeyne.tasker.jooq.tables.Tagcloud;
import com.kevindeyne.tasker.jooq.tables.User;
import com.kevindeyne.tasker.jooq.tables.records.EventRecord;
import com.kevindeyne.tasker.jooq.tables.records.IssueRecord;
import com.kevindeyne.tasker.jooq.tables.records.KnowledgeRecord;
import com.kevindeyne.tasker.jooq.tables.records.ProjectRecord;
import com.kevindeyne.tasker.jooq.tables.records.ProjectUsersRecord;
import com.kevindeyne.tasker.jooq.tables.records.SchemaVersionRecord;
import com.kevindeyne.tasker.jooq.tables.records.SearchRecord;
import com.kevindeyne.tasker.jooq.tables.records.SprintRecord;
import com.kevindeyne.tasker.jooq.tables.records.TagRecord;
import com.kevindeyne.tasker.jooq.tables.records.TagcloudRecord;
import com.kevindeyne.tasker.jooq.tables.records.UserRecord;

import javax.annotation.Generated;

import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>taskr</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<EventRecord, Long> IDENTITY_EVENT = Identities0.IDENTITY_EVENT;
    public static final Identity<IssueRecord, Long> IDENTITY_ISSUE = Identities0.IDENTITY_ISSUE;
    public static final Identity<KnowledgeRecord, Long> IDENTITY_KNOWLEDGE = Identities0.IDENTITY_KNOWLEDGE;
    public static final Identity<ProjectRecord, Long> IDENTITY_PROJECT = Identities0.IDENTITY_PROJECT;
    public static final Identity<ProjectUsersRecord, Long> IDENTITY_PROJECT_USERS = Identities0.IDENTITY_PROJECT_USERS;
    public static final Identity<SearchRecord, Long> IDENTITY_SEARCH = Identities0.IDENTITY_SEARCH;
    public static final Identity<SprintRecord, Long> IDENTITY_SPRINT = Identities0.IDENTITY_SPRINT;
    public static final Identity<TagRecord, Long> IDENTITY_TAG = Identities0.IDENTITY_TAG;
    public static final Identity<TagcloudRecord, Long> IDENTITY_TAGCLOUD = Identities0.IDENTITY_TAGCLOUD;
    public static final Identity<UserRecord, Long> IDENTITY_USER = Identities0.IDENTITY_USER;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<EventRecord> KEY_EVENT_PRIMARY = UniqueKeys0.KEY_EVENT_PRIMARY;
    public static final UniqueKey<IssueRecord> KEY_ISSUE_PRIMARY = UniqueKeys0.KEY_ISSUE_PRIMARY;
    public static final UniqueKey<KnowledgeRecord> KEY_KNOWLEDGE_PRIMARY = UniqueKeys0.KEY_KNOWLEDGE_PRIMARY;
    public static final UniqueKey<ProjectRecord> KEY_PROJECT_PRIMARY = UniqueKeys0.KEY_PROJECT_PRIMARY;
    public static final UniqueKey<ProjectUsersRecord> KEY_PROJECT_USERS_PRIMARY = UniqueKeys0.KEY_PROJECT_USERS_PRIMARY;
    public static final UniqueKey<SchemaVersionRecord> KEY_SCHEMA_VERSION_PRIMARY = UniqueKeys0.KEY_SCHEMA_VERSION_PRIMARY;
    public static final UniqueKey<SearchRecord> KEY_SEARCH_PRIMARY = UniqueKeys0.KEY_SEARCH_PRIMARY;
    public static final UniqueKey<SprintRecord> KEY_SPRINT_PRIMARY = UniqueKeys0.KEY_SPRINT_PRIMARY;
    public static final UniqueKey<TagRecord> KEY_TAG_PRIMARY = UniqueKeys0.KEY_TAG_PRIMARY;
    public static final UniqueKey<TagcloudRecord> KEY_TAGCLOUD_PRIMARY = UniqueKeys0.KEY_TAGCLOUD_PRIMARY;
    public static final UniqueKey<UserRecord> KEY_USER_PRIMARY = UniqueKeys0.KEY_USER_PRIMARY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 extends AbstractKeys {
        public static Identity<EventRecord, Long> IDENTITY_EVENT = createIdentity(Event.EVENT, Event.EVENT.ID);
        public static Identity<IssueRecord, Long> IDENTITY_ISSUE = createIdentity(Issue.ISSUE, Issue.ISSUE.ID);
        public static Identity<KnowledgeRecord, Long> IDENTITY_KNOWLEDGE = createIdentity(Knowledge.KNOWLEDGE, Knowledge.KNOWLEDGE.ID);
        public static Identity<ProjectRecord, Long> IDENTITY_PROJECT = createIdentity(Project.PROJECT, Project.PROJECT.ID);
        public static Identity<ProjectUsersRecord, Long> IDENTITY_PROJECT_USERS = createIdentity(ProjectUsers.PROJECT_USERS, ProjectUsers.PROJECT_USERS.ID);
        public static Identity<SearchRecord, Long> IDENTITY_SEARCH = createIdentity(Search.SEARCH, Search.SEARCH.ID);
        public static Identity<SprintRecord, Long> IDENTITY_SPRINT = createIdentity(Sprint.SPRINT, Sprint.SPRINT.ID);
        public static Identity<TagRecord, Long> IDENTITY_TAG = createIdentity(Tag.TAG, Tag.TAG.ID);
        public static Identity<TagcloudRecord, Long> IDENTITY_TAGCLOUD = createIdentity(Tagcloud.TAGCLOUD, Tagcloud.TAGCLOUD.ID);
        public static Identity<UserRecord, Long> IDENTITY_USER = createIdentity(User.USER, User.USER.ID);
    }

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<EventRecord> KEY_EVENT_PRIMARY = createUniqueKey(Event.EVENT, "KEY_event_PRIMARY", Event.EVENT.ID);
        public static final UniqueKey<IssueRecord> KEY_ISSUE_PRIMARY = createUniqueKey(Issue.ISSUE, "KEY_issue_PRIMARY", Issue.ISSUE.ID);
        public static final UniqueKey<KnowledgeRecord> KEY_KNOWLEDGE_PRIMARY = createUniqueKey(Knowledge.KNOWLEDGE, "KEY_knowledge_PRIMARY", Knowledge.KNOWLEDGE.ID);
        public static final UniqueKey<ProjectRecord> KEY_PROJECT_PRIMARY = createUniqueKey(Project.PROJECT, "KEY_project_PRIMARY", Project.PROJECT.ID);
        public static final UniqueKey<ProjectUsersRecord> KEY_PROJECT_USERS_PRIMARY = createUniqueKey(ProjectUsers.PROJECT_USERS, "KEY_project_users_PRIMARY", ProjectUsers.PROJECT_USERS.ID);
        public static final UniqueKey<SchemaVersionRecord> KEY_SCHEMA_VERSION_PRIMARY = createUniqueKey(SchemaVersion.SCHEMA_VERSION, "KEY_schema_version_PRIMARY", SchemaVersion.SCHEMA_VERSION.INSTALLED_RANK);
        public static final UniqueKey<SearchRecord> KEY_SEARCH_PRIMARY = createUniqueKey(Search.SEARCH, "KEY_search_PRIMARY", Search.SEARCH.ID);
        public static final UniqueKey<SprintRecord> KEY_SPRINT_PRIMARY = createUniqueKey(Sprint.SPRINT, "KEY_sprint_PRIMARY", Sprint.SPRINT.ID);
        public static final UniqueKey<TagRecord> KEY_TAG_PRIMARY = createUniqueKey(Tag.TAG, "KEY_tag_PRIMARY", Tag.TAG.ID);
        public static final UniqueKey<TagcloudRecord> KEY_TAGCLOUD_PRIMARY = createUniqueKey(Tagcloud.TAGCLOUD, "KEY_tagcloud_PRIMARY", Tagcloud.TAGCLOUD.ID);
        public static final UniqueKey<UserRecord> KEY_USER_PRIMARY = createUniqueKey(User.USER, "KEY_user_PRIMARY", User.USER.ID);
    }
}
