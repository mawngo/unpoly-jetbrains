package com.github.sitdownrightnow2552.unpolyjetbrains.attribute

import com.intellij.psi.xml.XmlTag


object Matchers {
    fun tag(name: String): Matcher {
        return { tag: XmlTag -> tag.name == name }
    }

    fun attribute(name: String): Matcher {
        return { tag: XmlTag -> tag.attributes.find { it.name == name } != null }
    }

    fun links(): Matcher {
        return { tag: XmlTag ->
            tag.name == "a" || tag.attributes.find { it.name == "up-href" } != null
        }
    }
}
