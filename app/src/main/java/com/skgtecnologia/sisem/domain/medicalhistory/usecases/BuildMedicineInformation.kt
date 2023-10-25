package com.skgtecnologia.sisem.domain.medicalhistory.usecases

import androidx.annotation.CheckResult
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.skgtecnologia.sisem.domain.medicalhistory.model.ADMINISTRATION_ROUTE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.APPLICATION_TIME_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.APPLIED_DOSE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.CODE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.DATE_MEDICINE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.DOSE_UNIT_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GENERIC_NAME_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.QUANTITY_USED_KEY
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import javax.inject.Inject

class BuildMedicineInformation @Inject constructor() {

    @CheckResult
    operator fun invoke(
        dropDownValue: MutableState<DropDownInputUiModel>,
        fieldsValues: SnapshotStateMap<String, InputUiModel>,
        timePickerValue: MutableState<String>,
        chipValues: SnapshotStateMap<String, ChipSelectionItemUiModel>
    ): Map<String, String> = mapOf(
        GENERIC_NAME_KEY to dropDownValue.value.id,
        CODE_KEY to dropDownValue.value.name,
        DATE_MEDICINE_KEY to fieldsValues[DATE_MEDICINE_KEY]?.updatedValue.orEmpty(),
        APPLICATION_TIME_KEY to timePickerValue.value,
        DOSE_UNIT_KEY to chipValues[DOSE_UNIT_KEY]?.id.orEmpty(),
        APPLIED_DOSE_KEY to fieldsValues[APPLIED_DOSE_KEY]?.updatedValue.orEmpty(),
        QUANTITY_USED_KEY to fieldsValues[QUANTITY_USED_KEY]?.updatedValue.orEmpty(),
        ADMINISTRATION_ROUTE_KEY to chipValues[ADMINISTRATION_ROUTE_KEY]?.id.orEmpty()
    )
}
