package com.kevindeyne.tasker.service

import com.kevindeyne.tasker.controller.form.AcceptInviteForm
import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.controller.form.RegistrationForm
import com.kevindeyne.tasker.domain.UserPrincipal
import com.kevindeyne.tasker.repositories.ActivationRepository
import com.kevindeyne.tasker.repositories.InvitationRepository
import com.kevindeyne.tasker.repositories.UserRepository
import org.apache.commons.validator.GenericValidator
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.transaction.annotation.Transactional


@Transactional
open class RegistrationService(var userRepository: UserRepository, var activationRepository: ActivationRepository, var invitationRepository : InvitationRepository) {
		
	fun registerUser(form : RegistrationForm) : FormResponse {
		val v = validate(form.username, form.email, form.password, form.password2, form.country)
		if(v.status == "OK"){
			var user : UserPrincipal? = userRepository.findByUsername(form.email)
			if(user != null){
				return FormResponse("NOK", "email")
			} else {
				form.password = activationRepository.encodePassword(form.password)
				val userId : Long = userRepository.create(form.username, form.email, form.password)
				activationRepository.registerActivation(userId)
			}
		}

		return v
    }

	fun acceptInvite(inviteID : String, inviteKey : String, form : AcceptInviteForm) : FormResponse {
		val dto = invitationRepository.findEmail(inviteID, inviteKey)
		val v = validate(form.username, dto?.email, form.password, form.password2, form.country)
		if(v.status == "OK" && dto != null){
			val user : UserPrincipal? = userRepository.findByUsername(dto.email)
			val userId : Long
			if(user == null){
				form.password = activationRepository.encodePassword(form.password)
				userId = userRepository.create(form.username, dto.email, form.password)
			} else {
				userId = user.userId
			}

			userRepository.addUserToProject(userId, dto.projectID)
			invitationRepository.removeInvitation(inviteID)
			v.element = dto.email
		}

		return v
	}

	private fun validate(username : String?, email: String?, password: String?, password2: String?, country: String?) : FormResponse {
		if(!(GenericValidator.maxLength(username, 30) && GenericValidator.minLength(username, 2))){
			return FormResponse("NOK", "username")
		}

		if(GenericValidator.isBlankOrNull(email)){
			return FormResponse("NOK", "email")
		}

		if(!EmailValidator.getInstance(false).isValid(email)){
			return FormResponse("NOK", "email")
		}

		if(GenericValidator.isBlankOrNull(password) || !GenericValidator.minLength(password, 4)){
			return FormResponse("NOK", "password")
		}

		if(GenericValidator.isBlankOrNull(password2) || !GenericValidator.minLength(password2, 4)){
			return FormResponse("NOK", "password2")
		}

		if(password != password2){
			return FormResponse("NOK", "password2")
		}

		if(!GenericValidator.isBlankOrNull(country)){ //honeypot
			return FormResponse("NOK", "email")
		}

		return FormResponse("OK")
	}
}