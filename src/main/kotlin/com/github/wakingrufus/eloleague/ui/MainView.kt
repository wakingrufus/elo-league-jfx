package com.github.wakingrufus.eloleague.ui

import com.github.wakingrufus.eloleague.game.GameView
import com.github.wakingrufus.eloleague.league.LeagueListView
import com.github.wakingrufus.eloleague.league.LeagueView
import com.github.wakingrufus.eloleague.player.PlayerView
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
        right {
            maxWidth = 240.px.value
            vbox {
                this += PlayerView::class
                this += GameView::class
            }
        }

    }

}
