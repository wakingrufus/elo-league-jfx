package com.github.wakingrufus.eloleague.league

import com.github.wakingrufus.eloleague.State
import mu.KLogging
import tornadofx.*

class LeagueListView : View("League List") {
    companion object : KLogging()

    val model: LeagueModel by inject()

    init {
        State.loadFromFile()
    }

    override val root =
            vbox {
                tableview(State.localLeagues) {
                    column("Name", LeagueItem::nameProperty)
                    bindSelected(model)
                    columnResizePolicy = SmartResize.POLICY
                }
                buttonbar {
                    button(text = "New").setOnAction {
                        val newLeague = LeagueItem()
                        model.rebind { item = newLeague }
                        State.addNewLeague(newLeague)
                    }
                    button(text = "Delete") {
                        enableWhen {
                            model.empty.not()
                        }
                        action {
                            State.removeLeague(model.item)
                        }
                    }
                    button(text = "Save All").setOnAction {
                        log.info("saving data")
                        State.saveToFile()
                    }
                }
            }
}
