package com.kevindeyne.tasker.loader;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.kevindeyne.tasker.jooq.Tables;

@Component
public class IssueLoader implements ApplicationListener<ContextRefreshedEvent>, Ordered {

	@Autowired
	private DSLContext dsl;

	private boolean generateNew = true;
	private int maxUserIssuesInSprint = 40;

    @Override
	public int getOrder() {
        return 1;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	if(generateNew) {
    		dsl.truncate(Tables.ISSUE).execute();

    		int currentUserIssuesInSprint = 0;

    		Faker faker = new Faker();
        	System.out.println("Generating issues ...");
			for (int i = 0; i < 5000 + maxUserIssuesInSprint; i++) {
				Long assignedTo = 1L;
				if(maxUserIssuesInSprint > currentUserIssuesInSprint++) {
					assignedTo = 0L;
				}

				dsl.insertInto(Tables.ISSUE, Tables.ISSUE.TITLE, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.ASSIGNED, Tables.ISSUE.CREATE_DATE, Tables.ISSUE.CREATE_USER, Tables.ISSUE.UPDATE_DATE, Tables.ISSUE.UPDATE_USER)
				   .values(faker.book().title(), mergeSentences(faker.lorem().sentences(15)), assignedTo, getRandomTimestamp(), "1", getRandomTimestamp(), "1")
				   .execute();
			}
			System.out.println("Done!");
    	}
    }

	private String mergeSentences(List<String> sentences) {
		StringBuffer sb = new StringBuffer();
		sentences.forEach(s -> sb.append(s));
		return sb.toString();
	}

	private Timestamp getRandomTimestamp() {
		return new Timestamp(new Random().nextInt());
	}
}