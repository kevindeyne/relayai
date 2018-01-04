package com.kevindeyne.tasker.config

import com.kevindeyne.tasker.interceptors.CommonInterceptor
import com.kevindeyne.tasker.repositories.IssueRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
open class WebMVCConfig : WebMvcConfigurer {
	
	@Autowired
	lateinit var issueRepository : IssueRepository
	
	override fun addInterceptors(registry : InterceptorRegistry) {
		registry.addInterceptor(CommonInterceptor(issueRepository))
	}

}