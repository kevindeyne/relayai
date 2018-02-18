package com.kevindeyne.tasker.repositories;

import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;

public abstract class JOOQProvider implements MockDataProvider {

	public JOOQProvider() { }

	protected boolean isSelectStatement(MockExecuteContext ctx) {
		return getSQLFromContext(ctx).toUpperCase().startsWith("SELECT");
	}

	private String getSQLFromContext(MockExecuteContext ctx) {
		String sql = ctx.sql();
        System.out.println(sql.replaceAll("`", "").replaceAll("taskr.", ""));
		return sql;
	}

}
