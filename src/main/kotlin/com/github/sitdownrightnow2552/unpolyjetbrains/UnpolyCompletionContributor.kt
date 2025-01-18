package com.github.sitdownrightnow2552.unpolyjetbrains

import com.github.sitdownrightnow2552.unpolyjetbrains.attribute.UnpolyAttributes
import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.xml.XmlTokenType
import com.intellij.util.ProcessingContext


class UnpolyCompletionContributor : CompletionContributor() {
    init {

        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(XmlTokenType.XML_NAME).withParent(XmlPatterns.xmlAttribute()),
            UnpolyAttributeCompletionProvider()
        )

        UnpolyAttributes.allAttributesByNames.forEach { (name, attributes) ->
            val completions = attributes.flatMap { it.completions }.toList()
            completions.forEach {
                extend(
                    CompletionType.BASIC,
                    PlatformPatterns
                        .psiElement(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN)
                        .inside(XmlPatterns.xmlAttribute(name)),
                    it
                )
            }
        }
    }
}
