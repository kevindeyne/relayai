package com.kevindeyne.tasker.loader;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.kevindeyne.tasker.jooq.Tables;

@Component
public class IssueLoader implements ApplicationListener<ContextRefreshedEvent>, Ordered {

	@Autowired
	private DSLContext dsl;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private boolean generateNew = true;
	private int maxUserIssuesInSprint = 40;
	private int daysPerSprint = 14;
	private int totalAmountOfSprints = 500;

    @Override
	public int getOrder() {
        return 1;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	if(generateNew) {
    		truncateAll();

    		int currentUserIssuesInSprint = 0;

    		Faker faker = new Faker();
        	System.out.println("Generating issues ...");

        	Long userId = generateUsers(faker);
        	Long projectId = generateProjects(faker);
        	attachProjectAndUser(projectId, userId);

        	Long sprintId = generateSprints(faker, projectId);

			for (int i = 0; i < 5000 + maxUserIssuesInSprint; i++) {
				Long assignedTo = 1L;
				if(maxUserIssuesInSprint > currentUserIssuesInSprint++) {
					assignedTo = 0L;
				}

				dsl.insertInto(Tables.ISSUE, Tables.ISSUE.TITLE, Tables.ISSUE.DESCRIPTION,Tables.ISSUE.PROJECT_ID, Tables.ISSUE.SPRINT_ID, Tables.ISSUE.ASSIGNED, Tables.ISSUE.CREATE_DATE, Tables.ISSUE.CREATE_USER, Tables.ISSUE.UPDATE_DATE, Tables.ISSUE.UPDATE_USER)
				   .values(faker.book().title(), mergeSentences(faker.lorem().sentences(15)), projectId, sprintId, assignedTo, getRandomTimestamp(), "1", getRandomTimestamp(), "1")
				   .execute();
			}
			System.out.println("Done!");
    	}
    }

	private Long generateSprints(Faker faker, Long projectId) {
		int daysInThePast = totalAmountOfSprints*daysPerSprint + totalAmountOfSprints;
		LocalDate startTime = LocalDate.now().minusDays(daysInThePast);

		for (int i = 0; i < totalAmountOfSprints; i++) {
			dsl.insertInto(Tables.SPRINT, Tables.SPRINT.START_DATE, Tables.SPRINT.END_DATE, Tables.SPRINT.PROJECT_ID)
			   .values(Timestamp.valueOf(startTime.atStartOfDay()), Timestamp.valueOf(startTime.plusDays(14).atStartOfDay()), projectId)
			   .execute();
			startTime = startTime.plusDays(15);
		}

		return dsl.selectFrom(Tables.SPRINT).fetchAny().get(Tables.SPRINT.ID);
	}

	private void attachProjectAndUser(Long projectId, Long userId) {
		dsl.insertInto(Tables.PROJECT_USERS, Tables.PROJECT_USERS.PROJECT_ID, Tables.PROJECT_USERS.USER_ID)
		   .values(projectId, userId)
		   .execute();
	}

	private Long generateProjects(Faker faker) {
		for (int i = 0; i < 500; i++) {
			dsl.insertInto(Tables.PROJECT, Tables.PROJECT.TITLE, Tables.PROJECT.KEY)
			   .values(faker.space().nasaSpaceCraft(), faker.space().agencyAbbreviation())
			   .execute();
		}

		return dsl.selectFrom(Tables.PROJECT).fetchAny().get(Tables.PROJECT.ID);
	}

	private Long generateUsers(Faker faker) {
		dsl.insertInto(Tables.USER, Tables.USER.EMAIL, Tables.USER.PASSWORD, Tables.USER.CREATE_DATE, Tables.USER.CREATE_USER, Tables.USER.UPDATE_DATE, Tables.USER.UPDATE_USER)
		   .values("admin", passwordEncoder.encode("admin"), getRandomTimestamp(), "1", getRandomTimestamp(), "1")
		   .execute();

		Long userId = dsl.selectFrom(Tables.USER).fetchAny().get(Tables.USER.ID);

		for (int i = 0; i < 500; i++) {
			dsl.insertInto(Tables.USER, Tables.USER.EMAIL, Tables.USER.PASSWORD, Tables.USER.CREATE_DATE, Tables.USER.CREATE_USER, Tables.USER.UPDATE_DATE, Tables.USER.UPDATE_USER)
			   .values(faker.internet().emailAddress(), faker.internet().password(), getRandomTimestamp(), "1", getRandomTimestamp(), "1")
			   .execute();
		}

		return userId;
	}

	private void truncateAll() {
		dsl.truncate(Tables.PROJECT_USERS).execute();
		dsl.truncate(Tables.ISSUE).execute();
		dsl.truncate(Tables.SPRINT).execute();
		dsl.truncate(Tables.USER).execute();
		dsl.truncate(Tables.PROJECT).execute();
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