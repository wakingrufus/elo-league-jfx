package com.github.wakingrufus.eloleague

import org.jsoup.helper.StringUtil

 fun isValidInt(value: String?): Boolean {
    return (value != null && !value.isNullOrBlank() && StringUtil.isNumeric(value.replace(",", "")))
}