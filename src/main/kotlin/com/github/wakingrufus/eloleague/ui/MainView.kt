package com.github.wakingrufus.eloleague.ui

import com.github.wakingrufus.eloleague.dao.JacksonFileDataHandler
import com.github.wakingrufus.eloleague.dao.JacksonUrlDataHandler
import com.github.wakingrufus.eloleague.data.ConfigData
import com.github.wakingrufus.eloleague.league.*
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*

class MainView : View("ELO League") {
    companion object : KLogging()

    val leagueListController: LeagueListController by inject()

    override val root = borderpane {
        minHeight = 100.percent.value
        top {
            menubar {
                menu("File") {
                    item("Open File...").action {
                        FileChooser().apply {
                            extensionFilters.add(FileChooser.ExtensionFilter("JSON File", "*.json"))
                        }.run {
                            showOpenDialog(null)
                        }?.run {
                            JacksonFileDataHandler(this).readFileConfig().leagues.run {
                                if (isNotEmpty()) {
                                    leagueListController.leagues.setAll(this.map { fromData(it) })
                                }
                            }
                        }
                    }
                    item("Open Url...").action {
                        find<UrlDialog>().apply {
                            openModal(stageStyle = StageStyle.UTILITY, block = true)
                        }.url?.run {
                            JacksonUrlDataHandler(this).readFileConfig().leagues.run {
                                if (isNotEmpty()) {
                                    leagueListController.leagues.setAll(this.map { fromData(it) })
                                }
                            }
                        }
                    }
                    separator()
                    item("Save File...").action {
                        FileChooser().apply {
                            extensionFilters.add(FileChooser.ExtensionFilter("JSON File", "*.json"))
                        }.run {
                            showSaveDialog(null)
                        }?.run {
                            JacksonFileDataHandler(this).saveConfig(
                                    ConfigData(leagues = leagueListController.leagues
                                            .filterNotNull()
                                            .map { toData(it) }
                                            .toSet()))
                        }
                    }
                }
            }
        }
        left {
            this += LeagueListView::class
        }
        center {
            this += LeagueView::class
        }
    }

}
