package com.fairyband.soak.presentation.deeplink

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.fairyband.soak.domain.model.NewsFeed
import com.fairyband.soak.presentation.BuildConfig
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import timber.log.Timber

internal fun kakaoShare(
    item: NewsFeed,
    color: Color,
    context: Context
) {
    val templateId = 124946L
    val textColor = String.format("%06X", color.toArgb() and 0xFFFFFF)
    // 받는 쪽은 앱 실행 링크의 exposureContentId 로 공유받은 목록에 등록해요. (SharedContentDeepLink 참고)
    val templateArgs = mapOf(
        SharedContentDeepLink.ARG_TITLE to item.title,
        "IMG" to "${BuildConfig.BASE_URL}/share/og?exposureContentId=${item.id}&textColor=%23$textColor",
        SharedContentDeepLink.ARG_EXPOSURE_CONTENT_ID to item.id.toString(),
    )

    if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
        ShareClient.instance.shareCustom(
            context,
            templateId,
            templateArgs
        ) { sharingResult, error ->
            if (error != null) {
                Timber.e("카카오톡 공유 실패: ${error.message}")
            } else if (sharingResult != null) {
                context.startActivity(sharingResult.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    } else {
        val sharerUrl = WebSharerClient.instance.makeCustomUrl(templateId, templateArgs)
        try {
            val intent = CustomTabsIntent.Builder().build().intent
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = sharerUrl
            context.startActivity(intent)
        } catch (e: Exception) {
            Timber.e("웹 공유 실패: ${e.message}")
        }
    }
}
