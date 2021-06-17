package com.jwim.srdemo.api.v1

import com.jwim.srdemo.application.ShowService
import com.jwim.srdemo.entity.EpisodeDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/shows")
class ShowResource(val showService: ShowService) {

    @GetMapping("/{showName}/latest")
    fun getLatestEpisodeByShowName(@PathVariable showName: String): EpisodeDTO {
        return showService.getLatestEpisodeByShowName(showName)
    }
}

