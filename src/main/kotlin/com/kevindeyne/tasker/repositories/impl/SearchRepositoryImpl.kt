package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.SearchResult
import com.kevindeyne.tasker.domain.SearchResultType
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.service.SecurityHolder
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Repository
open class SearchRepositoryImpl (val dsl: DSLContext) : SearchRepository {
	
	@Transactional
	override fun findInSrcVal(text : String) : List<SearchResult>{
		return dsl.selectFrom(Tables.SEARCH)
			   .where(Tables.SEARCH.PROJECT_ID.eq(SecurityHolder.getProjectId())).and(Tables.SEARCH.SRCVAL.like("%$text%"))
			   .fetch()
			   .parallelStream()
			   .map {
				  n -> SearchResult(n.get(Tables.SEARCH.ID),
									n.get(Tables.SEARCH.LINKED_ID),
									SearchResultType.valueOf(n.get(Tables.SEARCH.TYPE)),
									n.get(Tables.SEARCH.NAME))
			   }
			   .collect(Collectors.toList())
	}
}