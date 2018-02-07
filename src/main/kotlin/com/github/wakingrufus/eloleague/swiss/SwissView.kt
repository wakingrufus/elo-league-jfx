package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.league.LeagueModel
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*

class SwissView : Fragment("Swiss Tournament") {
    companion object : KLogging()

    val league: LeagueModel by inject()
    val tournament: SwissTournamentModel by param()
    val teamModel: SwissTeamModel by inject()
    val swissRound: SwissRoundModel by inject()

    init {
        tournament.teams.onChange { tournament.markDirty(tournament.teams) }
        tournament.rounds.onChange { tournament.markDirty(tournament.rounds) }
    }

    override val root = form {
        vbox {
            hbox {
                this += find<TournamentSettingsView>(mapOf(
                        "tournament" to tournament,
                        "league" to league.item
                ))

                fieldset("Teams") {
                    val teamsTable = tableview(tournament.teams) {
                        column("Name", SwissTeamItem::name)
                        column<SwissTeamItem, String>("Members") {
                            ReadOnlyStringWrapper(it.value.players.joinToString(transform = { player -> player.name }))
                        }
                        bindSelected(teamModel)
                        columnResizePolicy = SmartResize.POLICY
                    }
                    teamsTable.columns[0].sortType = javafx.scene.control.TableColumn.SortType.DESCENDING
                    teamsTable.columns[0].sortableProperty().set(true)
                    teamsTable.sortOrder.add(teamsTable.columns[0])
                    teamsTable.sort()
                    tournament.teams.value.onChange {
                        while (it.next()) {
                            if (!it.wasPermutated()) {
                                teamsTable.sort()
                            }
                        }
                    }

                    buttonbar {
                        button("New Team").setOnAction {
                            teamModel.rebind { item = SwissTeamItem() }
                            editTeam()
                        }
                        button("Edit Team").setOnAction {
                            editTeam()
                        }
                    }
                }
            }
            fieldset("Rounds") {
                tableview(tournament.rounds) {
                    column("Number", SwissRoundItem::roundNumberProperty)
                    column<SwissRoundItem, String>("Progress") {
                        ReadOnlyStringWrapper(it.value.pairingsProperty.value
                                .filter { it.gamesProperty.isNotEmpty() }
                                .count().toString()
                                + " / " + it.value.pairingsProperty.size)
                    }
                    bindSelected(swissRound)
                    columnResizePolicy = SmartResize.POLICY
                }
                buttonbar {
                    button("Edit Round").setOnAction {
                        enableWhen(swissRound.empty.not())
                        editRound()
                    }
                    button("Next Round").setOnAction {
                        tournament.rounds.value.add(if (tournament.rounds.value.isEmpty()) {
                            SwissRoundItem(tournament.rounds.value.size + 1).apply {
                                pairingsProperty.value.setAll(randomPairing(tournament.teams.value))
                                null
                            }
                        } else {
                            SwissRoundItem(roundNumber = tournament.rounds.value.size + 1).apply {
                                pairingsProperty.value.setAll(pairing(
                                        records = calculateCurrentStandings(tournament.item),
                                        previousPairings = tournament.item.rounds.flatMap { it.pairingsProperty }))
                            }
                        })
                    }
                }

            }
            buttonbar {
                button("View Standings") {
                    action {
                        find<StandingsView>(mapOf(
                                "standings" to calculateCurrentStandings(tournament.item).sortedWith(standingSorter))
                        ).apply {
                            openModal(stageStyle = StageStyle.UTILITY, block = true)
                        }
                    }
                }
                button("Save") {
                    enableWhen(tournament.dirty.and(tournament.valid))
                    action {
                        tournament.commit()
                        if (league.tournaments.value.none { it.id == tournament.id.value }) {
                            league.tournaments.value.addAll(tournament.item)
                        }
                        tournament.item = null
                        close()
                    }
                }
                button("Cancel") {
                    action {
                        tournament.rollback()
                        tournament.item = null
                        close()
                    }
                }
            }
        }
    }

    private fun editRound() {
        find<SwissRoundView>(mapOf(
                "swissRoundModel" to swissRound,
                "league" to league.item,
                "tournament" to tournament.item
        )).apply {
            openModal(stageStyle = StageStyle.UTILITY, block = true)
        }
    }


    private fun editTeam() {
        find<SwissTeamView>(mapOf(
                "teamModel" to teamModel,
                "leagueModel" to league,
                "tournament" to tournament
        )).apply {
            openModal(stageStyle = StageStyle.UTILITY, block = true)
        }
    }
}


