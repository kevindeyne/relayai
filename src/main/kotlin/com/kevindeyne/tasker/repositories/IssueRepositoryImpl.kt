package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.IssueListing
import com.kevindeyne.tasker.jooq.Tables
import org.jooq.DSLContext
import org.jooq.Record3
import org.jooq.Result
import org.jooq.tools.StringUtils
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Component
open class IssueRepositoryImpl (val dsl: DSLContext) : IssueRepository {
	
	@Transactional
	override fun findAllForUser(userId : String) : List<IssueListing> {
		
		var listing : Result<Record3<Long, String, String>> =				
			dsl.select(Tables.ISSUE.ID, Tables.ISSUE.TITLE, Tables.ISSUE.DESCRIPTION)
			   .from(Tables.ISSUE)
			   .orderBy(Tables.ISSUE.CREATE_DATE.desc()) //by importance
			   .fetch()
		
		return listing.parallelStream()
					  .map {
						  n -> IssueListing(n.get(Tables.ISSUE.ID),
											n.get(Tables.ISSUE.TITLE),
											abbreviate(n.get(Tables.ISSUE.DESCRIPTION)),
											n.get(Tables.ISSUE.DESCRIPTION))
					  }
					  .collect(Collectors.toList())		
	}
	
	@Transactional
	override fun findById(issueId : Long) : IssueListing {
		var record : Record3<Long, String, String> =				
			dsl.select(Tables.ISSUE.ID, Tables.ISSUE.TITLE, Tables.ISSUE.DESCRIPTION)
			   .from(Tables.ISSUE)
			   .where(Tables.ISSUE.ID.eq(issueId))	
			   .fetchOne()
		
		return record.map {
						  n -> IssueListing(n.get(Tables.ISSUE.ID),
											n.get(Tables.ISSUE.TITLE),
											"",
											n.get(Tables.ISSUE.DESCRIPTION))
					  }	
	}
	
	
	private fun abbreviate(field : String) : String {
		return (StringUtils.abbreviate(field, 60)).replace(Regex("<[^>]*>"), "")
	}
	
}