package com.fairyband.soak.presentation.analytics

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

object SoakAnalytics {

    private const val PARAM_OBJECT_TYPE = "object_type"
    private const val PARAM_OBJECT_SECTION = "object_section"
    private const val PARAM_OBJECT_ID = "object_id"
    private const val PARAM_LIST_INDEX = "list_index"
    private const val PARAM_CARD_INDEX = "card_index"
    private const val PARAM_JOB_GROUP = "job_group"
    private const val PARAM_CAREER_LEVEL = "career_level"
    private const val PARAM_PLATFORM = "platform"
    private const val PARAM_PREVIOUS_SCREEN = "previous_screen"
    private const val PARAM_ARGUMENTS = "arguments"

    fun logPageViewMain() {
        Firebase.analytics.logEvent("pageview_main") {}
    }

    fun logPageViewBottomSheetCustom() {
        Firebase.analytics.logEvent("pageview_bottom_sheet_custom") {
            param(PARAM_OBJECT_TYPE, "bottom_sheet")
        }
    }

    fun logPageViewNewsletterCarousel() {
        Firebase.analytics.logEvent("pageview_newsletter_carousel") {
            param(PARAM_OBJECT_TYPE, "newsletter")
        }
    }

    fun logPageViewBottomSheetNotification() {
        Firebase.analytics.logEvent("pageview_bottom_sheet_notification") {
            param(PARAM_OBJECT_TYPE, "bottom_sheet")
        }
    }

    fun logPageView(screenName: String, previousScreenName: String, arguments: String) {
        Firebase.analytics.logEvent("page_view") {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(PARAM_PLATFORM, "Android")
            param(PARAM_PREVIOUS_SCREEN, previousScreenName)
            param(PARAM_ARGUMENTS, arguments)
        }
    }

    fun logClickMain(title: String, listIndex: Int) {
        Firebase.analytics.logEvent("click_main") {
            param(PARAM_OBJECT_SECTION, "newsletter_list")
            param(PARAM_OBJECT_TYPE, "newsletter")
            param(PARAM_OBJECT_ID, title)
            param(PARAM_LIST_INDEX, listIndex.toLong())
        }
    }

    fun logClickBottomSheetCustom(jobGroup: List<String>, careerLevel: String) {
        Firebase.analytics.logEvent("click_bottom_sheet_custom") {
            param(PARAM_OBJECT_TYPE, "button")
            param(PARAM_JOB_GROUP, jobGroup.joinToString(separator = ","))
            param(PARAM_CAREER_LEVEL, careerLevel)
        }
    }

    fun logClickBottomSheetNotification() {
        Firebase.analytics.logEvent("click_bottom_sheet_notification") {
            param(PARAM_OBJECT_TYPE, "button")
        }
    }

    fun logClickNewsletterCarousel(id: Long, cardIndex: Long) {
        Firebase.analytics.logEvent("click_newsletter_carousel") {
            param(PARAM_OBJECT_SECTION, "newsletter_card")
            param(PARAM_OBJECT_TYPE, "button")
            param(PARAM_OBJECT_ID, id.toString())
            param(PARAM_CARD_INDEX, cardIndex)
        }
    }

    fun logClickNewsletterCarousel(id: String, cardIndex: String) {
        Firebase.analytics.logEvent("click_newsletter_carousel") {
            param(PARAM_OBJECT_SECTION, "newsletter_card")
            param(PARAM_OBJECT_TYPE, "button")
            param(PARAM_OBJECT_ID, id)
            param(PARAM_CARD_INDEX, cardIndex)
        }
    }

    fun logImpressionNewsletterCarousel(title: String, cardIndex: Long) {
        Firebase.analytics.logEvent("impression_newsletter_carousel") {
            param(PARAM_OBJECT_SECTION, "newsletter_card")
            param(PARAM_OBJECT_TYPE, "newsletter")
            param(PARAM_OBJECT_ID, title)
            param(PARAM_CARD_INDEX, cardIndex)
        }
    }
}
