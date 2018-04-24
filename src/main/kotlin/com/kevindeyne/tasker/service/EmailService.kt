package com.kevindeyne.tasker.service

import com.kevindeyne.tasker.repositories.ProjectRepository
import com.kevindeyne.tasker.repositories.UserRepository
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component



@Component
open class EmailService(var userRepository: UserRepository, var projectRepository: ProjectRepository, var passwordEncoder : PasswordEncoder, val mailSender : MailSender) {
		
	fun sendActivationMail(key : String, email : String) {
		val simpleMailMessage = SimpleMailMessage()
		simpleMailMessage.setFrom("noreply@relayai.io")
		simpleMailMessage.setTo(email)
		simpleMailMessage.setSubject("RelayAI.io: Please activate your account")
		simpleMailMessage.setText("Hello, Your account is almost complete. Please activate your account here; https://www.relayai.io/activate/$key to take your newly created account into use. Thank you for choosing RelayAI.")

		mailSender.send(simpleMailMessage)
    }
}