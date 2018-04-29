package com.kevindeyne.tasker.service

import com.kevindeyne.tasker.controller.form.FormResponse
import com.kevindeyne.tasker.controller.form.RegistrationForm
import com.kevindeyne.tasker.domain.UserPrincipal
import com.kevindeyne.tasker.repositories.ActivationRepository
import com.kevindeyne.tasker.repositories.UserRepository
import org.apache.commons.validator.GenericValidator
import org.apache.commons.validator.routines.EmailValidator

open class RegistrationService(var userRepository: UserRepository, var activationRepository: ActivationRepository) {
		
	fun registerUser(form : RegistrationForm) : FormResponse {
		val v = validate(form)
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

	private fun validate(form : RegistrationForm) : FormResponse {
		if(!(GenericValidator.maxLength(form.username, 30) && GenericValidator.minLength(form.username, 2))){
			return FormResponse("NOK", "username")
		}

		if(!EmailValidator.getInstance(false).isValid(form.email)){
			return FormResponse("NOK", "email")
		}

		if(GenericValidator.isBlankOrNull(form.password) || !GenericValidator.minLength(form.password, 4)){
			return FormResponse("NOK", "password")
		}

		if(GenericValidator.isBlankOrNull(form.password2) || !GenericValidator.minLength(form.password2, 4)){
			return FormResponse("NOK", "password2")
		}

		if(form.password != form.password2){
			return FormResponse("NOK", "password2")
		}

		if(!GenericValidator.isBlankOrNull(form.country)){ //honeypot
			return FormResponse("NOK", "email")
		}

		return FormResponse("OK")
	}
}