package com.valkiria.uicomponents.bricks.signature

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SignaturePadViewModel : ViewModel() {

    @SuppressLint("MutableCollectionMutableState")
    private val _path = mutableStateOf(mutableListOf<PathState>())
    val path: State<MutableList<PathState>> = _path
    var isValidSignature: Boolean = false

    fun setPathState(value: PathState) {
        _path.value.add(value)
    }

    fun clearPathState() {
        isValidSignature = false
        _path.value = mutableListOf()
    }
}
