package com.jwim.srdemo.entity

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No show with that name was found")
class ShowNotFoundException: RuntimeException()