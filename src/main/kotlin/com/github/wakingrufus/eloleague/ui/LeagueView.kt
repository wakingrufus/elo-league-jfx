package com.github.wakingrufus.eloleague.ui

import tornadofx.*

class LeagueView : View("League View") {

    val model: LeagueModel by inject()
 //   val save: (LeagueModel) -> Unit by param()

    override val root = vbox {
        form {
            fieldset("Edit League") {
                field("Name") {
                    textfield(model.name)
                }
                button("Save") {
                    enableWhen(model.dirty)
                    action {
                        model.commit()
                     //   save(model)
                    }
                }
            }
        }
    }

}
