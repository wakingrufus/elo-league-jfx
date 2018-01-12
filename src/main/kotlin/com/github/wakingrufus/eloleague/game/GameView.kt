package com.github.wakingrufus.eloleague.game

import com.github.wakingrufus.eloleague.isValidInt
import com.github.wakingrufus.eloleague.league.LeagueModel
import com.github.wakingrufus.eloleague.player.PlayerChooserView
import com.github.wakingrufus.eloleague.player.PlayerItem
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*
import java.time.format.DateTimeFormatter

class GameView : Fragment("Player View") {
    companion object : KLogging()

    val gameModel: GameModel  by inject()
    val leagueModel: LeagueModel by param()
    val onSave: (GameItem) -> Unit by param({ _ -> })

    val choosePlayer: (allPlayers: ObservableList<PlayerItem>, selectedPlayers: ObservableList<PlayerItem>) -> PlayerItem? = { allPlayers, selectedPlayers ->
        find<PlayerChooserView>(
                mapOf("players" to allPlayers.filter { lp -> !selectedPlayers.any { lp.id == it.id } }.observable())
        ).apply {
            openModal(stageStyle = StageStyle.UTILITY, block = true)
        }.choice
    }

    init {
        gameModel.team1Players.onChange { gameModel.markDirty(gameModel.team1Players) }
        gameModel.team2Players.onChange { gameModel.markDirty(gameModel.team2Players) }

    }

    override val root = form {
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
        hbox {
            fieldset("Team 1") {
                vbox {
                    style {
                        minHeight = 8.em
                    }
                    children.bind(gameModel.team1Players.value) { player: PlayerItem ->
                        field(player.name) {
                            button("Remove") {
                                action {
                                    gameModel.team1Players.value.remove(player)
                                }
                            }
                        }
                    }

                }
                button("Add Player") {
                    action {
                        gameModel.team1Players.value.add(choosePlayer(
                                leagueModel.players.value,
                                gameModel.team1Players.value))
                    }
                    /*
                        gameModel.addValidator(this, gameModel.team1Players) {
                            if (gameModel.team1Players.value.isEmpty()) {
                                tornadofx.error("team 1 required")
                            }
                            null
                        }
                        */
                }
                field("team 1 score") {
                    textfield(gameModel.team1Score).validator {
                        if (isValidInt(it)) null else error("must be numeric")
                    }
                }
            }

            fieldset("Team 2") {
                vbox {
                    style {
                        minHeight = 8.em
                    }
                    children.bind(gameModel.team2Players.value) { player: PlayerItem ->
                        field(player.name) {
                            button("Remove") {
                                action {
                                    gameModel.team2Players.value.remove(player)
                                }
                            }
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
        }
        buttonbar {
            button("Save") {
                enableWhen(gameModel.dirty.and(gameModel.valid))
                action {
                    gameModel.commit()
                    onSave(gameModel.item)
                    close()
                }
            }
            button("Cancel") {
                action {
                    gameModel.rollback()
                    close()
                }
            }
        }

    }
}
