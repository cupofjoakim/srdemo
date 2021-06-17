package com.jwim.srdemo.application.sr

import com.google.common.base.Suppliers
import com.google.common.util.concurrent.RateLimiter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.util.concurrent.TimeUnit

@Service
class SRClient(val restTemplate: RestTemplate) {
    private final val baseUrl = "https://api.sr.se/api/v2"
    private final val formatParam = "&format=json"
    private final val rateLimiter = RateLimiter.create(3.0)
    private val logger = LoggerFactory.getLogger(SRClient::class.java)
    private val showSupplier = Suppliers.memoizeWithExpiration({
        getShowsFromApi()
    }, 1, TimeUnit.MINUTES)

    fun getLatestEpisodeForShow(showId: Int): SRLastEpisodeDTO {
        rateLimiter.acquire()
        logger.info("Fetching latest episode for show with id $showId from SR")
        return restTemplate
            .getForObject(
                "$baseUrl/episodes/getlatest?programid=$showId$formatParam",
                SRLastEpisodeDTO::class.java
            ) ?: throw IOException()
    }

    fun getShows(): SRProgramsDTO {
        return showSupplier.get()
    }

    private fun getShowsFromApi(): SRProgramsDTO {
        rateLimiter.acquire()
        logger.info("Fetching all programs from SR")
        return restTemplate
            .getForObject(
                "$baseUrl/programs/index?pagination=false$formatParam",
                SRProgramsDTO::class.java
            ) ?: throw IOException()
    }
}