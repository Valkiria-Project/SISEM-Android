package com.valkiria.uicomponents.model.ui.report

import com.valkiria.uicomponents.model.ui.body.HeaderUiModel

data class ReportsDetailModel(
    val header: HeaderUiModel,
    val details: List<ReportDetailModel>
)