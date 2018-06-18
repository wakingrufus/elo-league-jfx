package com.github.wakingrufus.eloleague.results

import com.github.wakingrufus.eloleague.TornadoFxTest
import javafx.scene.control.TableView
import mu.KLogging
import org.junit.Test
import org.testfx.api.FxAssert
import org.testfx.matcher.base.NodeMatchers

class ResultsViewTest : TornadoFxTest() {
    companion object : KLogging()

    @Test
    fun test() {
        val results: LeagueResultItem = LeagueResultItem().apply {
            players.add(PlayerResultItem(id = "1", name = "1", gamesPlayed = 0, losses = 0, wins = 0, currentRating = 1500))
            players.add(PlayerResultItem(id = "2", name = "2", gamesPlayed = 1, losses = 1, wins = 0, currentRating = 1490))
            players.add(PlayerResultItem(id = "3", name = "3", gamesPlayed = 2, losses = 1, wins = 1, currentRating = 1501))
        }
        showViewWithParams<ResultsView>(mapOf(
                "trialPeriod" to 1,
                "leagueResultItem" to results
        ))

        FxAssert.verifyThat("#results-wrapper", NodeMatchers.isVisible())
        FxAssert.verifyThat<TableView<PlayerResultItem>>("#results-tableview", { it.items.size == 3 })
        clickOn("#filter-newbies-button")
        FxAssert.verifyThat<TableView<PlayerResultItem>>("#results-tableview", { it.items.size == 2 })
    }

}
