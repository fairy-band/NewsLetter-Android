package com.fairyband.soak.presentation.feature.markdown

/**
 * 서버가 내려주는 마크다운 본문을 [MarkdownNode] 트리로 변환해요.
 *
 * iOS(`MarkdownParser.swift`)를 이식한 구현이라, 표준 마크다운이 아니라
 * **iOS와 동일한 결과**를 내는 것이 목표예요. 판정 규칙을 바꾸면 두 플랫폼의
 * 렌더링이 어긋나므로 주의해 주세요.
 */
class MarkdownParser {

    fun parse(raw: String): List<MarkdownNode> {
        return parseBlocks(preprocess(raw).split("\n"))
    }

    /** Callout 문법을 내부 마커로 바꿔요. `> [!tip]` → `%%CALLOUT:tip%%` */
    private fun preprocess(text: String): String {
        return CALLOUT_PATTERN.replace(text) { match ->
            "$CALLOUT_MARKER${match.groupValues[1].lowercase()}%%\n"
        }
    }

    private fun parseBlocks(lines: List<String>): List<MarkdownNode> {
        val nodes = mutableListOf<MarkdownNode>()
        var index = 0

        while (index < lines.size) {
            val line = lines[index]

            if (line.isBlank()) {
                index++
                continue
            }

            // 코드 블록
            if (line.startsWith(CODE_FENCE)) {
                val language = line.drop(CODE_FENCE.length).trim()
                val codeLines = mutableListOf<String>()
                index++
                while (index < lines.size && !lines[index].startsWith(CODE_FENCE)) {
                    codeLines += lines[index]
                    index++
                }
                nodes += MarkdownNode.CodeBlock(
                    language = language.ifEmpty { null },
                    code = codeLines.joinToString("\n"),
                )
                index++
                continue
            }

            // 헤딩
            val heading = parseHeadingLine(line)
            if (heading != null) {
                val (level, content) = heading
                nodes += MarkdownNode.Heading(level, parseInline(content))
                index++
                continue
            }

            val trimmed = line.trim()

            // 수평선
            if (trimmed in HORIZONTAL_RULES) {
                nodes += MarkdownNode.HorizontalRule
                index++
                continue
            }

            // 표
            if (trimmed.startsWith("|") && trimmed.endsWith("|")) {
                val tableLines = mutableListOf(line)
                index++
                while (index < lines.size && lines[index].trim().startsWith("|")) {
                    tableLines += lines[index]
                    index++
                }
                parseTable(tableLines)?.let { nodes += it }
                continue
            }

            // Callout (전처리로 삽입한 마커)
            if (trimmed.startsWith(CALLOUT_MARKER)) {
                val type = CalloutType.from(
                    trimmed.replace(CALLOUT_MARKER, "").replace("%%", "")
                )
                val bodyLines = mutableListOf<String>()
                index++
                while (index < lines.size && lines[index].startsWith(">")) {
                    bodyLines += lines[index].drop(1).trim()
                    index++
                }
                nodes += MarkdownNode.Callout(type, parseBlocks(bodyLines))
                continue
            }

            // 인용
            if (trimmed.startsWith(">")) {
                val quoteLines = mutableListOf<String>()
                while (index < lines.size && lines[index].trim().startsWith(">")) {
                    quoteLines += lines[index].drop(1).trim()
                    index++
                }
                nodes += MarkdownNode.Blockquote(parseBlocks(quoteLines))
                continue
            }

            // 불릿 목록
            if (isBulletItem(trimmed)) {
                val items = mutableListOf<List<MarkdownNode>>()
                while (index < lines.size) {
                    val item = lines[index].trim()
                    if (!isBulletItem(item)) break
                    items += parseInline(item.drop(2))
                    index++
                }
                nodes += MarkdownNode.BulletList(items)
                continue
            }

            // 번호 목록
            if (parseOrderedItem(trimmed) != null) {
                val items = mutableListOf<List<MarkdownNode>>()
                while (index < lines.size) {
                    val item = parseOrderedItem(lines[index].trim()) ?: break
                    items += parseInline(item)
                    index++
                }
                nodes += MarkdownNode.OrderedList(items)
                continue
            }

            // 문단: 빈 줄이나 다른 블록을 만날 때까지 이어 붙여요.
            val paragraphLines = mutableListOf<String>()
            while (index < lines.size) {
                val candidate = lines[index].trim()
                if (candidate.isEmpty()) break
                if (candidate.startsWith("#")) break
                if (candidate.startsWith(CODE_FENCE)) break
                if (candidate.startsWith("|")) break
                if (candidate == "---" || candidate == "***") break
                paragraphLines += lines[index]
                index++
            }

            if (paragraphLines.isNotEmpty()) {
                nodes += MarkdownNode.Paragraph(parseInline(paragraphLines.joinToString(" ")))
                continue
            }

            // 어떤 블록으로도 소비되지 않은 줄이에요. (예: `#태그`, 닫히지 않은 `|`)
            // 그대로 두면 index 가 멈춰 무한 루프가 되므로 문단으로 흘려보내요.
            nodes += MarkdownNode.Paragraph(parseInline(line))
            index++
        }

        return nodes
    }

    /**
     * 인라인 요소를 파싱해요.
     *
     * iOS와 동일하게 **가장 앞선 위치가 아니라 [INLINE_MARKERS] 순서대로** 첫 번째로
     * 짝이 맞는 마커를 처리해요. 그래서 `a *i* and **b**` 처럼 뒤쪽 볼드가 먼저 잡히면
     * 앞의 이탤릭은 일반 텍스트로 남아요.
     */
    private fun parseInline(text: String): List<MarkdownNode> {
        val nodes = mutableListOf<MarkdownNode>()
        var remaining = text

        while (remaining.isNotEmpty()) {
            val marker = INLINE_MARKERS.firstOrNull { remaining.hasPair(it) }

            if (marker == null) {
                nodes += MarkdownNode.PlainText(remaining)
                break
            }

            val start = remaining.indexOf(marker)
            val contentStart = start + marker.length
            val end = remaining.indexOf(marker, contentStart)

            val before = remaining.substring(0, start)
            if (before.isNotEmpty()) {
                nodes += MarkdownNode.PlainText(before)
            }
            nodes += wrapInline(marker, remaining.substring(contentStart, end))
            remaining = remaining.substring(end + marker.length)
        }

        return nodes
    }

    private fun String.hasPair(marker: String): Boolean {
        val start = indexOf(marker)
        return start >= 0 && indexOf(marker, start + marker.length) >= 0
    }

    private fun wrapInline(marker: String, content: String): MarkdownNode = when (marker) {
        BOLD_ITALIC -> MarkdownNode.BoldItalic(parseInline(content))
        BOLD -> MarkdownNode.Bold(parseInline(content))
        ITALIC -> MarkdownNode.Italic(parseInline(content))
        else -> MarkdownNode.Code(content)
    }

    private fun parseHeadingLine(line: String): Pair<Int, String>? {
        val level = line.takeWhile { it == '#' }.length
        if (level !in 1..MAX_HEADING_LEVEL) return null

        val rest = line.drop(level)
        if (!rest.startsWith(" ")) return null

        return level to rest.drop(1)
    }

    private fun isBulletItem(line: String): Boolean =
        BULLET_PREFIXES.any { line.startsWith(it) }

    /**
     * `1. 내용` 형태면 `내용`을 돌려줘요.
     *
     * iOS와 동일하게 **첫 마침표 앞 전체**를 숫자로 판정하므로,
     * `2026. 8. 20. 발표` 같은 줄도 번호 목록으로 인식돼요.
     */
    private fun parseOrderedItem(line: String): String? {
        val dotSpaceIndex = line.indexOf(". ")
        if (dotSpaceIndex < 0) return null
        if (line.take(line.indexOf('.')).toIntOrNull() == null) return null

        return line.substring(dotSpaceIndex + 2)
    }

    private fun parseTable(lines: List<String>): MarkdownNode.Table? {
        if (lines.size < 2) return null

        val header = lines[0].toCells()
        if (header.isEmpty()) return null

        // 두 번째 줄은 정렬 구분선이라 건너뛰어요.
        val rows = lines.drop(2).map { it.toCells() }.filter { it.isNotEmpty() }
        return MarkdownNode.Table(header = header, rows = rows)
    }

    private fun String.toCells(): List<String> =
        split("|")
            .map { it.trim() }
            .filter { cell ->
                cell.isNotEmpty() && !cell.all { it == '-' || it == ':' }
            }

    private companion object {
        const val CALLOUT_MARKER = "%%CALLOUT:"
        const val CODE_FENCE = "```"
        const val MAX_HEADING_LEVEL = 6

        const val BOLD_ITALIC = "***"
        const val BOLD = "**"
        const val ITALIC = "*"
        const val INLINE_CODE = "`"

        val CALLOUT_PATTERN = Regex(
            """^>\s*\[!(tip|info|warning|danger|note|important)]\s*\n?""",
            setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE),
        )
        val HORIZONTAL_RULES = setOf("---", "***", "___")
        val BULLET_PREFIXES = listOf("- ", "* ", "+ ")
        val INLINE_MARKERS = listOf(BOLD_ITALIC, BOLD, ITALIC, INLINE_CODE)
    }
}
