package com.skgtecnologia.sisem.domain.medicalhistory.usecases

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.skgtecnologia.sisem.domain.medicalhistory.model.APPLICATION_TIME_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.CODE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.DATE_MEDICINE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.DOSE_UNIT_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GENERIC_NAME_KEY
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import org.junit.Assert
import org.junit.Test

class BuildMedicineInformationTest {

    private lateinit var buildMedicineInformation: BuildMedicineInformation

    @Test
    fun `when build medicine screen return a map`() {
        val id = "1"
        val value = "1"
        val name = "name"
        val dropDownValue = mutableStateOf(DropDownInputUiModel(id, id))
        val fieldsValues = mutableStateMapOf<String, InputUiModel>()
        fieldsValues[id] = InputUiModel(id, value, true)
        val timePickerValue = mutableStateOf(value)
        val chipValues = mutableStateMapOf<String, ChipSelectionItemUiModel>()
        chipValues[id] = ChipSelectionItemUiModel(id, name)

        buildMedicineInformation = BuildMedicineInformation()
        val result = buildMedicineInformation.invoke(
            dropDownValue,
            fieldsValues,
            timePickerValue,
            chipValues
        )

        Assert.assertEquals(dropDownValue.value.id, result[GENERIC_NAME_KEY])
        Assert.assertEquals(dropDownValue.value.name, result[CODE_KEY])
        Assert.assertEquals(timePickerValue.value, result[APPLICATION_TIME_KEY])
    }
}
