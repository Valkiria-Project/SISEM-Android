package com.valkiria.uicomponents.components.termsandconditions

import android.os.Parcelable
import com.valkiria.uicomponents.props.MarginsUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TermsAndConditionsUiModel(
    val margins: MarginsUiModel?
) : Parcelable
