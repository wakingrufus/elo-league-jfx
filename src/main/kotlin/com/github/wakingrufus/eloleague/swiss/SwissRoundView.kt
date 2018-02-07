package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.league.LeagueItem
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*

class SwissRoundView : Fragment() {
    companion object : KLogging()

    val swissRoundModel: SwissRoundModel by param()
    val tournament: SwissTournamentItem by param()
    val league: LeagueItem by param()

    init {
        swissRoundModel.pairings.onChange { swissRoundModel.markDirty(swissRoundModel.pairings) }
        swissRoundModel.pairings.value.forEach {
            it.gamesProperty.value.onChange {
                swissRoundModel.markDirty(swissRoundModel.pairings)
            }
        }
    }

    override val root = vbox(spacing = 1.em.value) {
        val table = tableview(items = swissRoundModel.pairings.value) {
            column<SwissPairingItem, String>("Team 1") {
                ReadOnlyStringWrapper(it.value.teamsProperty[0].name)
            }
            column<SwissPairingItem, String>("Team 2") {
                ReadOnlyStringWrapper(it.value.teamsProperty[1].name)
            }
            column<SwissPairingItem, String>("Winning Team") { cellData ->
                when {
                    cellData.value == null -> ReadOnlyStringWrapper("-")
                    else -> ReadOnlyStringWrapper(cellData.value.teamsProperty
                            .first { it.id == cellData.value.getWinningTeam() }.name)
                }
            }
        }
        button("Edit Result") {
            enableWhen { table.selectionModel.selectedItemProperty().isNotNull }
            action {
                find<SwissPairingEditView>(mapOf(
                        "pairing" to table.selectedItem,
                        "swissRoundModel" to swissRoundModel,
                        "league" to league,
                        "tournament" to tournament
                )).apply { openModal(stageStyle = StageStyle.UTILITY, block = true) }
            }
        }
        buttonbar {
            button("OK") {
                enableWhen(swissRoundModel.dirty.and(swissRoundModel.valid))
                action {
                    swissRoundModel.commit()
                    close()
                }
            }
            button("Cancel") {
                action {
                    swissRoundModel.rollback()
                    close()
                }
            }
        }
    }
}

