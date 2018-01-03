package com.github.wakingrufus.eloleague.swiss

import tornadofx.*

class SwissTeamModel : ItemViewModel<SwissTeamItem>() {
    var id = bind { item?.idProperty }
    var name = bind { item?.nameProperty }
    var players = bind { item?.playersProperty }
}