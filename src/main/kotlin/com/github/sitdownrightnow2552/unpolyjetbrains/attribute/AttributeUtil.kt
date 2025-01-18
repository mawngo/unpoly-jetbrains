package com.github.sitdownrightnow2552.unpolyjetbrains.attribute

import com.intellij.psi.xml.XmlTag

object AttributeUtil {
    fun containsAttribute(context: XmlTag, attributeName: String?): Boolean {
        return context.attributes.find { it.name == attributeName } != null
    }
}
