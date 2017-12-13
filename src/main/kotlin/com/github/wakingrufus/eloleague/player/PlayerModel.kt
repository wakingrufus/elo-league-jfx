package com.github.wakingrufus.eloleague.player

import tornadofx.ItemViewModel

class PlayerModel: ItemViewModel<PlayerItem>() {
    var id = bind {item?.idProperty}
    var name = bind{item?.nameProperty}
}
