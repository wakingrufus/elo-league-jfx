package com.github.wakingrufus.eloleague.league

import com.github.wakingrufus.eloleague.game.GameItem
import com.github.wakingrufus.eloleague.game.GameModel
import com.github.wakingrufus.eloleague.isValidInt
import com.github.wakingrufus.eloleague.player.PlayerItem
import com.github.wakingrufus.eloleague.player.PlayerModel
import javafx.beans.property.ReadOnlyStringWrapper
import tornadofx.*
import java.util.*

class LeagueView : View("League View") {

    val model: LeagueModel by inject()
    val playerModel: PlayerModel by inject()
    val gameModel: GameModel by inject()


    override val root = vbox {
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
                field("teamSize") {
                    textfield(model.teamSize).validator {
                        if (isValidInt(it)) null else error("must be numeric")
                    }
                }
                tableview(model.players) {
                    column("Name", PlayerItem::nameProperty)
                    bindSelected(playerModel)
                    columnResizePolicy = SmartResize.POLICY
                }
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
                button("Add Player").setOnAction {
                    val newPlayer = PlayerItem(id = UUID.randomUUID().toString())
                    playerModel.rebind { newPlayer }
                    model.players.value.add(newPlayer)
                }
                button("Add Game").setOnAction {
                    val newGame = GameItem(id = UUID.randomUUID().toString())
                    gameModel.rebind { newGame }
                    model.games.value.add(newGame)
                }
                button("Save") {
                    enableWhen(model.dirty.and(model.valid))
                    action {
                        model.commit()
                    }
                }
            }
        }
    }

}
