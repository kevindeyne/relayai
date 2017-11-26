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

    		Faker faker = new Faker();
        	System.out.println("Generating issues ...");

        	Long userId = generateUsers(faker);
        	Long projectId = generateProjects(faker);
        	attachProjectAndUser(projectId, userId);
        	Long sprintId = generateSprints(faker, projectId);

        	int currentUserIssuesInSprint = 0;
			for (int i = 0; i < 5000 + maxUserIssuesInSprint; i++) {
				Long assignedTo = 500L;
				if(maxUserIssuesInSprint > currentUserIssuesInSprint++) {
					assignedTo = userId;
				}

				insertIntoIssue(faker, projectId, sprintId, assignedTo);
			}

			System.out.println("Done!");
    	}
    }

	private void insertIntoIssue(Faker faker, Long projectId, Long sprintId, Long assignedTo) {
		dsl.insertInto(Tables.ISSUE, Tables.ISSUE.TITLE, Tables.ISSUE.DESCRIPTION,Tables.ISSUE.PROJECT_ID, Tables.ISSUE.SPRINT_ID, Tables.ISSUE.ASSIGNED, Tables.ISSUE.CREATE_DATE, Tables.ISSUE.CREATE_USER, Tables.ISSUE.UPDATE_DATE, Tables.ISSUE.UPDATE_USER)
		   .values(faker.book().title(), mergeSentences(faker.lorem().sentences(15)), projectId, sprintId, assignedTo, getRandomTimestamp(), "1", getRandomTimestamp(), "1")
		   .execute();
	}

	private Long generateSprints(Faker faker, Long projectId) {
		insertSprint(LocalDate.now().minusDays(1), LocalDate.now().plusDays(14), projectId);
		Long currentSprintId = getAnySprintId();


		int daysInThePast = totalAmountOfSprints*daysPerSprint + totalAmountOfSprints;
		LocalDate startTime = LocalDate.now().minusDays(daysInThePast);

		for (int i = 0; i < totalAmountOfSprints; i++) {
			insertSprint(startTime, startTime.plusDays(14), projectId);
			startTime = startTime.plusDays(15);
		}

		return currentSprintId;
	}

	private Long getAnySprintId() {
		return dsl.selectFrom(Tables.SPRINT).fetchAny().get(Tables.SPRINT.ID);
	}

	private void insertSprint(LocalDate startTime, LocalDate endTime, Long projectId) {
		dsl.insertInto(Tables.SPRINT, Tables.SPRINT.START_DATE, Tables.SPRINT.END_DATE, Tables.SPRINT.PROJECT_ID)
		   .values(Timestamp.valueOf(startTime.atStartOfDay()), Timestamp.valueOf(endTime.atStartOfDay()), projectId)
		   .execute();
	}

	private void attachProjectAndUser(Long projectId, Long userId) {
		dsl.insertInto(Tables.PROJECT_USERS, Tables.PROJECT_USERS.PROJECT_ID, Tables.PROJECT_USERS.USER_ID, Tables.PROJECT_USERS.ACTIVE)
		   .values(projectId, userId, true)
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
		insertIntoUser("admin", passwordEncoder.encode("admin"));
		Long userId = selectAnyUser();

		for (int i = 0; i < 500; i++) {
			insertIntoUser(faker.internet().emailAddress(),  faker.internet().password());
		}

		return userId;
	}

	private Long selectAnyUser() {
		return dsl.selectFrom(Tables.USER).fetchAny().get(Tables.USER.ID);
	}

	private void insertIntoUser(String username, String password) {
		dsl.insertInto(Tables.USER, Tables.USER.EMAIL, Tables.USER.PASSWORD, Tables.USER.CREATE_DATE, Tables.USER.CREATE_USER, Tables.USER.UPDATE_DATE, Tables.USER.UPDATE_USER)
		   .values(username, password, getRandomTimestamp(), "1", getRandomTimestamp(), "1")
		   .execute();
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