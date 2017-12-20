package com.github.wakingrufus.eloleague.results

import com.github.wakingrufus.eloleague.TornadoFxTest
import mu.KLogging
import org.junit.Test
import org.testfx.api.FxAssert
import org.testfx.matcher.base.NodeMatchers

class ResultsViewTest : TornadoFxTest() {
    companion object : KLogging()

    @Test
    fun test() {
        val results: LeagueResultItem = LeagueResultItem()
        showViewWithParams<ResultsView>(mapOf(
                "leagueResultItem" to results
        ))

        FxAssert.verifyThat("#results-wrapper", NodeMatchers.isVisible())
    }

}
