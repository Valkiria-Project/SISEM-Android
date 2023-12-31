package com.valkiria.uicomponents.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.valkiria.uicomponents.R

val montserratFontName = GoogleFont("Montserrat")
val lobsterTwoFontName = GoogleFont("Lobster Two")

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val montserratFontFamily = FontFamily(
    Font(googleFont = montserratFontName, fontProvider = provider),
    Font(googleFont = montserratFontName, fontProvider = provider, weight = FontWeight.Bold)
)

val lobsterTwoFontFamily = FontFamily(
    Font(googleFont = lobsterTwoFontName, fontProvider = provider),
    Font(googleFont = lobsterTwoFontName, fontProvider = provider, weight = FontWeight.Bold)
)

// Set of Material typography styles to start with
val UiComponentsTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = lobsterTwoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = lobsterTwoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = lobsterTwoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
