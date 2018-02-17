package com.kevindeyne.tasker.repositories;

import static com.kevindeyne.tasker.jooq.Tables.PROJECT;

import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;
import org.junit.Assert;
import org.junit.Test;

import com.kevindeyne.tasker.domain.ProjectListing;


public class ProjectRepositoryJOOQTest {

	@Test
	public void test() {
		// Initialise your data provider (implementation further down):
		MockDataProvider provider = new MyProvider();
		MockConnection connection = new MockConnection(provider);

		// Pass the mock connection to a jOOQ DSLContext:
		DSLContext dsl = DSL.using(connection, SQLDialect.MYSQL);

		IssueRepository issueRepo = new IssueRepositoryImpl(dsl);
		SprintRepository sprintRepo = new SprintRepositoryImpl(dsl, issueRepo);

		// Execute queries transparently, with the above DSLContext:
		ProjectRepositoryImpl repo = new ProjectRepositoryImpl(dsl, sprintRepo, issueRepo);

		ProjectListing project = repo.findProject(1L);

		Assert.assertNotNull(project);
	}

	private class MyProvider implements MockDataProvider {

	    @Override
	    public MockResult[] execute(MockExecuteContext ctx) throws SQLException {

	        // You might need a DSLContext to create org.jooq.Result and org.jooq.Record objects
	        DSLContext dsl = DSL.using(SQLDialect.MYSQL);
	        MockResult[] mock = new MockResult[1];

	        // The execute context contains SQL string(s), bind values, and other meta-data
	        String sql = ctx.sql();
	        System.out.println(sql.replaceAll("`", "").replaceAll("taskr.", ""));

	        // You decide, whether any given statement returns results, and how many
	        if (sql.toUpperCase().startsWith("SELECT")) {
	        	Result<Record3<Long, String, String>> result = dsl.newResult(PROJECT.ID, PROJECT.TITLE, PROJECT.KEY);
	            result.add(dsl
	                .newRecord(PROJECT.ID, PROJECT.TITLE, PROJECT.KEY)
	                .values(1L, "Orwell", "Key"));
	            mock[0] = new MockResult(1, result);
	        }

	        // You can detect batch statements easily
	        else if (ctx.batch()) {
	            // [...]
	        }

	        return mock;
	    }
	}

}
