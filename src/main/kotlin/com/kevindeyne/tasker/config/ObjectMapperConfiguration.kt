package com.kevindeyne.tasker.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType

@Configuration
open class ObjectMapperConfiguration() {

	@Bean
	@Primary
	open fun objectMapper() = ObjectMapper().apply {
		registerModule(KotlinModule())
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
	}

	@Bean
	@Primary
	open fun jacksonBuilder(): Jackson2ObjectMapperBuilder {
		val jacksonMapperBuilder = Jackson2ObjectMapperBuilder()
				.failOnUnknownProperties(false)
				.modules(JavaTimeModule(), KotlinModule())
				.propertyNamingStrategy(PropertyNamingStrategy.SnakeCaseStrategy.SNAKE_CASE)
		return jacksonMapperBuilder
	}

	@Bean
	@Primary
	open fun jacksonJmsMessageConverter(): MessageConverter {
		var converter : MappingJackson2MessageConverter = MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		converter.setObjectMapper(objectMapper());
		return converter;
	}

}