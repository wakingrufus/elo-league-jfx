package com.github.wakingrufus.eloleague.results

import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*

class ResultsView : Fragment() {
    companion object : KLogging()

    val trialPeriod: Int by param(0)
    val leagueResultItem: LeagueResultItem by param()
    val gameResults: ObservableList<GameResultItem> = FXCollections.observableArrayList()
    lateinit var playerList: SortedFilteredList<PlayerResultItem>

    override val root = borderpane {
        if (!this@ResultsView::playerList.isInitialized) {
            playerList = SortedFilteredList(leagueResultItem.players)
        }
        center {
            id = "results-wrapper"

            val table = tableview(playerList) {
                id = "results-tableview"
                column("Name", PlayerResultItem::nameProperty)
                column("Rating", PlayerResultItem::currentRatingProperty)
                column("Games", PlayerResultItem::gamesPlayedProperty)
                column("Wins", PlayerResultItem::winsProperty)
                column("Losses", PlayerResultItem::lossesProperty)


                columnResizePolicy = SmartResize.POLICY
            }

            table.columns[1].sortType = javafx.scene.control.TableColumn.SortType.DESCENDING
            table.columns[1].sortableProperty().set(true)
            table.sortOrder.add(table.columns[1])

            table.sort()

            table.onSelectionChange { player ->
                gameResults.setAll(leagueResultItem.games.filter {
                    it.player.id == player?.idProperty?.value
                })
            }

        }
        bottom {
            buttonbar {
                button("View Details") {
                    enableWhen { Bindings.isNotEmpty(gameResults) }
                    setOnAction {
                        find<ResultsDetailsView>(mapOf("gameResults" to gameResults)).apply {
                            openModal(
                                    stageStyle = StageStyle.UTILITY,
                                    block = true
                            )
                        }
                    }
                }
                button("Filter out trial players") {
                    id = "filter-newbies-button"
                    setOnAction {
                        playerList.predicate = { it.gamesPlayedProperty >= trialPeriod }
                    }
                }
            }
        }
    }
}
