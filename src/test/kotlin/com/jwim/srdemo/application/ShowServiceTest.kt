package com.jwim.srdemo.application

import com.jwim.srdemo.application.sr.*
import com.jwim.srdemo.entity.ShowNotFoundException
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito
import org.springframework.web.client.RestTemplate

internal class ShowServiceTest() {
    private val expectedGetShowURL = "https://api.sr.se/api/v2/programs/index?pagination=false&format=json"

    @Test
    fun getLatestEpisodeByShowName_throwsWhenNonExistentShow() {
        val exampleSRProgramsDTO = SRProgramsDTO("A copyright notice", listOf())
        val restTemplate = Mockito.mock(RestTemplate::class.java)
        val srClient = SRClient(restTemplate)
        val showService = ShowService(srClient)

        Mockito.`when`(restTemplate.getForObject(expectedGetShowURL, SRProgramsDTO::class.java))
            .thenReturn(exampleSRProgramsDTO)

        assertThrows(ShowNotFoundException::class.java) {
            showService.getLatestEpisodeByShowName("A non existent show name")
        }
    }

    @Test
    fun getLatestEpisodeByShowName_ignoresCasing() {
        val exampleSRProgramsDTO = SRProgramsDTO("A copyright notice", listOf(
            SRProgramDTO(1, "Mammas Nya Kille")
        ))
        val exampleSRLastEpisodeDTO = SRLastEpisodeDTO("A copyright notice", SREpisodeDTO(
            2,
            "A nice show title!",
            "This will be fun",
            "/Date(1623936600000)/"
        ))
        val restTemplate = Mockito.mock(RestTemplate::class.java)
        val srClient = SRClient(restTemplate)
        val showService = ShowService(srClient)

        Mockito.`when`(restTemplate.getForObject(expectedGetShowURL, SRProgramsDTO::class.java))
            .thenReturn(exampleSRProgramsDTO)
        Mockito.`when`(restTemplate.getForObject("https://api.sr.se/api/v2/episodes/getlatest?programid=1&format=json", SRLastEpisodeDTO::class.java))
            .thenReturn(exampleSRLastEpisodeDTO)

        val episode = showService.getLatestEpisodeByShowName("mammas nya kille")

        assertEquals(exampleSRLastEpisodeDTO.episode.title, episode.title)
    }
}
