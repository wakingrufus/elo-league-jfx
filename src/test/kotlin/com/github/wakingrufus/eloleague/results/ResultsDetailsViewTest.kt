package com.github.wakingrufus.eloleague.results

import com.github.wakingrufus.eloleague.TornadoFxTest
import com.github.wakingrufus.eloleague.player.PlayerItem
import com.github.wakingrufus.eloleague.printNodes
import com.github.wakingrufus.eloleague.waitFor
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.Parent
import mu.KLogging
import org.junit.Test
import org.testfx.api.FxAssert
import org.testfx.matcher.base.NodeMatchers
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class ResultsDetailsViewTest : TornadoFxTest() {
    companion object : KLogging()

    @Test
    fun test() {
        val result1 = GameResultItem(
                id = UUID.randomUUID().toString(),
                player = PlayerItem(id = UUID.randomUUID().toString(), name = "Adam"),
                ratingAdjustment = 1,
                entryDate = DateTimeFormatter.ISO_DATE_TIME.format(Instant.now().atOffset(ZoneOffset.UTC)),
                win = true,
                startingRating = 1500,
                team1Score = 5,
                team2Score = 3,
                team1Players = listOf(
                        PlayerItem(id = UUID.randomUUID().toString(), name = "Adam"),
                        PlayerItem(id = UUID.randomUUID().toString(), name = "Beth")),
                team2Players = listOf(
                        PlayerItem(id = UUID.randomUUID().toString(), name = "Alice"),
                        PlayerItem(id = UUID.randomUUID().toString(), name = "Bob"))
        )
        val resultDetails: ObservableList<GameResultItem> = FXCollections.observableArrayList(
                result1
        )
        showViewWithParams<ResultsDetailsView>(mapOf(
                "gameResults" to resultDetails
        ))
        waitFor({scene!!.root.childrenUnmodifiable[0].isVisible})
        FxAssert.verifyThat("#result-details-wrapper", NodeMatchers.isVisible())
        printNodes(scene!!.root)
        FxAssert.verifyThat("#result-details-wrapper .table-view .table-row-cell #team-1",
                NodeMatchers.hasText("Adam, Beth"))
        FxAssert.verifyThat("#result-details-wrapper .table-view .table-row-cell #team-2",
                NodeMatchers.hasText("Alice, Bob"))
    }
}
