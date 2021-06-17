package com.jwim.srdemo.application.sr

import com.google.common.base.Suppliers
import com.google.common.util.concurrent.RateLimiter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.util.concurrent.TimeUnit

@Service
class SRClient {
    private final val baseUrl = "https://api.sr.se/api/v2/"
    private final val formatParam = "&format=json"
    private final val rateLimiter = RateLimiter.create(3.0)

    private val logger = LoggerFactory.getLogger(SRClient::class.java)

    private val showSupplier = Suppliers.memoizeWithExpiration({
        getShows()
    }, 1, TimeUnit.MINUTES)

    fun getLatestEpisodeForShow(showId: Int): SRLastEpisodeDTO {
        rateLimiter.acquire()
        logger.info("Fetching latest episode for show with id $showId from SR")
        return RestTemplate()
            .getForObject(
                "$baseUrl/episodes/getlatest?programid=$showId$formatParam",
                SRLastEpisodeDTO::class.java
            ) ?: throw IOException()
    }

    fun getShowsMemoized(): SRProgramsDTO {
        return showSupplier.get()
    }

    fun getShows(): SRProgramsDTO {
        // Unlikely to change often, cache for 1 hour?
        rateLimiter.acquire()
        logger.info("Fetching all programs from SR")
        return RestTemplate()
            .getForObject(
                "$baseUrl/programs/index?pagination=false$formatParam",
                SRProgramsDTO::class.java
            ) ?: throw IOException()
    }
}