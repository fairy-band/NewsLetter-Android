package com.fairyband.soak.presentation.deeplink

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import timber.log.Timber

/**
 * 콘텐츠 공유 링크를 시스템 공유 시트로 보내요. 받는 쪽은 링크로 앱을 열어 공유받은 목록에 등록해요.
 */
internal fun shareContentLink(context: Context, exposureContentId: Long) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, SharedContentDeepLink.shareUrlOf(exposureContentId))
    }
    val chooser = Intent.createChooser(sendIntent, null).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        context.startActivity(chooser)
    } catch (e: ActivityNotFoundException) {
        Timber.e(e, "공유 실패")
    }
}
