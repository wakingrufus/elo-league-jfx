package com.github.wakingrufus.eloleague.results

import javafx.beans.binding.Bindings
import javafx.beans.property.ReadOnlyListWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.ObservableList
import mu.KLogging
import tornadofx.*

class ResultsDetailsView : Fragment() {
    companion object : KLogging()

    val gameResults: ObservableList<GameResultItem> by param()

    override val root = vbox {
        id = "result-details-wrapper"
        visibleWhen { Bindings.isNotEmpty(gameResults) }
        val table = tableview(ReadOnlyListWrapper(gameResults)) {
            column("time", GameResultItem::entryDate)
            column<GameResultItem, String>("Player") { it.value.player.nameProperty }
            column("starting Rating", GameResultItem::startingRating)
            column("Adjustment", GameResultItem::ratingAdjustment)
            val t1 = column<GameResultItem, String>("Team 1") {
                ReadOnlyStringWrapper(it.value.team1Players.
                        joinToString(transform = { player -> player.name }))
            }
            t1.id = "team-1"
            column("Score", GameResultItem::team1Score)
            val t2 = column<GameResultItem, String>("Team 2") {
                ReadOnlyStringWrapper(it.value.team2Players.
                        joinToString(transform = { player -> player.name }))
            }
            t2.id = "team-2"
            column("Score", GameResultItem::team2Score)
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