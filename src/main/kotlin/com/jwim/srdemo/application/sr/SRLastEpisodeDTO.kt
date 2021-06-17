package com.jwim.srdemo.application.sr

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SRLastEpisodeDTO(
    val copyright: String,
    val episode: SREpisodeDTO,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SREpisodeDTO (
    val id: Int,
    val title: String,
    val description: String,
    val publishdateutc: String,
)

