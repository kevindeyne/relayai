package com.kevindeyne.tasker.config

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component


@Component
open class Encoder: BCryptPasswordEncoder(11) {

}