package com.github.wakingrufus.eloleague.game

import com.github.wakingrufus.eloleague.isValidInt
import com.github.wakingrufus.eloleague.league.LeagueModel
import com.github.wakingrufus.eloleague.player.PlayerItem
import com.github.wakingrufus.eloleague.player.PlayerChooserView
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*
import java.time.format.DateTimeFormatter

class GameView : View("Player View") {
    companion object : KLogging()

    val gameModel: GameModel by inject()
    val leagueModel: LeagueModel by inject()

    val choosePlayer: (allPlayers: ObservableList<PlayerItem>, selectedPlayers: ObservableList<PlayerItem>) -> PlayerItem? = { allPlayers, selectedPlayers ->
        find<PlayerChooserView>(
                mapOf("players" to allPlayers.filter { lp -> !selectedPlayers.any { lp.id == it.id } }.observable())
        ).apply {
            openModal(stageStyle = StageStyle.UTILITY, block = true)
        }.choice
    }

    override val root = form {
        visibleWhen {
            gameModel.empty.not()
        }
        fieldset("Edit Game") {
            field("time") {
                textfield(gameModel.timestamp).validator {
                    try {
                        DateTimeFormatter.ISO_DATE_TIME.parse(it)
                    } catch (e: Exception) {
                        error("Invalid date")
                    }
                    null
                }
            }

        }
        fieldset("Team 1") {
            vbox {
                gameModel.team1Players.onChange {
                    if (gameModel.isNotEmpty) {
                        this.children.setAll(
                                gameModel.team1Players.value.map {
                                    field(it.name) {
                                        button("Remove") {
                                            action {
                                                gameModel.team1Players.value.remove(it)
                                            }
                                        }
                                    }
                                }
                        )
                    }
                }

            }
            button("Add Player") {
                action {
                    gameModel.team1Players.value.add(choosePlayer(
                            leagueModel.players.value,
                            gameModel.team1Players.value))
                }
            }
            field("team 1 score") {
                textfield(gameModel.team1Score).validator {
                    if (isValidInt(it)) null else error("must be numeric")
                }
            }
        }

        fieldset("Team 2") {
            vbox {
                gameModel.team2Players.onChange {
                    if (gameModel.isNotEmpty) {
                        this.children.setAll(
                                gameModel.team2Players.value.map {
                                    field(it.name) {
                                        button("Remove") {
                                            action {
                                                gameModel.team2Players.value.remove(it)
                                            }
                                        }
                                    }
                                }
                        )
                    }
                }

            }
            button("Add Player").setOnAction {
                gameModel.team2Players.value.add(choosePlayer(
                        leagueModel.players.value,
                        gameModel.team2Players.value))
            }
            field("team 2 score") {
                textfield(gameModel.team2Score).validator {
                    if (isValidInt(it)) null else error("must be numeric")
                }
            }
        }

        buttonbar {
            button("Save") {
                enableWhen(gameModel.dirty.and(gameModel.valid))
                action {
                    gameModel.commit()
                    gameModel.item = null
                }
            }
            button("Cancel") {
                action {
                    gameModel.rollback()
                    gameModel.item = null
                }
            }
        }

    }
}
