package com.jwim.srdemo.application

import com.jwim.srdemo.application.sr.SRClient
import com.jwim.srdemo.application.sr.SRProgramDTO
import com.jwim.srdemo.entity.EpisodeDTO
import com.jwim.srdemo.entity.ShowNotFoundException
import org.springframework.stereotype.Service
import java.util.Date

@Service
class ShowService(val srClient: SRClient) {

    fun getLatestEpisodeByShowName(showName: String): EpisodeDTO {
        val show = findShowByName(showName)
        val episode = srClient.getLatestEpisodeForShow(show.id).episode

        // Not happy with this, probably would be better suited in a mapper but will keep as is for now
        val parsedEpisodeDate = convertMicrosoftJsonDateToDate(episode.publishdateutc)

        return EpisodeDTO(episode.id, episode.title, episode.description, parsedEpisodeDate)
    }

    /**
     * Returns a parsed Date from the given [microsoftDate].
     *
     * "Needed" due to SR returning a date in the Microsoft Json Date format (ex. "/Date(1623936600000)/")
     * Needed is in quotation marks as it could've been a feature to keep passing it, but I'd rather not :)
     */
    private fun convertMicrosoftJsonDateToDate(microsoftDate: String): Date {
        val regex = Regex("([0-9]+)")
        val epochDate = regex.find(microsoftDate)!!.value
        return Date(epochDate.toLong())
    }

    private fun findShowByName(showName: String): SRProgramDTO {
        return srClient.getShows().programs.find {
            it.name.equals(showName, ignoreCase = true)
        } ?: throw ShowNotFoundException()
    }
}