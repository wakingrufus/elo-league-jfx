package com.github.wakingrufus.eloleague.ui

import javafx.beans.value.ObservableValue
import javafx.event.EventTarget
import tornadofx.bind
import tornadofx.control.DateTimePicker
import tornadofx.opcr
import tornadofx.toProperty
import java.time.LocalDateTime

fun EventTarget.datetimepicker(value: LocalDateTime? = null, op: (DateTimePicker.() -> Unit)? = null) = opcr(this, DateTimePicker().apply { if (value != null) dateTimeValue = value }, op)
fun EventTarget.datetimepicker(property: ObservableValue<LocalDateTime>, op: (DateTimePicker.() -> Unit)? = null) = datetimepicker().apply {
    bind(property.value?.toLocalDate().toProperty())
    op?.invoke(this)
}