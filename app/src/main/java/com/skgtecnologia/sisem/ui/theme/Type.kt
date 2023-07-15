package com.skgtecnologia.sisem.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
        fontFamily = montserratFontFamily
    ),
    displayMedium = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.W700,
        fontFamily = montserratFontFamily
    ),
    displaySmall = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.W500,
        fontFamily = montserratFontFamily
    ),
    headlineMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.W700,
        fontFamily = montserratFontFamily
    ),
    headlineSmall = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.W500,
        fontFamily = montserratFontFamily
    ),
    titleLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.W400,
        fontFamily = montserratFontFamily
    ),
    titleMedium = TextStyle(
        fontFamily = montserratFontFamily
    ),
    titleSmall = TextStyle(
        fontFamily = montserratFontFamily
    ),
    bodyLarge = TextStyle(
        fontFamily = montserratFontFamily
    ),
    labelLarge = TextStyle(
        fontFamily = montserratFontFamily
    ),
    labelMedium = TextStyle(
        fontFamily = montserratFontFamily
    ),
    labelSmall = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = montserratFontFamily,
        letterSpacing = 0.5.sp,
        lineHeight = 16.sp
    )
)
