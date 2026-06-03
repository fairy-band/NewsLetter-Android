package com.fairyband.soak.presentation.analytics

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

object SoakAnalytics {

    private const val PARAM_CARD_INDEX = "card_index"
    private const val PARAM_CARD_TYPE = "card_type"
    private const val PARAM_CONTENT_TYPE = "content_type"
    private const val PARAM_CONTENT_TITLE = "content_title"
    private const val PARAM_CONTENT_ID = "content_id"
    private const val PARAM_ACTION_TYPE = "action_type"
    private const val PARAM_ORDER_BY = "order_by"
    private const val PARAM_FILTER_VALUE = "filter_value"
    private const val PARAM_JOB_GROUP = "job_group"
    private const val PARAM_CAREER_LEVEL = "career_level"
    private const val PARAM_NAME = "name"
    private const val PARAM_URL = "url"
    private const val PARAM_LANGUAGE = "language"
    private const val PARAM_PLATFORM = "platform"
    private const val PARAM_PREVIOUS_SCREEN = "previous_screen"
    private const val PARAM_ARGUMENTS = "arguments"

    // Main

    fun logMainPageview() {
        Firebase.analytics.logEvent("main_pageview") {}
    }

    fun logMainContentsDetailPageview(
        cardIndex: Int,
        cardType: String,
        contentType: String,
        contentTitle: String,
        contentId: String,
    ) {
        Firebase.analytics.logEvent("main_contents_detail_pageview") {
            param(PARAM_CARD_INDEX, cardIndex.toLong())
            param(PARAM_CARD_TYPE, cardType)
            param(PARAM_CONTENT_TYPE, contentType)
            param(PARAM_CONTENT_TITLE, contentTitle)
            param(PARAM_CONTENT_ID, contentId)
        }
    }

    fun logMainContentsDetailClick(
        cardType: String,
        contentType: String,
        contentTitle: String,
        contentId: String,
    ) {
        Firebase.analytics.logEvent("main_contents_detail_click") {
            param(PARAM_CARD_TYPE, cardType)
            param(PARAM_CONTENT_TYPE, contentType)
            param(PARAM_CONTENT_TITLE, contentTitle)
            param(PARAM_CONTENT_ID, contentId)
        }
    }

    // Explore

    fun logExplorePageview() {
        Firebase.analytics.logEvent("explore_pageview") {}
    }

    fun logExploreContentsDetailPageview(
        cardIndex: Int,
        contentType: String,
        contentTitle: String,
        contentId: String,
    ) {
        Firebase.analytics.logEvent("explore_contents_detail_pageview") {
            param(PARAM_CARD_INDEX, cardIndex.toLong())
            param(PARAM_CONTENT_TYPE, contentType)
            param(PARAM_CONTENT_TITLE, contentTitle)
            param(PARAM_CONTENT_ID, contentId)
        }
    }

    fun logExploreContentsDetailClick(
        contentType: String,
        contentTitle: String,
        contentId: String,
    ) {
        Firebase.analytics.logEvent("explore_contents_detail_click") {
            param(PARAM_CONTENT_TYPE, contentType)
            param(PARAM_CONTENT_TITLE, contentTitle)
            param(PARAM_CONTENT_ID, contentId)
        }
    }

    fun logExploreOrderClick(orderBy: String) {
        Firebase.analytics.logEvent("explore_click") {
            param(PARAM_ACTION_TYPE, "order")
            param(PARAM_ORDER_BY, orderBy)
        }
    }

    fun logExploreFilterClick(filterValue: String) {
        Firebase.analytics.logEvent("explore_click") {
            param(PARAM_ACTION_TYPE, "filter")
            param(PARAM_FILTER_VALUE, filterValue)
        }
    }

    fun logExploreReportPageview() {
        Firebase.analytics.logEvent("explore_report_pageview") {}
    }

    fun logExploreReportSubmitClick(
        name: String,
        url: String,
        jobGroup: String,
        language: String,
    ) {
        Firebase.analytics.logEvent("explore_report_click") {
            param(PARAM_ACTION_TYPE, "submit_report_form")
            param(PARAM_NAME, name)
            param(PARAM_URL, url)
            param(PARAM_JOB_GROUP, jobGroup)
            param(PARAM_LANGUAGE, language)
        }
    }

    // Bottom sheet

    fun logBottomSheetNotificationPageview() {
        Firebase.analytics.logEvent("bottom_sheet_notification_pageview") {}
    }

    fun logBottomSheetNotificationClick() {
        Firebase.analytics.logEvent("bottom_sheet_notification_click") {}
    }

    fun logBottomSheetCustomPageview() {
        Firebase.analytics.logEvent("bottom_sheet_custom_pageview") {}
    }

    fun logBottomSheetCustomClick(jobGroup: List<String>, careerLevel: String) {
        Firebase.analytics.logEvent("bottom_sheet_custom_click") {
            param(PARAM_JOB_GROUP, jobGroup.joinToString(separator = ","))
            param(PARAM_CAREER_LEVEL, careerLevel)
        }
    }

    // Navigation

    fun logPageView(screenName: String, previousScreenName: String, arguments: String) {
        Firebase.analytics.logEvent("page_view") {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(PARAM_PLATFORM, "Android")
            param(PARAM_PREVIOUS_SCREEN, previousScreenName)
            param(PARAM_ARGUMENTS, arguments)
        }
    }
}

fun String.toContentType(): String = when (this) {
    "NEWS" -> "newsletter"
    "BLOG" -> "blog"
    "BOOK" -> "book"
    else -> this.lowercase()
}
