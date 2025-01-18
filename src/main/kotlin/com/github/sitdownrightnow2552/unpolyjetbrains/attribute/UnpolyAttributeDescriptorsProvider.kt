package com.github.sitdownrightnow2552.unpolyjetbrains.attribute

import com.intellij.psi.impl.source.html.dtd.HtmlElementDescriptorImpl
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.XmlAttributeDescriptor
import com.intellij.xml.XmlAttributeDescriptorsProvider

class UnpolyAttributeDescriptorsProvider : XmlAttributeDescriptorsProvider {
    override fun getAttributeDescriptors(context: XmlTag): Array<XmlAttributeDescriptor> {
        return emptyArray()
    }

    override fun getAttributeDescriptor(attributeName: String, context: XmlTag): XmlAttributeDescriptor? {
        if (context.descriptor !is HtmlElementDescriptorImpl) return null

        val attributes = UnpolyAttributes.allAttributesByNames[attributeName] ?: return null
        for (attribute in attributes) {
            if (!attribute.match(context)) {
                continue
            }
            return UnpolyAttributeDescriptor(attribute, context)
        }
        return null
    }
}
