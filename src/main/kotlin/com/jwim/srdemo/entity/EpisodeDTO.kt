package com.jwim.srdemo.entity

import java.util.Date

data class EpisodeDTO (
    val id: Int,
    val title: String,
    val description: String,
    val publishedAt: Date,
)
