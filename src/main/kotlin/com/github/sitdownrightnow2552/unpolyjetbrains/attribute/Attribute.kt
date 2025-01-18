package com.github.sitdownrightnow2552.unpolyjetbrains.attribute

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.xml.XmlTag

typealias Matcher = (XmlTag) -> Boolean
typealias Completion = CompletionProvider<CompletionParameters?>

/**
 * Contain information about an Unpoly attribute.
 */
data class Attribute(
    /**
     * Name of the attribute.
     */
    val name: String,
    /**
     * Short description for the attribute.
     */
    val text: String,
    /**
     * Values that this attribute accepts.
     * If this field is not empty, then this attribute is enumerated.
     */
    val values: Set<String> = emptySet(),
    /**
     * Whether this attribute is deprecated.
     */
    val deprecated: Boolean = false,
    /**
     * Whether this attribute is experimental.
     */
    val experimental: Boolean = false,
    /**
     * Default value of this attribute.
     */
    val defaultValue: String = "",
    /**
     * This attribute modifiers.
     */
    val modifiers: List<Attribute> = emptyList(),

    /**
     * List of matchers for checking whether this attribute is supported on the target XmlTag.
     */
    val completions: List<Completion> = mutableListOf(),

    val isEnumerated: Boolean = false,
) {
    /**
     * List of matchers for checking whether this attribute is supported on the target XmlTag.
     */
    private val matchers: MutableList<Matcher> = mutableListOf()

    val icon
        get() = ICON

    fun match(tag: XmlTag): Boolean {
        if (matchers.isEmpty()) {
            return true
        }
        for (matcher in matchers) {
            if (matcher.invoke(tag)) {
                return true
            }
        }
        return false
    }

    companion object {
        // Value placeholder wrapped in "<" and ">".
        // TODO: add suggestion for those group.
        const val VALUE_URI = "<uri>"
        const val VALUE_SELECTOR = "<selector>"
        const val VALUE_NUMBER = "<number>"
        const val VALUE_JSON = "<json>"
        const val VALUE_HTML = "<html>"
        const val VALUE_JS = "<js>"

        // Value group separated by ",".
        const val VALUE_BOOLEAN = "true,false"
        const val VALUE_HTTP_METHOD = "get,post,put,delete"
        const val VALUE_TRANSITION =
            "fade-in,fade-out,move-to-top,move-from-top,move-to-bottom,move-from-bottom,move-to-left,move-from-left,move-to-right,move-from-right,none"
        const val VALUE_WATCH_EVENT = "change,input,<event>"
        const val VALUE_POSITION = "top,bottom,left,right"
        const val VALUE_ALIGN = "top,bottom,left,right,center"
        const val VALUE_LAYER_MATCHING = "current,parent,closest,overlay,ancestor,child,descendant,subtree"

        private val ICON = IconLoader.getIcon("/pluginIcon.svg", Attribute::class.java)

        /**
         * Create an Unpoly attribute using notation syntax.
         *
         * @param notation contains information of tag, name and default value, copied from the document, for example, a[[up-layer='new']] (a[up-layer='new'], the former one is for escaping kdoc).
         * @param text the description of attribute.
         * @param values available value of this enumerated attribute.
         * @param deprecated whether this attribute is deprecated (or experimental).
         * @param modifiers a set of this attribute modifiers.
         */
        @JvmStatic
        fun of(
            notation: String,
            text: String = "",
            values: Collection<String> = emptyList(),
            deprecated: Boolean = false,
            experimental: Boolean = false,
            enumerated: Boolean = false,
            modifiers: Collection<Attribute> = emptyList(),
            matchers: Collection<Matcher> = emptyList(),
            completions: Collection<Completion> = emptyList(),
        ): Attribute {
            val (tag, name, value) = parseNotation(notation)

            var supportedValues = values
            if (value.isNotEmpty()) {
                if (values.isEmpty()) {
                    // try to set the value type if a value was specified in notation but values-set is not specified manually.
                    for (group in listOf(VALUE_HTTP_METHOD, VALUE_BOOLEAN)) {
                        if (group.split(",").contains(value)) {
                            supportedValues = setOf(group)
                        }
                    }
                }

                supportedValues += setOf(value)
            }

            supportedValues = supportedValues
                .flatMap { it.split(",") }
                .filter { it.isNotBlank() }
                .toSet()

            val attribute = Attribute(
                name = name,
                text = text,
                values = supportedValues,
                deprecated = deprecated,
                experimental = experimental,
                defaultValue = value,
                modifiers = modifiers.reversed().distinctBy { it.name }.reversed().toList(),
                completions = completions.toList(),
                isEnumerated = enumerated,
            )

            attribute.matchers.addAll(matchers)
            if (tag.isNotEmpty()) {
                attribute.matchers.add(Matchers.tag(tag))
            }

            attribute.modifiers.forEach { it.matchers.add(Matchers.attribute(name)) }
            return attribute
        }

        @JvmStatic
        fun parseNotation(notation: String): Triple<String, String, String> {
            val match = Regex("(.+)?\\[(.+?)(='(.+?)')?]").find(notation)
                ?: throw IllegalArgumentException("No match found for notation $notation")
            val (_, tag, name, _, value) = match.groupValues
            return Triple(tag, name, value)
        }
    }
}
