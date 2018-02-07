package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.league.LeagueItem
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*

class SwissPairingEditView : Fragment() {
    companion object : KLogging()

    val pairing: SwissPairingItem by param()
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

        hbox(spacing = 1.em.value) {
            minHeight = 4.em.value

            this += find<SwissPairingView>(mapOf(
                    "pairing" to pairing
            ))

            button("Add Game").setOnAction {
                tournament.rounds.flatMap {
                    it.pairingsProperty.flatMap {
                        it.gamesProperty.map { it.idProperty.value }
                    }
                }.let { tournamentGameIds ->
                            find<GamePicker>(mapOf(
                                    "games" to league.games
                                            .filter {
                                                gameMatchesPairing(it, pairing)
                                            }.filter {
                                                !tournamentGameIds.contains(it.id)
                                            }.filter {
                                                !pairing.gamesProperty.map { it.idProperty.value }.contains(it.id)
                                            }
                            )).apply { openModal(stageStyle = StageStyle.UTILITY, block = true) }.also {
                                it.selectedGame.value?.let {
                                    val winningPlayers = if (it.team1Score > it.team2Score) it.team1Players else it.team2Players
                                    SwissGameItem(
                                            gameId = it.id,
                                            winningTeamId = pairing.teamsProperty
                                                    .first {
                                                        it.players.map { it.id }
                                                                .containsAll(winningPlayers.map { it.id })
                                                    }.id
                                    ).apply {
                                        pairing.gamesProperty.add(this)
                                    }
                                }
                            }
                        }
            }
            button("Add Incomplete Game") {
                action {
                    pairing.gamesProperty.add(SwissGameItem(gameId = null, winningTeamId = null))
                }
            }
            button("Clear Games").setOnAction {
                pairing.gamesProperty.clear()
            }
        }


    }
}

