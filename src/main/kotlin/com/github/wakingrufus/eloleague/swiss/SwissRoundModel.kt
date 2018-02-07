package com.github.wakingrufus.eloleague.swiss

import tornadofx.*

class SwissRoundModel : ItemViewModel<SwissRoundItem>() {
    var roundNumber = bind { item?.roundNumberProperty }
    var pairings = bind { item?.pairingsProperty }
}