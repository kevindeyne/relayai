package com.kevindeyne.tasker.repositories;

import static com.kevindeyne.tasker.jooq.Tables.PROJECT;

import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;
import org.junit.Assert;
import org.junit.Test;

import com.kevindeyne.tasker.domain.ProjectListing;


public class ProjectRepositoryJOOQTest extends JOOQTest {

	@Test
	public void test() {
		DSLContext dsl = newDSL(new MyProvider());

		IssueRepository issueRepo = new IssueRepositoryImpl(dsl);
		SprintRepository sprintRepo = new SprintRepositoryImpl(dsl, issueRepo);

		ProjectRepositoryImpl repo = new ProjectRepositoryImpl(dsl, sprintRepo, issueRepo);
		ProjectListing project = repo.findProject(1L);

		Assert.assertNotNull(project);
	}

	private class MyProvider extends JOOQProvider {
	    @Override
	    public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
	        DSLContext dsl = newDSL();
	        MockResult[] mock = new MockResult[1];

	        if (isSelectStatement(ctx)) {
	        	Result<Record3<Long, String, String>> result = dsl.newResult(PROJECT.ID, PROJECT.TITLE, PROJECT.KEY);
	            result.add(dsl.newRecord(PROJECT.ID, PROJECT.TITLE, PROJECT.KEY).values(1L, "Orwell", "Key"));
	            mock[0] = new MockResult(1, result);
	        }

	        return mock;
	    }
	}

}
