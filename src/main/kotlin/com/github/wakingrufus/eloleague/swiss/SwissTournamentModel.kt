package com.github.wakingrufus.eloleague.swiss

import tornadofx.*

class SwissTournamentModel : ItemViewModel<SwissTournamentItem>() {
    var id = bind { item?.idProperty }
    var name = bind { item?.nameProperty }
    var startTime = bind { item?.startTimeProperty }
    var teams = bind { item?.teamsProperty }
    var rounds = bind { item?.roundsProperty }

}
