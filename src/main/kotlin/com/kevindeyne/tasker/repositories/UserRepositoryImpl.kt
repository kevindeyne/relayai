package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.domain.UserPrincipal
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.jooq.tables.records.UserRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Repository
open class UserRepositoryImpl (val dsl: DSLContext) : UserRepository {
	
	@Transactional
	override fun findByUsername(username : String) : UserPrincipal? {				
		var record : Optional<UserRecord> = dsl.selectFrom(Tables.USER)
			   .where(Tables.USER.EMAIL.eq(username))
			   .fetchOptional()
		
		if(record.isPresent) {
			return record.get().map {
			      n -> UserPrincipal(n.get(Tables.USER.ID),
									 n.get(Tables.USER.EMAIL),
									 n.get(Tables.USER.PASSWORD))
			   }	
		} else {
			return null
		}
	}
}