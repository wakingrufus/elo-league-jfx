package com.github.wakingrufus.eloleague.ui

import mu.KLogging
import tornadofx.*

class MainView : View("ELO League") {
    companion object : KLogging()

    override val root = borderpane {
        minHeight = 100.percent.value
        left {
            this += LeagueListView::class
        }
        center {
            this += LeagueView::class
        }

    }

}
