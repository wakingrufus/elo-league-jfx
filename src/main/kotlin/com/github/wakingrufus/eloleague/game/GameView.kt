package com.github.wakingrufus.eloleague.game

import com.github.wakingrufus.eloleague.isValidInt
import com.github.wakingrufus.eloleague.league.LeagueModel
import com.github.wakingrufus.eloleague.player.PlayerItem
import com.github.wakingrufus.eloleague.ui.datetimepicker
import javafx.collections.ObservableList
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import tornadofx.*


class GameView : View("Player View") {

    val gameModel: GameModel by inject()
    val leagueModel: LeagueModel by inject()

    var team1List: ListView<PlayerItem>? = null


    override val root = vbox {
        visibleWhen {
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
                                team1List = listview(leagueModel.players) {
                                    selectionModel.selectionMode = SelectionMode.MULTIPLE
                                    gameModel.team1Players.value.forEach { selectionModel.select(it) }
                                    selectionModel.selectedItems.onChange {
                                        gameModel.team1Players.setValue(it.list as ObservableList<PlayerItem>)
                                    }

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
                                    selectionModel.selectionMode = SelectionMode.MULTIPLE
                                    gameModel.team2Players.value.forEach { selectionModel.select(it) }
                                    selectionModel.selectedItems.onChange {
                                        gameModel.team2Players.setValue(it.list as ObservableList<PlayerItem>)
                                    }
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
                    hbox {
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
        }
    }

}
