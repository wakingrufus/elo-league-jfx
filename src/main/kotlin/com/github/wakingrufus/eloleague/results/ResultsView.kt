package com.github.wakingrufus.eloleague.results

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import mu.KLogging
import tornadofx.*

class ResultsView : Fragment() {
    companion object : KLogging()

    val leagueResultItem: LeagueResultItem by param()
    val gameResults: ObservableList<GameResultItem> = FXCollections.observableArrayList()

    override val root = borderpane {

        left {
            id = "results-wrapper"

            val table = tableview(leagueResultItem.players) {
                column("Name", PlayerResultItem::nameProperty)
                column("Rating", PlayerResultItem::currentRatingProperty)
                column("Games", PlayerResultItem::gamesPlayedProperty)
                column("Wins", PlayerResultItem::winsProperty)
                column("Losses", PlayerResultItem::lossesProperty)


                columnResizePolicy = SmartResize.POLICY
            }

            table.columns[1].sortType = javafx.scene.control.TableColumn.SortType.ASCENDING
            table.columns[1].sortableProperty().set(true)
            table.sortOrder.add(table.columns[1])
            table.sort()

            table.onSelectionChange { player ->
                gameResults.setAll(leagueResultItem.games.filter {
                    it.player.id == player?.idProperty?.value
                })
            }

        }
        center {
            this += find<ResultsDetailsView>(mapOf("gameResults" to gameResults))
        }
    }

}