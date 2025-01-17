package com.github.sitdownrightnow2552.unpolyjetbrains.attribute

import com.intellij.psi.impl.source.html.dtd.HtmlElementDescriptorImpl
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.XmlAttributeDescriptorsProvider

class UnpolyAttributeDescriptorsProvider : XmlAttributeDescriptorsProvider {
    override fun getAttributeDescriptors(context: XmlTag): Array<XmlAttributeDescriptor> {
        if (context.descriptor !is HtmlElementDescriptorImpl) return emptyArray()

        val attributes = UnpolyAttributes.attributesByTags.getOrDefault("", emptySet()) +
            UnpolyAttributes.attributesByTags.getOrDefault(context.name, emptySet())

        return attributes
            .flatMap { if (containsAttribute(context, it.name)) it.modifiers else setOf(it) }
            .map { UnpolyAttributeDescriptor(it, context) }
            .toTypedArray()
    }

    override fun getAttributeDescriptor(attributeName: String, context: XmlTag): XmlAttributeDescriptor? {
        if (context.descriptor !is HtmlElementDescriptorImpl) return null

        val attributes = UnpolyAttributes.allAttributesByNames[attributeName] ?: return null
        for (attribute in attributes) {
            if (attribute.tag != "" && attribute.tag != context.name) {
                continue
            }
            if (attribute.dependOn != null && !containsAttribute(context, attribute.dependOn!!.name)) {
                continue
            }
            return UnpolyAttributeDescriptor(attribute, context)
        }
        return null
    }

    private fun containsAttribute(context: XmlTag, attributeName: String?): Boolean {
        return context.attributes.find { it.name == attributeName } != null
    }
}
