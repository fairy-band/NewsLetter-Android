package com.fairyband.soak.presentation.feature.markdown

/**
 * 마크다운 문서를 구성하는 노드예요.
 * iOS(`MarkdownNode.swift`)와 동일한 구조를 유지해 양 플랫폼의 렌더링 결과를 맞춰요.
 */
sealed interface MarkdownNode {

    // 블록 요소
    data class Heading(val level: Int, val children: List<MarkdownNode>) : MarkdownNode
    data class Paragraph(val children: List<MarkdownNode>) : MarkdownNode
    data class CodeBlock(val language: String?, val code: String) : MarkdownNode
    data class Blockquote(val children: List<MarkdownNode>) : MarkdownNode
    data class Callout(val type: CalloutType, val children: List<MarkdownNode>) : MarkdownNode
    data class BulletList(val items: List<List<MarkdownNode>>) : MarkdownNode
    data class OrderedList(val items: List<List<MarkdownNode>>) : MarkdownNode
    data object HorizontalRule : MarkdownNode
    data class Table(val header: List<String>, val rows: List<List<String>>) : MarkdownNode

    // 인라인 요소
    data class PlainText(val text: String) : MarkdownNode
    data class Bold(val children: List<MarkdownNode>) : MarkdownNode
    data class Italic(val children: List<MarkdownNode>) : MarkdownNode
    data class BoldItalic(val children: List<MarkdownNode>) : MarkdownNode
    data class Code(val code: String) : MarkdownNode
}

enum class CalloutType {
    TIP, INFO, WARNING, DANGER, NOTE, IMPORTANT;

    /** Callout 헤더에 노출하는 레이블이에요. (예: `TIP` → `Tip`) */
    val label: String = name.lowercase().replaceFirstChar { it.uppercase() }

    companion object {
        /** 알 수 없는 타입은 [NOTE] 로 처리해요. */
        fun from(value: String): CalloutType =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: NOTE
    }
}
