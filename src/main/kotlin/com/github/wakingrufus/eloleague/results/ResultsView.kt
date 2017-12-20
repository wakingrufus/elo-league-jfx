package com.github.wakingrufus.eloleague.results

import mu.KLogging
import tornadofx.*

class ResultsView : Fragment() {
    companion object : KLogging()

    val leagueResultItem: LeagueResultItem by param()

    override val root = vbox {
        id = "results-wrapper"

        val table = tableview(leagueResultItem.players) {
            column("Name", PlayerResultItem::nameProperty)
            column("Rating", PlayerResultItem::currentRatingProperty)
            column("Games", PlayerResultItem::gamesPlayedProperty)
            column("Wins", PlayerResultItem::winsProperty)
            column("Losses", PlayerResultItem::lossesProperty)


        }

        table.columns[1].sortType = javafx.scene.control.TableColumn.SortType.DESCENDING
        table.columns[1].sortableProperty().set(true)
        table.sortOrder.add(table.columns[1])
        table.sort()
    }


}