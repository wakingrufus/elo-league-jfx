package com.github.wakingrufus.eloleague.results

import com.github.wakingrufus.elo.League
import com.github.wakingrufus.elo.LeagueState
import com.github.wakingrufus.eloleague.TornadoFxTest
import com.github.wakingrufus.eloleague.data.PlayerData
import mu.KLogging
import org.junit.Test
import org.testfx.api.FxAssert
import org.testfx.matcher.base.NodeMatchers
import java.util.*

class ResultsViewTest : TornadoFxTest() {
    companion object : KLogging()

    @Test
    fun test() {
        showViewWithParams<ResultsView>(mapOf(
                "players" to listOf(PlayerData(id = UUID.randomUUID().toString(), name = "name")),
                "leagueState" to LeagueState(league = League(teamSize = 1))))

        FxAssert.verifyThat("#results-wrapper", NodeMatchers.isVisible())
    }

}
