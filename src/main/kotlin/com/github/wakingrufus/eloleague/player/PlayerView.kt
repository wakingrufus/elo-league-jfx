package com.github.wakingrufus.eloleague.player

import tornadofx.*

class PlayerView : View("Player View") {

    val playerModel: PlayerModel by inject()

    override val root = vbox {
        visibleWhen {
            playerModel.empty.not()
        }
        form {
            fieldset("Edit Player") {
                field("Name") {
                    textfield(playerModel.name)

                    button("Save") {
                        enableWhen(playerModel.dirty.and(playerModel.valid))
                        action {
                            playerModel.commit()
                            playerModel.item = null
                        }
                    }
                }
            }
        }

    }
}