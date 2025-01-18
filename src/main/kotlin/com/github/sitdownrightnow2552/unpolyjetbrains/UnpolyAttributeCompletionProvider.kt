package com.github.sitdownrightnow2552.unpolyjetbrains

import com.github.sitdownrightnow2552.unpolyjetbrains.attribute.AttributeUtil.containsAttribute
import com.github.sitdownrightnow2552.unpolyjetbrains.attribute.UnpolyAttributes
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionUtilCore
import com.intellij.codeInsight.completion.XmlAttributeInsertHandler
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext

class UnpolyAttributeCompletionProvider : CompletionProvider<CompletionParameters?>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val position = parameters.position

        if (HTMLLanguage.INSTANCE !in position.containingFile.viewProvider.languages) {
            return
        }

        val rawAttribute = position.parent as? XmlAttribute ?: return
        val xmlTag = rawAttribute.parent ?: return

        val attribute = StringUtil.trimEnd(rawAttribute.name, CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED)

        if (attribute.isEmpty()) {
            return
        }

        UnpolyAttributes.attributes.filter { it.match(xmlTag) }
            .flatMap { if (containsAttribute(xmlTag, it.name)) it.modifiers else setOf(it) }
            .forEach {
                var element = LookupElementBuilder
                    .create(it.name)
                    .withIcon(it.icon)
                    .withCaseSensitivity(false)
                    .withTypeText(attribute)
                    .withInsertHandler(XmlAttributeInsertHandler.INSTANCE)

                if (it.deprecated) {
                    element = element
                        .withTailText(" (deprecated)")
                        .withStrikeoutness(true)
                }

                if (it.experimental) {
                    element = element
                        .withTailText(" (experimental)")
                }

                result.addElement(element)
            }
    }
}
