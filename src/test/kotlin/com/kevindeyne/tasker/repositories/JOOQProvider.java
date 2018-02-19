package com.kevindeyne.tasker.repositories;

import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;

public abstract class JOOQProvider implements MockDataProvider {

	public JOOQProvider() { }

	protected boolean isSelectStatement(MockExecuteContext ctx) {
		return getSQLFromContext(ctx).toUpperCase().startsWith("SELECT");
	}

	protected boolean isSelectStatement(String sql) {
		return sql.startsWith("SELECT");
	}

	protected String getSQLFromContext(MockExecuteContext ctx) {
		String sql = ctx.sql().toUpperCase();
        System.out.println(sql.replaceAll("`", "").replaceAll("TASKR.", ""));
		return sql;
	}

}
