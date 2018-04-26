package com.kevindeyne.tasker.config

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import org.springframework.cloud.aws.mail.simplemail.SimpleEmailServiceJavaMailSender
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender

@Configuration
open class MailConfig {

	@Bean
	fun amazonSimpleEmailService(credentialsProvider: AWSCredentialsProvider): AmazonSimpleEmailService {
		return AmazonSimpleEmailServiceClientBuilder.standard()
				.withCredentials(credentialsProvider)
				.withRegion(Regions.US_WEST_2).build()
	}

	@Bean
	fun mailSender(ses: AmazonSimpleEmailService): JavaMailSender {
		return SimpleEmailServiceJavaMailSender(ses)
	}
}