package com.kevindeyne.tasker

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.PropertySource
import org.springframework.jms.annotation.EnableJms
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@PropertySource("application.properties")
open class TaskerApplication

fun main(args: Array<String>) {
	SpringApplication.run(TaskerApplication::class.java, *args)
}
