package com.github.wakingrufus.eloleague.league

import com.github.wakingrufus.elo.calculateNewLeague
import com.github.wakingrufus.eloleague.game.GameItem
import com.github.wakingrufus.eloleague.game.GameModel
import com.github.wakingrufus.eloleague.game.toGameData
import com.github.wakingrufus.eloleague.isValidInt
import com.github.wakingrufus.eloleague.player.PlayerItem
import com.github.wakingrufus.eloleague.player.PlayerModel
import com.github.wakingrufus.eloleague.results.*
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*
import java.util.*

class LeagueView : View("League View") {
    companion object : KLogging()

    val model: LeagueModel by inject()
    val playerModel: PlayerModel by inject()
    val gameModel: GameModel by inject()

    override val root = vbox {
        visibleWhen {
            model.empty.not()
        }
        form {
            fieldset("Edit League") {
                field("Name") {
                    textfield(model.name)
                }
                field("Starting Rating") {
                    textfield(model.startingRating).validator {
                        if (isValidInt(it)) null else error("must be numeric")
                    }
                }
                field("xi") {
                    textfield(model.xi).validator {
                        if (isValidInt(it)) null else error("must be numeric")
                    }
                }
                field("K-Factor Base") {
                    textfield(model.kFactorBase).validator {
                        if (isValidInt(it)) null else error("must be numeric")
                    }
                }
                field("Trial Period") {
                    textfield(model.trialPeriod).validator {
                        if (isValidInt(it)) null else error("must be numeric")
                    }
                }
                field("trialKFactorMultiplier") {
                    textfield(model.trialKFactorMultiplier).validator {
                        if (isValidInt(it)) null else error("must be numeric")
                    }
                }
                hbox {
                    button("Save") {
                        enableWhen(model.dirty.and(model.valid))
                        action {
                            model.commit()
                            model.item = null
                        }
                    }
                    button("Cancel") {
                        action {
                            model.rollback()
                            model.item = null
                        }
                    }
                }
                fieldset("Players") {
                    tableview(model.players) {
                        column("Name", PlayerItem::nameProperty)
                        bindSelected(playerModel)
                        columnResizePolicy = SmartResize.POLICY
                    }
                    button("Add Player").setOnAction {
                        val newPlayer = PlayerItem(id = UUID.randomUUID().toString())
                        playerModel.rebind { item = newPlayer }
                        model.players.value.add(newPlayer)
                    }
                }
                fieldset("Games") {
                    tableview(model.games) {
                        column("Time", GameItem::timestamp)
                        column<GameItem, String>("Team 1") {
                            ReadOnlyStringWrapper(it.value.team1Players.
                                    joinToString(transform = { player -> player.name }))
                        }
                        column("Team 1 score", GameItem::team1ScoreProperty)
                        column<GameItem, String>("Team 2") {
                            ReadOnlyStringWrapper(it.value.team2Players.
                                    joinToString(transform = { player -> player.name }))
                        }
                        column("Team 2 score", GameItem::team2ScoreProperty)
                        bindSelected(gameModel)
                        columnResizePolicy = SmartResize.POLICY
                    }
                    button("Add Game").setOnAction {
                        val newGame = GameItem(id = UUID.randomUUID().toString())
                        gameModel.rebind { item = newGame }
                        model.games.value.add(newGame)
                    }
                }
                button("View Results").setOnAction {
                    val games = games(model.games.value.map { toGameData(it) })
                    val modal: ResultsView =
                            find<ResultsView>(mapOf(
                                    "leagueResultItem" to  results(
                                            leagueItem = model.item,
                                            leagueState = calculateNewLeague(
                                                    league = league(toData(model.item)),
                                                    games = games)))).apply {
                                openModal(
                                        stageStyle = StageStyle.UTILITY,
                                        block = true
                                )
                            }
                }
            }
        }
    }

}
