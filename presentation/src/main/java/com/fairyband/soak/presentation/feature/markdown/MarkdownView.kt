package com.fairyband.soak.presentation.feature.markdown

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.AnnotatedString.Builder
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fairyband.soak.core.theme.SoakTheme
import com.fairyband.soak.presentation.R

/**
 * 마크다운 본문을 렌더링해요.
 *
 * iOS(`MarkdownView.swift`)를 이식한 구현이라 레이아웃 수치(간격, radius, padding)는
 * iOS와 맞춰 두었어요. 색은 다크 배경 전용이라 [MarkdownColors] 를 써요.
 *
 * @param pointColor 카드별 포인트 컬러예요. 헤딩(H1~H3) · 볼드 · 리스트 마커에 반영돼요.
 */
@Composable
fun MarkdownView(
    markdown: String,
    pointColor: Color,
    modifier: Modifier = Modifier,
) {
    val nodes = remember(markdown) { MarkdownParser().parse(markdown) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MarkdownViewDefaults.BLOCK_SPACING),
    ) {
        nodes.forEach { node ->
            BlockNode(
                node = node,
                pointColor = pointColor,
                textStyle = SoakTheme.typography.body15,
            )
        }
    }
}

/** 블록 노드를 종류에 맞는 컴포저블로 분기해요. */
@Composable
private fun BlockNode(
    node: MarkdownNode,
    pointColor: Color,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    when (node) {
        is MarkdownNode.Heading -> HeadingBlock(
            level = node.level,
            children = node.children,
            pointColor = pointColor,
            modifier = modifier,
        )

        is MarkdownNode.Paragraph -> Text(
            modifier = modifier.fillMaxWidth(),
            text = node.children.toAnnotatedString(pointColor),
            style = textStyle,
            color = MarkdownColors.text,
        )

        is MarkdownNode.CodeBlock -> CodeBlock(
            language = node.language,
            code = node.code,
            modifier = modifier,
        )

        is MarkdownNode.Blockquote -> BlockquoteBlock(
            children = node.children,
            pointColor = pointColor,
            modifier = modifier,
        )

        is MarkdownNode.Callout -> CalloutBlock(
            type = node.type,
            children = node.children,
            pointColor = pointColor,
            modifier = modifier,
        )

        is MarkdownNode.BulletList -> BulletListBlock(
            items = node.items,
            pointColor = pointColor,
            textStyle = textStyle,
            modifier = modifier,
        )

        is MarkdownNode.OrderedList -> OrderedListBlock(
            items = node.items,
            pointColor = pointColor,
            textStyle = textStyle,
            modifier = modifier,
        )

        MarkdownNode.HorizontalRule -> HorizontalDivider(
            modifier = modifier.padding(vertical = 4.dp),
            color = MarkdownColors.divider,
        )

        is MarkdownNode.Table -> TableBlock(
            header = node.header,
            rows = node.rows,
            modifier = modifier,
        )

        // 인라인 노드는 블록 위치에 올 수 없어요.
        else -> Unit
    }
}

@Composable
private fun HeadingBlock(
    level: Int,
    children: List<MarkdownNode>,
    pointColor: Color,
    modifier: Modifier = Modifier,
) {
    // 중간 제목(###)은 앞 문단과 구분되도록 위쪽 여백을 더 줘요.
    val topPadding = when (level) {
        1 -> 4.dp
        3 -> 20.dp
        else -> 0.dp
    }
    val style = when (level) {
        1 -> SoakTheme.typography.head22.copy(fontWeight = FontWeight.Bold)
        2 -> SoakTheme.typography.head20.copy(fontWeight = FontWeight.SemiBold)
        3 -> SoakTheme.typography.body16.copy(fontWeight = FontWeight.SemiBold)
        else -> SoakTheme.typography.body15.copy(fontWeight = FontWeight.Medium)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = topPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = children.toAnnotatedString(pointColor),
            style = style,
            color = if (level <= 3) pointColor else MarkdownColors.textStrong,
        )

        if (level <= 2) {
            HorizontalDivider(color = MarkdownColors.divider)
        }
    }
}

@Composable
private fun CodeBlock(
    language: String?,
    code: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MarkdownColors.codeBackground)
            .border(
                width = 1.dp,
                color = MarkdownColors.codeBorder,
                shape = RoundedCornerShape(12.dp),
            ),
    ) {
        if (!language.isNullOrEmpty()) {
            Text(
                modifier = Modifier
                    .padding(start = 14.dp, top = 12.dp, bottom = 10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MarkdownColors.codeLanguageBackground)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                text = language,
                style = SoakTheme.typography.caption11.copy(fontWeight = FontWeight.SemiBold),
                color = MarkdownColors.textTertiary,
            )

            HorizontalDivider(color = MarkdownColors.codeBorder)
        }

        Text(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(14.dp),
            text = code,
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = MarkdownViewDefaults.CODE_FONT_SIZE,
            ),
            color = MarkdownColors.codeText,
        )
    }
}

@Composable
private fun BlockquoteBlock(
    children: List<MarkdownNode>,
    pointColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(8.dp))
            .background(MarkdownColors.quoteBackground)
            .border(
                width = 1.dp,
                color = MarkdownColors.quoteBorder,
                shape = RoundedCornerShape(8.dp),
            ),
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .background(MarkdownColors.quoteBar),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            children.forEach { child ->
                BlockNode(
                    node = child,
                    pointColor = pointColor,
                    textStyle = SoakTheme.typography.body13,
                )
            }
        }
    }
}

@Composable
private fun CalloutBlock(
    type: CalloutType,
    children: List<MarkdownNode>,
    pointColor: Color,
    modifier: Modifier = Modifier,
) {
    val accentColor = MarkdownColors.accentOf(type)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(accentColor.copy(alpha = 0.10f))
            .border(
                width = 1.dp,
                color = accentColor.copy(alpha = 0.20f),
                shape = RoundedCornerShape(12.dp),
            )
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(accentColor.copy(alpha = 0.12f))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = ImageVector.vectorResource(calloutIconRes(type)),
                contentDescription = null,
                tint = accentColor,
            )

            Text(
                text = type.label,
                style = SoakTheme.typography.caption12.copy(fontWeight = FontWeight.SemiBold),
                color = accentColor,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            children.forEach { child ->
                BlockNode(
                    node = child,
                    pointColor = pointColor,
                    textStyle = SoakTheme.typography.body13,
                )
            }
        }
    }
}

@Composable
private fun BulletListBlock(
    items: List<List<MarkdownNode>>,
    pointColor: Color,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items.forEach { item ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(6.dp)
                        .background(color = pointColor, shape = CircleShape),
                )

                Text(
                    modifier = Modifier.weight(1f),
                    // 리스트 마커가 포인트 컬러라, 볼드는 흰색으로 둬 대비를 줘요.
                    text = item.toAnnotatedString(MarkdownColors.textStrong),
                    style = textStyle,
                    color = MarkdownColors.text,
                )
            }
        }
    }
}

@Composable
private fun OrderedListBlock(
    items: List<List<MarkdownNode>>,
    pointColor: Color,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items.forEachIndexed { index, item ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .padding(top = 1.dp)
                        .background(color = pointColor.copy(alpha = 0.12f), shape = CircleShape)
                        .sizeIn(minWidth = 18.dp, minHeight = 18.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = "${index + 1}",
                        style = SoakTheme.typography.caption12.copy(fontWeight = FontWeight.SemiBold),
                        color = pointColor,
                    )
                }

                Text(
                    modifier = Modifier.weight(1f),
                    text = item.toAnnotatedString(MarkdownColors.textStrong),
                    style = textStyle,
                    color = MarkdownColors.text,
                )
            }
        }
    }
}

@Composable
private fun TableBlock(
    header: List<String>,
    rows: List<List<String>>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MarkdownColors.divider,
                shape = RoundedCornerShape(12.dp),
            ),
    ) {
        TableRow(
            cells = header,
            style = SoakTheme.typography.body13.copy(fontWeight = FontWeight.SemiBold),
            color = MarkdownColors.textTertiary,
            background = MarkdownColors.tableHeaderBackground,
        )

        HorizontalDivider(color = MarkdownColors.divider)

        rows.forEachIndexed { index, row ->
            TableRow(
                cells = row,
                style = SoakTheme.typography.body13,
                color = MarkdownColors.text,
                background = if (index % 2 == 1) {
                    MarkdownColors.tableStripeBackground
                } else {
                    Color.Transparent
                },
            )

            if (index < rows.lastIndex) {
                HorizontalDivider(color = MarkdownColors.divider)
            }
        }
    }
}

@Composable
private fun TableRow(
    cells: List<String>,
    style: TextStyle,
    color: Color,
    background: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .height(IntrinsicSize.Min),
    ) {
        cells.forEachIndexed { index, cell ->
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                text = cell,
                style = style,
                color = color,
            )

            if (index < cells.lastIndex) {
                VerticalDivider(color = MarkdownColors.divider)
            }
        }
    }
}

/**
 * 인라인 노드를 [AnnotatedString] 으로 변환해요.
 *
 * @param pointColor 볼드에 입힐 색이에요. 문단·헤딩에서는 카드 포인트 컬러, 리스트에서는 흰색을 넘겨요.
 */
private fun List<MarkdownNode>.toAnnotatedString(pointColor: Color): AnnotatedString =
    buildAnnotatedString { appendNodes(this@toAnnotatedString, pointColor) }

private fun Builder.appendNodes(
    nodes: List<MarkdownNode>,
    pointColor: Color,
) {
    nodes.forEach { node ->
        when (node) {
            is MarkdownNode.PlainText -> append(node.text)

            // 짝이 맞지 않는 `**` 는 파서가 빈 강조 노드로 만들어요. 스타일만 남기지 않도록 걸러요.
            is MarkdownNode.Bold -> if (node.children.isNotEmpty()) {
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = pointColor)) {
                    appendNodes(node.children, pointColor)
                }
            }

            is MarkdownNode.Italic -> if (node.children.isNotEmpty()) {
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                    appendNodes(node.children, pointColor)
                }
            }

            is MarkdownNode.BoldItalic -> if (node.children.isNotEmpty()) {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic,
                        color = pointColor,
                    )
                ) {
                    appendNodes(node.children, pointColor)
                }
            }

            // Compose 는 인라인 배경에 패딩을 넣을 수 없어 배경만 칠해요.
            is MarkdownNode.Code -> withStyle(
                SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = MarkdownViewDefaults.CODE_FONT_SIZE,
                    background = MarkdownColors.inlineCodeBackground,
                )
            ) {
                append(node.code)
            }

            // 블록 노드는 인라인 위치에 올 수 없어요.
            else -> Unit
        }
    }
}

/**
 * Callout 헤더 아이콘이에요.
 *
 * iOS 는 SF Symbols(`lightbulb`, `info.circle` 등)를 쓰는데 대응 에셋이 없어
 * 같은 형태로 직접 그린 벡터를 써요. 디자인 에셋이 나오면 교체 대상이에요.
 */
@DrawableRes
private fun calloutIconRes(type: CalloutType): Int = when (type) {
    CalloutType.TIP -> R.drawable.ic_callout_tip
    CalloutType.INFO -> R.drawable.ic_callout_info
    CalloutType.WARNING -> R.drawable.ic_callout_warning
    CalloutType.DANGER -> R.drawable.ic_callout_danger
    CalloutType.NOTE -> R.drawable.ic_callout_note
    CalloutType.IMPORTANT -> R.drawable.ic_callout_important
}

private object MarkdownViewDefaults {
    val BLOCK_SPACING = 16.dp
    val CODE_FONT_SIZE = 12.sp
}

@Preview(showBackground = true, backgroundColor = 0xFF1B1D1F, heightDp = 1400)
@Composable
private fun MarkdownViewPreview() {
    SoakTheme {
        Column(
            modifier = Modifier
                .background(MarkdownColors.background)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
        ) {
            MarkdownView(
                markdown = SAMPLE_MARKDOWN,
                pointColor = Color(0xFF68A4E7),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1B1D1F, heightDp = 600)
@Composable
private fun MarkdownViewCalloutPreview() {
    SoakTheme {
        Column(
            modifier = Modifier
                .background(MarkdownColors.background)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
        ) {
            MarkdownView(
                markdown = CALLOUT_SAMPLE_MARKDOWN,
                pointColor = Color(0xFF41D17F),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1B1D1F, heightDp = 1200)
@Composable
private fun MarkdownViewApiResponsePreview() {
    SoakTheme {
        Column(
            modifier = Modifier
                .background(MarkdownColors.background)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
        ) {
            MarkdownView(
                markdown = API_RESPONSE_SAMPLE_MARKDOWN,
                pointColor = Color(0xFFF38338),
            )
        }
    }
}

private val SAMPLE_MARKDOWN = """
    # Tuist 로 보는 iOS 모듈화

    ## 배경

    iOS 앱이 커지면 `.xcodeproj` 파일이 *문제*가 된다. 여러 명이 같이 편집하면 **git 충돌**이 빈번하며, 빌드 시간이 **예측 불가능**해진다.

    ## 핵심 개념

    ### DAG란?

    방향이 있고 사이클이 없는 그래프. **의존성 표현**에 표준적으로 쓰인다.

    > "같은 입력에 같은 출력" — Idempotency(멱등성)의 핵심 정의

    > [!tip]
    > `Project.swift`는 코드이지 설정 파일이 아니다. 컴파일된다.

    > [!warning]
    > 모듈을 너무 잘게 쪼개면 앱 시작 시간이 길어진다.

    ## 주요 개념

    - **Target** — Xcode의 빌드 단위
    - **DAG** — 방향이 있고 사이클 없는 그래프
    - **DSL** — 도메인 전용 선언 언어

    ## 학습 순서

    1. Tuist 공식 docs 첫 페이지
    2. module-dsl 토픽 읽기
    3. module-graph 토픽 읽기

    ---

    ## 코드 예시

    ```swift
    let project = Project(
        name: "HelloTuist",
        targets: [
            .target(name: "App", destinations: .iOS, product: .app)
        ]
    )
    ```

    ## 선택 기준표

    | 모듈 수 | 추천 도구 |
    | --- | --- |
    | 1–3개 | Xcode native |
    | 5–50개 | Tuist |
    | 100개+ | Bazel 검토 |
""".trimIndent()

private val CALLOUT_SAMPLE_MARKDOWN = """
    > [!tip]
    > 강점 활용 전략을 먼저 세우세요.

    > [!info]
    > `.external`을 기본으로 사용하면 바이너리 캐싱 가능.

    > [!warning]
    > 모듈을 너무 잘게 쪼개지 마세요.

    > [!danger]
    > 순환 의존은 런타임 crash를 유발합니다.

    > [!important]
    > 이 설정은 팀 전체에 영향을 줍니다.

    > [!note]
    > 지원하지 않는 타입은 Note 로 처리돼요.
""".trimIndent()

/** `GET /api/newsletters/exposure-contents/419/markdown` 실제 응답 발췌예요. */
private val API_RESPONSE_SAMPLE_MARKDOWN = """
    ## 메타(Meta)의 초지능 AI 연구소 동향

    메타의 초지능(Superintelligence) 연구소 내 논의는 아직 초기 단계이며, 메타 CEO인 마크 주커버그(Mark Zuckerberg)의 승인이 필요한 잠재적인 변화에 대해 결정된 바는 없습니다.

    * **오픈소스 정책에 대한 입장:** 메타 대변인은 초지능 연구소의 논의에 대한 논평을 거부했습니다.
    * **최근의 위기와 새로운 목표:** 최근 내부 경영 갈등, 직원 이탈, 기대에 못 미친 제품 출시 등 AI 기술 분야에서 난항을 겪은 메타이기에 새로운 초지능 연구소의 행보가 더욱 주목받고 있습니다.

    ### 핵심 인재 영입 및 대규모 투자

    * **파격적인 인재 영입:** 최대 1억 달러 규모의 급여 패키지를 제시하며 막대한 투자를 감행하고 있습니다.
    * **스케일 AI(Scale AI) 투자:** 메타는 지난 6월, 스케일 AI에 143억 달러를 투자했습니다.
""".trimIndent()
