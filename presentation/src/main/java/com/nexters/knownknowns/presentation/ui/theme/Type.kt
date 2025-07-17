package com.nexters.knownknowns.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.nexters.knownknowns.presentation.R

val PretendardBold = FontFamily(Font(R.font.pretendard_bold))
val PretendardSemiBold = FontFamily(Font(R.font.pretendard_semibold))
val PretendardMedium = FontFamily(Font(R.font.pretendard_medium))
val PretendardRegular = FontFamily(Font(R.font.pretendard_regular))

@Stable
class KnownKnownsTypography(
    head28Bold: TextStyle,
    head28SemiBold: TextStyle,
    head28Medium: TextStyle,
    head28Regular: TextStyle,
    head26Bold: TextStyle,
    head26SemiBold: TextStyle,
    head26Medium: TextStyle,
    head26Regular: TextStyle,
    head24Bold: TextStyle,
    head24SemiBold: TextStyle,
    head24Medium: TextStyle,
    head24Regular: TextStyle,
    head22Bold: TextStyle,
    head22SemiBold: TextStyle,
    head22Medium: TextStyle,
    head22Regular: TextStyle,
    head20Bold: TextStyle,
    head20SemiBold: TextStyle,
    head20Medium: TextStyle,
    head20Regular: TextStyle,
    body18Bold: TextStyle,
    body18SemiBold: TextStyle,
    body18Medium: TextStyle,
    body18Regular: TextStyle,
    body16Bold: TextStyle,
    body16SemiBold: TextStyle,
    body16Medium: TextStyle,
    body16Regular: TextStyle,
    body15Bold: TextStyle,
    body15SemiBold: TextStyle,
    body15Medium: TextStyle,
    body15Regular: TextStyle,
    body14Bold: TextStyle,
    body14SemiBold: TextStyle,
    body14Medium: TextStyle,
    body14Regular: TextStyle,
    body13Bold: TextStyle,
    body13SemiBold: TextStyle,
    body13Medium: TextStyle,
    body13Regular: TextStyle,
    caption12Bold: TextStyle,
    caption12SemiBold: TextStyle,
    caption12Medium: TextStyle,
    caption12Regular: TextStyle,
    caption11Bold: TextStyle,
    caption11SemiBold: TextStyle,
    caption11Medium: TextStyle,
    caption11Regular: TextStyle,
) {
    var head28Bold by mutableStateOf(head28Bold)
        private set
    var head28SemiBold by mutableStateOf(head28SemiBold)
        private set
    var head28Medium by mutableStateOf(head28Medium)
        private set
    var head28Regular by mutableStateOf(head28Regular)
        private set
    var head26Bold by mutableStateOf(head26Bold)
        private set
    var head26SemiBold by mutableStateOf(head26SemiBold)
        private set
    var head26Medium by mutableStateOf(head26Medium)
        private set
    var head26Regular by mutableStateOf(head26Regular)
        private set
    var head24Bold by mutableStateOf(head24Bold)
        private set
    var head24SemiBold by mutableStateOf(head24SemiBold)
        private set
    var head24Medium by mutableStateOf(head24Medium)
        private set
    var head24Regular by mutableStateOf(head24Regular)
        private set
    var head22Bold by mutableStateOf(head22Bold)
        private set
    var head22SemiBold by mutableStateOf(head22SemiBold)
        private set
    var head22Medium by mutableStateOf(head22Medium)
        private set
    var head22Regular by mutableStateOf(head22Regular)
        private set
    var head20Bold by mutableStateOf(head20Bold)
        private set
    var head20SemiBold by mutableStateOf(head20SemiBold)
        private set
    var head20Medium by mutableStateOf(head20Medium)
        private set
    var head20Regular by mutableStateOf(head20Regular)
        private set
    var body18Bold by mutableStateOf(body18Bold)
        private set
    var body18SemiBold by mutableStateOf(body18SemiBold)
        private set
    var body18Medium by mutableStateOf(body18Medium)
        private set
    var body18Regular by mutableStateOf(body18Regular)
        private set
    var body16Bold by mutableStateOf(body16Bold)
        private set
    var body16SemiBold by mutableStateOf(body16SemiBold)
        private set
    var body16Medium by mutableStateOf(body16Medium)
        private set
    var body16Regular by mutableStateOf(body16Regular)
        private set
    var body15Bold by mutableStateOf(body15Bold)
        private set
    var body15SemiBold by mutableStateOf(body15SemiBold)
        private set
    var body15Medium by mutableStateOf(body15Medium)
        private set
    var body15Regular by mutableStateOf(body15Regular)
        private set
    var body14Bold by mutableStateOf(body14Bold)
        private set
    var body14SemiBold by mutableStateOf(body14SemiBold)
        private set
    var body14Medium by mutableStateOf(body14Medium)
        private set
    var body14Regular by mutableStateOf(body14Regular)
        private set
    var body13Bold by mutableStateOf(body13Bold)
        private set
    var body13SemiBold by mutableStateOf(body13SemiBold)
        private set
    var body13Medium by mutableStateOf(body13Medium)
        private set
    var body13Regular by mutableStateOf(body13Regular)
        private set
    var caption12Bold by mutableStateOf(caption12Bold)
        private set
    var caption12SemiBold by mutableStateOf(caption12SemiBold)
        private set
    var caption12Medium by mutableStateOf(caption12Medium)
        private set
    var caption12Regular by mutableStateOf(caption12Regular)
        private set
    var caption11Bold by mutableStateOf(caption11Bold)
        private set
    var caption11SemiBold by mutableStateOf(caption11SemiBold)
        private set
    var caption11Medium by mutableStateOf(caption11Medium)
        private set
    var caption11Regular by mutableStateOf(caption11Regular)
        private set

    fun copy(
        head28Bold: TextStyle = this.head28Bold,
        head28SemiBold: TextStyle = this.head28SemiBold,
        head28Medium: TextStyle = this.head28Medium,
        head28Regular: TextStyle = this.head28Regular,
        head26Bold: TextStyle = this.head26Bold,
        head26SemiBold: TextStyle = this.head26SemiBold,
        head26Medium: TextStyle = this.head26Medium,
        head26Regular: TextStyle = this.head26Regular,
        head24Bold: TextStyle = this.head24Bold,
        head24SemiBold: TextStyle = this.head24SemiBold,
        head24Medium: TextStyle = this.head24Medium,
        head24Regular: TextStyle = this.head24Regular,
        head22Bold: TextStyle = this.head22Bold,
        head22SemiBold: TextStyle = this.head22SemiBold,
        head22Medium: TextStyle = this.head22Medium,
        head22Regular: TextStyle = this.head22Regular,
        head20Bold: TextStyle = this.head20Bold,
        head20SemiBold: TextStyle = this.head20SemiBold,
        head20Medium: TextStyle = this.head20Medium,
        head20Regular: TextStyle = this.head20Regular,
        body18Bold: TextStyle = this.body18Bold,
        body18SemiBold: TextStyle = this.body18SemiBold,
        body18Medium: TextStyle = this.body18Medium,
        body18Regular: TextStyle = this.body18Regular,
        body16Bold: TextStyle = this.body16Bold,
        body16SemiBold: TextStyle = this.body16SemiBold,
        body16Medium: TextStyle = this.body16Medium,
        body16Regular: TextStyle = this.body16Regular,
        body15Bold: TextStyle = this.body15Bold,
        body15SemiBold: TextStyle = this.body15SemiBold,
        body15Medium: TextStyle = this.body15Medium,
        body15Regular: TextStyle = this.body15Regular,
        body14Bold: TextStyle = this.body14Bold,
        body14SemiBold: TextStyle = this.body14SemiBold,
        body14Medium: TextStyle = this.body14Medium,
        body14Regular: TextStyle = this.body14Regular,
        body13Bold: TextStyle = this.body13Bold,
        body13SemiBold: TextStyle = this.body13SemiBold,
        body13Medium: TextStyle = this.body13Medium,
        body13Regular: TextStyle = this.body13Regular,
        caption12Bold: TextStyle = this.caption12Bold,
        caption12SemiBold: TextStyle = this.caption12SemiBold,
        caption12Medium: TextStyle = this.caption12Medium,
        caption12Regular: TextStyle = this.caption12Regular,
        caption11Bold: TextStyle = this.caption11Bold,
        caption11SemiBold: TextStyle = this.caption11SemiBold,
        caption11Medium: TextStyle = this.caption11Medium,
        caption11Regular: TextStyle = this.caption11Regular,
    ) = KnownKnownsTypography(
        head28Bold, head28SemiBold, head28Medium, head28Regular,
        head26Bold, head26SemiBold, head26Medium, head26Regular,
        head24Bold, head24SemiBold, head24Medium, head24Regular,
        head22Bold, head22SemiBold, head22Medium, head22Regular,
        head20Bold, head20SemiBold, head20Medium, head20Regular,
        body18Bold, body18SemiBold, body18Medium, body18Regular,
        body16Bold, body16SemiBold, body16Medium, body16Regular,
        body15Bold, body15SemiBold, body15Medium, body15Regular,
        body14Bold, body14SemiBold, body14Medium, body14Regular,
        body13Bold, body13SemiBold, body13Medium, body13Regular,
        caption12Bold, caption12SemiBold, caption12Medium, caption12Regular,
        caption11Bold, caption11SemiBold, caption11Medium, caption11Regular
    )

    fun update(other: KnownKnownsTypography) {
        head28Bold = other.head28Bold
        head28SemiBold = other.head28SemiBold
        head28Medium = other.head28Medium
        head28Regular = other.head28Regular
        head26Bold = other.head26Bold
        head26SemiBold = other.head26SemiBold
        head26Medium = other.head26Medium
        head26Regular = other.head26Regular
        head24Bold = other.head24Bold
        head24SemiBold = other.head24SemiBold
        head24Medium = other.head24Medium
        head24Regular = other.head24Regular
        head22Bold = other.head22Bold
        head22SemiBold = other.head22SemiBold
        head22Medium = other.head22Medium
        head22Regular = other.head22Regular
        head20Bold = other.head20Bold
        head20SemiBold = other.head20SemiBold
        head20Medium = other.head20Medium
        head20Regular = other.head20Regular
        body18Bold = other.body18Bold
        body18SemiBold = other.body18SemiBold
        body18Medium = other.body18Medium
        body18Regular = other.body18Regular
        body16Bold = other.body16Bold
        body16SemiBold = other.body16SemiBold
        body16Medium = other.body16Medium
        body16Regular = other.body16Regular
        body15Bold = other.body15Bold
        body15SemiBold = other.body15SemiBold
        body15Medium = other.body15Medium
        body15Regular = other.body15Regular
        body14Bold = other.body14Bold
        body14SemiBold = other.body14SemiBold
        body14Medium = other.body14Medium
        body14Regular = other.body14Regular
        body13Bold = other.body13Bold
        body13SemiBold = other.body13SemiBold
        body13Medium = other.body13Medium
        body13Regular = other.body13Regular
        caption12Bold = other.caption12Bold
        caption12SemiBold = other.caption12SemiBold
        caption12Medium = other.caption12Medium
        caption12Regular = other.caption12Regular
        caption11Bold = other.caption11Bold
        caption11SemiBold = other.caption11SemiBold
        caption11Medium = other.caption11Medium
        caption11Regular = other.caption11Regular
    }
}

@Composable
fun KnownKnownsTypography(): KnownKnownsTypography =
    KnownKnownsTypography(
        head28Bold = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 28.sp,
            lineHeight = 1.36.em
        ),
        head28SemiBold = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 28.sp,
            lineHeight = 1.36.em
        ),
        head28Medium = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 28.sp,
            lineHeight = 1.36.em
        ),
        head28Regular = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 28.sp,
            lineHeight = 1.36.em
        ),
        head26Bold = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 26.sp,
            lineHeight = 1.35.em
        ),
        head26SemiBold = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 26.sp,
            lineHeight = 1.35.em
        ),
        head26Medium = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 26.sp,
            lineHeight = 1.35.em
        ),
        head26Regular = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 26.sp,
            lineHeight = 1.35.em
        ),
        head24Bold = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 24.sp,
            lineHeight = 1.34.em
        ),
        head24SemiBold = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 24.sp,
            lineHeight = 1.34.em
        ),
        head24Medium = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 24.sp,
            lineHeight = 1.34.em
        ),
        head24Regular = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 24.sp,
            lineHeight = 1.34.em
        ),
        head22Bold = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 22.sp,
            lineHeight = 1.4.em
        ),
        head22SemiBold = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 22.sp,
            lineHeight = 1.4.em
        ),
        head22Medium = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 22.sp,
            lineHeight = 1.4.em
        ),
        head22Regular = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 22.sp,
            lineHeight = 1.4.em
        ),
        head20Bold = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 20.sp,
            lineHeight = 1.4.em
        ),
        head20SemiBold = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 20.sp,
            lineHeight = 1.4.em
        ),
        head20Medium = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 20.sp,
            lineHeight = 1.4.em
        ),
        head20Regular = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 20.sp,
            lineHeight = 1.4.em
        ),
        body18Bold = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 18.sp,
            lineHeight = 1.45.em
        ),
        body18SemiBold = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 18.sp,
            lineHeight = 1.45.em
        ),
        body18Medium = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 18.sp,
            lineHeight = 1.45.em
        ),
        body18Regular = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 18.sp,
            lineHeight = 1.45.em
        ),
        body16Bold = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 16.sp,
            lineHeight = 1.5.em
        ),
        body16SemiBold = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 16.sp,
            lineHeight = 1.5.em
        ),
        body16Medium = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 16.sp,
            lineHeight = 1.5.em
        ),
        body16Regular = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 16.sp,
            lineHeight = 1.5.em
        ),
        body15Bold = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 15.sp,
            lineHeight = 1.47.em
        ),
        body15SemiBold = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 15.sp,
            lineHeight = 1.47.em
        ),
        body15Medium = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 15.sp,
            lineHeight = 1.47.em
        ),
        body15Regular = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 15.sp,
            lineHeight = 1.47.em
        ),
        body14Bold = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 14.sp,
            lineHeight = 1.43.em
        ),
        body14SemiBold = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 14.sp,
            lineHeight = 1.43.em
        ),
        body14Medium = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 14.sp,
            lineHeight = 1.43.em
        ),
        body14Regular = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 14.sp,
            lineHeight = 1.43.em
        ),
        body13Bold = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 13.sp,
            lineHeight = 1.38.em
        ),
        body13SemiBold = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 13.sp,
            lineHeight = 1.38.em
        ),
        body13Medium = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 13.sp,
            lineHeight = 1.38.em
        ),
        body13Regular = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 13.sp,
            lineHeight = 1.38.em
        ),
        caption12Bold = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 12.sp,
            lineHeight = 1.35.em
        ),
        caption12SemiBold = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 12.sp,
            lineHeight = 1.35.em
        ),
        caption12Medium = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 12.sp,
            lineHeight = 1.35.em
        ),
        caption12Regular = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 12.sp,
            lineHeight = 1.35.em
        ),
        caption11Bold = TextStyle(
            fontFamily = PretendardBold,
            fontSize = 11.sp,
            lineHeight = 1.28.em
        ),
        caption11SemiBold = TextStyle(
            fontFamily = PretendardSemiBold,
            fontSize = 11.sp,
            lineHeight = 1.28.em
        ),
        caption11Medium = TextStyle(
            fontFamily = PretendardMedium,
            fontSize = 11.sp,
            lineHeight = 1.28.em
        ),
        caption11Regular = TextStyle(
            fontFamily = PretendardRegular,
            fontSize = 11.sp,
            lineHeight = 1.28.em
        ),
    )
