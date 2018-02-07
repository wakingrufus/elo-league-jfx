package com.github.wakingrufus.eloleague.game

import com.github.wakingrufus.eloleague.isValidInt
import com.github.wakingrufus.eloleague.league.LeagueModel
import com.github.wakingrufus.eloleague.player.PlayerItem
import com.github.wakingrufus.eloleague.player.PlayerListBuilder
import mu.KLogging
import tornadofx.*
import java.time.format.DateTimeFormatter

class GameView : Fragment("Edit Game") {
    companion object : KLogging()

    val gameModel: GameModel  by inject()
    val leagueModel: LeagueModel by inject()

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
                this += find<PlayerListBuilder>(mapOf(
                        "selectedPlayers" to gameModel.team1Players.value,
                        "otherIneligiblePlayers" to { gameModel.team2Players.value.map(PlayerItem::id) },
                        "allPlayers" to leagueModel.players.value
                ))
                field("team 1 score") {
                    textfield(gameModel.team1Score).validator {
                        if (isValidInt(it)) null else error("must be numeric")
                    }
                }
            }

            fieldset("Team 2") {
                this += find<PlayerListBuilder>(mapOf(
                        "selectedPlayers" to gameModel.team2Players.value,
                        "otherIneligiblePlayers" to { gameModel.team1Players.value.map(PlayerItem::id) },
                        "allPlayers" to leagueModel.players.value
                ))
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
                    if (leagueModel.games.value.none { it.id == gameModel.id.value }) {
                        leagueModel.games.value.addAll(gameModel.item)
                    }
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
