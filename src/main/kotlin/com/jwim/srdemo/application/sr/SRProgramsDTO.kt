package com.jwim.srdemo.application.sr

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SRProgramDTO (
    val id: Int = 0,
    val name: String = ""
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SRProgramsDTO (
    val copyright: String = "",
    val programs: List<SRProgramDTO> = listOf(),
)