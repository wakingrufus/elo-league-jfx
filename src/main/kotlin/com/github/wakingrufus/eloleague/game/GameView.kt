package com.github.wakingrufus.eloleague.game

import com.github.wakingrufus.eloleague.isValidInt
import com.github.wakingrufus.eloleague.league.LeagueModel
import com.github.wakingrufus.eloleague.ui.datetimepicker
import javafx.scene.control.SelectionMode
import tornadofx.*


class GameView : View("Player View") {

    val gameModel: GameModel by inject()
    val leagueModel: LeagueModel by inject()

    override val root = vbox {
        visibleWhen{
            gameModel.empty.not()
        }
        form {
            fieldset("Edit Game") {
                vbox {
                    field("time") {
                        datetimepicker(gameModel.timestamp)
                    }
                    hbox {
                        vbox {
                            field("Team 1") {
                                listview(leagueModel.players) {
                                    gameModel.team1Players.value.forEach({ playerId ->
                                        selectionModel.select(leagueModel.players.value.first { it.id == playerId.id })
                                    })
                                    gameModel.team1Players.value = (selectionModel.selectedItems)
                                    selectionModel.selectionMode = SelectionMode.MULTIPLE
                                    cellCache {
                                        label(it.nameProperty)
                                    }
                                }
                            }
                            field("team 1 score") {
                                textfield(gameModel.team1Score).validator {
                                    if (isValidInt(it)) null else error("must be numeric")
                                }
                            }
                        }
                        vbox {
                            field("Team 2") {
                                listview(leagueModel.players) {
                                    gameModel.team2Players.value.forEach({ playerId ->
                                        selectionModel.select(leagueModel.players.value.first { it.id == playerId.id })
                                    })
                                    gameModel.team2Players.value = (selectionModel.selectedItems)
                                    selectionModel.selectionMode = SelectionMode.MULTIPLE
                                    cellCache {
                                        label(it.nameProperty)
                                    }
                                }
                            }
                            field("team 2 score") {
                                textfield(gameModel.team2Score).validator {
                                    if (isValidInt(it)) null else error("must be numeric")
                                }
                            }
                        }
                    }
                    button("Save") {
                        enableWhen(gameModel.dirty.and(gameModel.valid))
                        action {
                            gameModel.commit()
                            gameModel.item = null
                        }
                    }
                }
            }
        }
    }

}
