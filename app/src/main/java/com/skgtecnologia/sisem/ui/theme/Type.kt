package com.skgtecnologia.sisem.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.valkiria.uicomponents.R

val montserratFontName = GoogleFont("Montserrat")

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val montserratFontFamily = FontFamily(
    Font(googleFont = montserratFontName, fontProvider = provider),
    Font(googleFont = montserratFontName, fontProvider = provider, weight = FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontSize = 25.sp,
        fontWeight = FontWeight.W500,
        fontFamily = montserratFontFamily,
    ),
    displayMedium = TextStyle(
        fontFamily = montserratFontFamily,
    ),
    displaySmall = TextStyle(
        fontFamily = montserratFontFamily,
    ),
    bodyLarge = TextStyle(
        fontFamily = montserratFontFamily,
    ),
    headlineMedium = TextStyle(
        fontFamily = montserratFontFamily,
    ),
    headlineSmall = TextStyle(
        fontFamily = montserratFontFamily,
    ),
    titleLarge = TextStyle(
        fontFamily = montserratFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = montserratFontFamily,
    ),
    titleSmall = TextStyle(
        fontFamily = montserratFontFamily,
    ),
    labelLarge = TextStyle(
        fontFamily = montserratFontFamily,
    ),
    labelMedium = TextStyle(
        fontFamily = montserratFontFamily,
    ),
    labelSmall = TextStyle(
        fontFamily = montserratFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
