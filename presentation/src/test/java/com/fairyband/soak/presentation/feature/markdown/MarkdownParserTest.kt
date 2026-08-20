package com.fairyband.soak.presentation.feature.markdown

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

private val parser = MarkdownParser()

private fun parse(raw: String) = parser.parse(raw)

private fun text(value: String) = MarkdownNode.PlainText(value)

class MarkdownParserTest : StringSpec({

    // 헤딩

    "# 개수만큼 헤딩 레벨이 정해진다" {
        (1..6).forEach { level ->
            val heading = parse("${"#".repeat(level)} 제목").first()

            heading shouldBe MarkdownNode.Heading(level, listOf(text("제목")))
        }
    }

    "# 뒤에 공백이 없으면 헤딩이 아니라 문단이다" {
        parse("#태그").first().shouldBeInstanceOf<MarkdownNode.Paragraph>()
    }

    "7단계 이상은 헤딩이 아니라 문단이다" {
        parse("####### 제목").first().shouldBeInstanceOf<MarkdownNode.Paragraph>()
    }

    // 문단

    "연속된 줄은 공백 하나로 이어 붙인다" {
        parse("첫 줄\n둘째 줄") shouldBe listOf(
            MarkdownNode.Paragraph(listOf(text("첫 줄 둘째 줄")))
        )
    }

    "빈 줄로 나뉜 문단은 별개 노드가 된다" {
        parse("첫 문단\n\n둘째 문단") shouldHaveSize 2
    }

    // 인라인

    "볼드 이탤릭 인라인코드를 파싱한다" {
        parse("**굵게**").first() shouldBe
            MarkdownNode.Paragraph(listOf(MarkdownNode.Bold(listOf(text("굵게")))))
        parse("*기울임*").first() shouldBe
            MarkdownNode.Paragraph(listOf(MarkdownNode.Italic(listOf(text("기울임")))))
        parse("***둘다***").first() shouldBe
            MarkdownNode.Paragraph(listOf(MarkdownNode.BoldItalic(listOf(text("둘다")))))
        parse("`code`").first() shouldBe
            MarkdownNode.Paragraph(listOf(MarkdownNode.Code("code")))
    }

    "앞뒤 텍스트와 강조가 섞여도 순서대로 파싱한다" {
        parse("앞 **굵게** 뒤").first() shouldBe MarkdownNode.Paragraph(
            listOf(
                text("앞 "),
                MarkdownNode.Bold(listOf(text("굵게"))),
                text(" 뒤"),
            )
        )
    }

    "닫히지 않은 마커는 일반 텍스트로 남는다" {
        parse("*닫히지 않음").first() shouldBe
            MarkdownNode.Paragraph(listOf(text("*닫히지 않음")))
    }

    // iOS 와 동일한 동작이에요. `**` 의 짝이 없으면 `*` 두 개가 짝으로 잡혀 빈 이탤릭이 생겨요.
    // 렌더러는 children 이 비어 있는 강조 노드를 그리지 않아야 해요.
    "짝이 없는 볼드 마커는 빈 이탤릭과 잔여 텍스트가 된다" {
        parse("**닫히지 않음").first() shouldBe MarkdownNode.Paragraph(
            listOf(
                MarkdownNode.Italic(emptyList()),
                text("닫히지 않음"),
            )
        )
    }

    // iOS 와 동일한 동작을 보존하기 위한 테스트예요. 표준 마크다운과는 다릅니다.
    "마커는 위치가 아니라 종류 우선순위로 처리되어 앞선 이탤릭이 남을 수 있다" {
        parse("a *i* and **b**").first() shouldBe MarkdownNode.Paragraph(
            listOf(
                text("a *i* and "),
                MarkdownNode.Bold(listOf(text("b"))),
            )
        )
    }

    // 코드 블록

    "코드 블록의 언어와 본문을 분리한다" {
        parse("```kotlin\nval a = 1\nval b = 2\n```").first() shouldBe
            MarkdownNode.CodeBlock(language = "kotlin", code = "val a = 1\nval b = 2")
    }

    "언어 표기가 없으면 language 가 null 이다" {
        parse("```\nplain\n```").first() shouldBe
            MarkdownNode.CodeBlock(language = null, code = "plain")
    }

    "코드 블록 안의 마크다운 문법은 해석하지 않는다" {
        parse("```\n# 제목이 아니다\n```").first() shouldBe
            MarkdownNode.CodeBlock(language = null, code = "# 제목이 아니다")
    }

    // 인용과 Callout

    "인용은 > 를 떼고 내부를 다시 파싱한다" {
        parse("> 인용문").first() shouldBe MarkdownNode.Blockquote(
            listOf(MarkdownNode.Paragraph(listOf(text("인용문"))))
        )
    }

    "Callout 타입 6종을 모두 인식한다" {
        CalloutType.entries.forEach { type ->
            val raw = "> [!${type.name.lowercase()}]\n> 본문"

            parse(raw).first() shouldBe MarkdownNode.Callout(
                type = type,
                children = listOf(MarkdownNode.Paragraph(listOf(text("본문")))),
            )
        }
    }

    "Callout 타입은 대소문자를 가리지 않는다" {
        val callout = parse("> [!WARNING]\n> 본문").first()

        callout.shouldBeInstanceOf<MarkdownNode.Callout>().type shouldBe CalloutType.WARNING
    }

    "알 수 없는 Callout 문법은 일반 인용으로 남는다" {
        parse("> [!unknown]\n> 본문").first().shouldBeInstanceOf<MarkdownNode.Blockquote>()
    }

    // 목록

    "불릿 목록은 - * + 를 모두 인식한다" {
        listOf("-", "*", "+").forEach { bullet ->
            parse("$bullet 첫째\n$bullet 둘째").first() shouldBe MarkdownNode.BulletList(
                listOf(
                    listOf(text("첫째")),
                    listOf(text("둘째")),
                )
            )
        }
    }

    "불릿 항목 안의 강조도 파싱한다" {
        parse("* **제목:** 설명").first() shouldBe MarkdownNode.BulletList(
            listOf(
                listOf(
                    MarkdownNode.Bold(listOf(text("제목:"))),
                    text(" 설명"),
                )
            )
        )
    }

    "번호 목록을 파싱한다" {
        parse("1. 첫째\n2. 둘째").first() shouldBe MarkdownNode.OrderedList(
            listOf(
                listOf(text("첫째")),
                listOf(text("둘째")),
            )
        )
    }

    // iOS 와 동일한 오탐이에요. 고치면 양 플랫폼 렌더링이 어긋납니다.
    "첫 마침표 앞이 숫자면 번호 목록으로 인식한다" {
        parse("2026. 8. 20. 발표").first().shouldBeInstanceOf<MarkdownNode.OrderedList>()
    }

    "숫자로 시작하지 않으면 번호 목록이 아니다" {
        parse("가. 첫째").first().shouldBeInstanceOf<MarkdownNode.Paragraph>()
    }

    // 표와 수평선

    "표의 헤더와 행을 분리하고 정렬 구분선은 버린다" {
        val raw = """
            | 모듈 수 | 추천 도구 |
            | --- | --- |
            | 1-3개 | Xcode |
            | 5-50개 | Tuist |
        """.trimIndent()

        parse(raw).first() shouldBe MarkdownNode.Table(
            header = listOf("모듈 수", "추천 도구"),
            rows = listOf(
                listOf("1-3개", "Xcode"),
                listOf("5-50개", "Tuist"),
            ),
        )
    }

    "수평선 표기 3종을 모두 인식한다" {
        listOf("---", "***", "___").forEach { rule ->
            parse(rule).first() shouldBe MarkdownNode.HorizontalRule
        }
    }

    // 실제 응답

    "실제 API 응답을 블록 순서대로 파싱한다" {
        val nodes = parse(REAL_RESPONSE_SAMPLE)

        nodes shouldHaveSize 5
        nodes[0] shouldBe MarkdownNode.Heading(2, listOf(text("메타(Meta)의 초지능 AI 연구소 동향")))
        nodes[1].shouldBeInstanceOf<MarkdownNode.Paragraph>()
        nodes[2].shouldBeInstanceOf<MarkdownNode.BulletList>()
        nodes[3] shouldBe MarkdownNode.Heading(3, listOf(text("핵심 인재 영입 및 대규모 투자")))
        nodes[4].shouldBeInstanceOf<MarkdownNode.BulletList>()
    }

    "이모지가 섞인 헤딩도 파싱한다" {
        parse("## 📌 요약").first() shouldBe
            MarkdownNode.Heading(2, listOf(text("📌 요약")))
    }

    "빈 문자열은 빈 목록을 반환한다" {
        parse("") shouldBe emptyList()
    }
})

/** `GET /api/newsletters/exposure-contents/419/markdown` 응답에서 발췌했어요. */
private val REAL_RESPONSE_SAMPLE = """
    ## 메타(Meta)의 초지능 AI 연구소 동향

    메타의 초지능 연구소 내 논의는 아직 초기 단계이며, 결정된 바는 없습니다.

    * **오픈소스 정책에 대한 입장:** 메타 대변인은 논평을 거부했습니다.
    * **최근의 위기와 새로운 목표:** 내부 경영 갈등과 직원 이탈을 겪었습니다.

    ### 핵심 인재 영입 및 대규모 투자

    * **파격적인 인재 영입:** 최대 1억 달러 규모의 급여 패키지를 제시했습니다.
""".trimIndent()
