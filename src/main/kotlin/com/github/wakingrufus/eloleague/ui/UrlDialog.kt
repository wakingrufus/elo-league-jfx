package com.github.wakingrufus.eloleague.ui

import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.net.URI
import java.net.URL


class UrlPickerItem(url: String) {
    val urlProperty = SimpleStringProperty(this, "", url)
    var url by urlProperty
}

class UrlPickerModel : ItemViewModel<UrlPickerItem>() {
    var url = bind(UrlPickerItem::urlProperty)
}

class UrlDialog : Fragment() {
    var url: URL? = null
    private val urlProperty =  UrlPickerModel()
    override val root = borderpane {
        center {
            form {
                fieldset {
                    field {
                        label("URL")
                        textfield(urlProperty.url).validator {
                            if (URI.create(it.toString()) != null) null else error("error")
                        }
                    }
                }
            }
        }
        bottom {
            buttonbar {
                button("OK") {
                    enableWhen(urlProperty.dirty)
                    action {
                        url = URI.create(urlProperty.url.value).toURL()
                        close()
                    }
                }
                button("Cancel") {
                    action {
                        url = null
                        close()
                    }
                }
            }
        }
    }
}