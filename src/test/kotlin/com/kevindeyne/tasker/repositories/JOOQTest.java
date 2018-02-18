package com.kevindeyne.tasker.repositories;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;

public abstract class JOOQTest {

	public static final SQLDialect sqlDialect = SQLDialect.MYSQL;

	public JOOQTest() { }

	protected DSLContext newDSL() {
		return DSL.using(sqlDialect);
	}

	protected DSLContext newDSL(MockDataProvider provider) {
		MockConnection connection = new MockConnection(provider);
		return DSL.using(connection, sqlDialect);
	}

}
