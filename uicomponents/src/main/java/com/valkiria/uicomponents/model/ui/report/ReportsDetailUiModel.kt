package com.valkiria.uicomponents.model.ui.report

import com.valkiria.uicomponents.components.body.HeaderUiModel

data class ReportsDetailUiModel(
    val header: HeaderUiModel,
    val details: List<ReportDetailUiModel>
)
