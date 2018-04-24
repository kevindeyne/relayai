package com.kevindeyne.tasker.repositories

import com.kevindeyne.tasker.controller.timesheet.TimeUtils
import com.kevindeyne.tasker.jooq.Tables
import com.kevindeyne.tasker.service.EmailService
import org.apache.commons.lang.StringUtils
import org.jooq.DSLContext
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
open class ActivationRepositoryImpl (val dsl: DSLContext, val encoder : PasswordEncoder, val emailService: EmailService, val userRepository: UserRepository) : ActivationRepository {

	override fun registerActivation(userId : Long) {
		val newKey = StringUtils.abbreviate(encoder.encode(userId.toString()), 35)

		val email = userRepository.findElementById(Tables.USER.EMAIL, userId)
		emailService.sendActivationMail(newKey, email)

		dsl.insertInto(Tables.ACTIVATION_PENDING,
				Tables.ACTIVATION_PENDING.USER_ID, Tables.ACTIVATION_PENDING.ACTIVATION_KEY, Tables.ACTIVATION_PENDING.VALID_UNTIL)
				.values(userId, newKey, TimeUtils.INSTANCE.timeStampInXHours(2) )
				.execute()
	}
}