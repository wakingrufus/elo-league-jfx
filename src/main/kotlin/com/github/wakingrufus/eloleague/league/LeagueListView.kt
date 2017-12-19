package com.github.wakingrufus.eloleague.league

import mu.KLogging
import tornadofx.*

class LeagueListView : View("League List") {
    companion object : KLogging()

    val controller: LeagueListController by inject()

    val model: LeagueModel by inject()

    override val root =
            vbox {
                tableview(controller.leagues) {
                    column("Name", LeagueItem::nameProperty)
                    bindSelected(model)
                    columnResizePolicy = SmartResize.POLICY
                }
                button(text = "New").setOnAction {
                    val newLeague = controller.newLeague()
                    model.rebind { item = newLeague }
                    controller.leagues.add(newLeague)
                }
                button(text = "Delete").setOnAction {
                    enableWhen{
                        model.empty.not()
                    }
                    controller.leagues.remove(model.item)
                }
                button(text = "Save All").setOnAction {
                    controller.save()
                }

            }
}
