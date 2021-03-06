package com.github.wakingrufus.eloleague.results

import javafx.beans.property.ReadOnlyListWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.ObservableList
import mu.KLogging
import tornadofx.*

class ResultsDetailsView : Fragment() {
    companion object : KLogging()

    val gameResults: ObservableList<GameResultItem> by param()

    override val root = stackpane {
        id = "result-details-wrapper"
        val table = tableview(ReadOnlyListWrapper(gameResults)) {
            readonlyColumn("time", GameResultItem::entryDate)
            column<GameResultItem, String>("Player") { it.value.player.nameProperty }
            readonlyColumn("starting Rating", GameResultItem::startingRating)
            readonlyColumn("Adjustment", GameResultItem::ratingAdjustment)
            val t1 = column<GameResultItem, String>("Team 1") {
                ReadOnlyStringWrapper(it.value.team1Players.
                        joinToString(transform = { player -> player.name }))
            }
            t1.id = "team-1"
            readonlyColumn("Score", GameResultItem::team1Score)
            val t2 = column<GameResultItem, String>("Team 2") {
                ReadOnlyStringWrapper(it.value.team2Players.
                        joinToString(transform = { player -> player.name }))
            }
            t2.id = "team-2"
            readonlyColumn("Score", GameResultItem::team2Score)
            columnResizePolicy = SmartResize.POLICY
        }

        table.columns[0].sortType = javafx.scene.control.TableColumn.SortType.ASCENDING
        table.columns[0].sortableProperty().set(true)
        table.sortOrder.add(table.columns[0])
        table.sort()
        gameResults.onChange {
            it.next()
            if (it.wasAdded()) {
                table.sort()
            }
        }
    }
}