package com.github.wakingrufus.eloleague.league

import com.github.wakingrufus.elo.calculateNewLeague
import com.github.wakingrufus.eloleague.game.*
import com.github.wakingrufus.eloleague.player.PlayerItem
import com.github.wakingrufus.eloleague.player.PlayerModel
import com.github.wakingrufus.eloleague.player.PlayerView
import com.github.wakingrufus.eloleague.results.ResultsView
import com.github.wakingrufus.eloleague.results.games
import com.github.wakingrufus.eloleague.results.league
import com.github.wakingrufus.eloleague.results.results
import com.github.wakingrufus.eloleague.swiss.SwissTournamentItem
import com.github.wakingrufus.eloleague.swiss.SwissTournamentModel
import com.github.wakingrufus.eloleague.swiss.SwissView
import javafx.scene.control.TabPane
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*
import java.util.*

class LeagueView : View("League View") {
    companion object : KLogging()

    val model: LeagueModel by inject()
    val playerModel: PlayerModel by inject()
    val gameModel: GameModel by inject()
    val tournamentModel: SwissTournamentModel by inject()

    override val root = vbox {
        visibleWhen {
            model.empty.not()
        }
        form {
            hbox {
                this += LeagueSettingsView::class
                fieldset("Players") {
                    tableview(model.players) {
                        column("Name", PlayerItem::nameProperty)
                        bindSelected(playerModel)
                        columnResizePolicy = SmartResize.POLICY
                    }
                    buttonbar {
                        button("Add Player").setOnAction {
                            val newPlayer = PlayerItem(id = UUID.randomUUID().toString())
                            playerModel.rebind { item = newPlayer }
                            find<PlayerView>().apply {
                                openModal(stageStyle = StageStyle.UTILITY, block = true)
                            }
                        }
                        button("Edit Player").setOnAction {
                            find<PlayerView>().apply {
                                openModal(stageStyle = StageStyle.UTILITY, block = true)
                            }
                        }
                    }
                }
            }
            tabpane {
                tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
                tab("Games") {
                    this += GameListView::class
                }
                tab("tournaments") {
                    //   visibleWhen(false.toProperty())
                    fieldset("Tournaments") {
                        val tournamentTable = tableview(model.tournaments) {
                            column("Time", SwissTournamentItem::startTime)
                            column("Name", SwissTournamentItem::name)
                            bindSelected(tournamentModel)
                            columnResizePolicy = SmartResize.POLICY
                        }
                        tournamentTable.columns[0].sortType = javafx.scene.control.TableColumn.SortType.DESCENDING
                        tournamentTable.columns[0].sortableProperty().set(true)
                        tournamentTable.sortOrder.add(tournamentTable.columns[0])
                        tournamentTable.sort()
                        model.tournaments.value.onChange {
                            while (it.next()) {
                                if (it.wasAdded()) {
                                    tournamentTable.sort()
                                }
                            }
                        }

                        buttonbar {
                            button("New Tournament").setOnAction {
                                tournamentModel.rebind { item = SwissTournamentItem() }
                                find<SwissView>(mapOf(
                                        "tournament" to tournamentModel
                                )).apply {
                                    openModal(stageStyle = StageStyle.UTILITY, block = false)
                                }
                            }
                            button("Edit Tournament").setOnAction {
                                find<SwissView>(mapOf(
                                        "tournament" to tournamentModel
                                )).apply {
                                    openModal(stageStyle = StageStyle.UTILITY, block = false)
                                }
                            }
                        }
                    }
                }
            }


            button("View Results").setOnAction {
                val games = games(model.games.value.map(GameItem::toData))
                find<ResultsView>(mapOf(
                        "leagueResultItem" to results(
                                leagueItem = model.item,
                                leagueState = calculateNewLeague(
                                        league = league(toData(model.item)),
                                        games = games)))).apply {
                    openModal(
                            stageStyle = StageStyle.UTILITY,
                            block = false
                    )
                }
            }

        }
    }

}
