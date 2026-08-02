package com.valkiria.uicomponents.bricks.signature

import android.graphics.Bitmap
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import androidx.lifecycle.viewmodel.compose.viewModel

@Suppress("LongParameterList", "MagicNumber")
@Composable
fun ComposeSignature(
    modifier: Modifier = Modifier,
    canvasModifier: Modifier = Modifier.height(150.dp),
    fillHeight: Boolean = false,
    signaturePadColor: Color = Color(0xFFEEEEEE),
    signatureColor: Color = Color.Black,
    signatureThickness: Float = 10f,
    hasAlpha: Boolean = false,
    completeComponent: @Composable (onClick: () -> Unit) -> Unit,
    clearComponent: @Composable (onClick: () -> Unit) -> Unit,
    onComplete: (Bitmap?) -> Unit,
    onClear: () -> Unit = {},
) {
    val viewModel: SignaturePadViewModel = viewModel()
    val path = viewModel.path
    val drawColor = remember { mutableStateOf(signatureColor) }
    val drawBrush = remember { mutableStateOf(signatureThickness) }

    Column(
        modifier = if (fillHeight) {
            modifier
        } else {
            modifier.wrapContentWidth().wrapContentHeight()
        }
    ) {
        viewModel.setPathState(PathState(Path(), drawColor.value, drawBrush.value))

        val signatureBitmap = signatureCanvas(
            fillHeight = fillHeight,
            viewModel = viewModel,
            drawColor = drawColor,
            drawBrush = drawBrush,
            path = path,
            signaturePadColor = signaturePadColor,
            modifier = modifier,
            canvasModifier = canvasModifier
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .padding(horizontal = 16.dp)
                .background(color = MaterialTheme.colorScheme.primary)
        )

        if (!fillHeight) {
            Spacer(modifier = Modifier.weight(1f))
        }

        SignatureButtons(
            modifier = modifier,
            viewModel = viewModel,
            signatureBitmap = signatureBitmap,
            hasAlpha = hasAlpha,
            clearComponent = clearComponent,
            completeComponent = completeComponent,
            onClear = onClear,
            onComplete = onComplete
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun ColumnScope.signatureCanvas(
    fillHeight: Boolean,
    viewModel: SignaturePadViewModel,
    drawColor: MutableState<Color>,
    drawBrush: MutableState<Float>,
    path: androidx.compose.runtime.State<MutableList<PathState>>,
    signaturePadColor: Color,
    modifier: Modifier,
    canvasModifier: Modifier
): () -> Bitmap {
    return if (fillHeight) {
        captureBitmap(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
        ) {
            DrawingCanvas(
                viewModel = viewModel,
                drawColor = drawColor,
                drawBrush = drawBrush,
                path = path.value,
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                signaturePadColor = signaturePadColor,
            )
        }
    } else {
        captureBitmap {
            DrawingCanvas(
                viewModel = viewModel,
                drawColor = drawColor,
                drawBrush = drawBrush,
                path = path.value,
                modifier = modifier.then(canvasModifier),
                signaturePadColor = signaturePadColor,
            )
        }
    }
}

@Suppress("LongParameterList")
@Composable
private fun SignatureButtons(
    modifier: Modifier,
    viewModel: SignaturePadViewModel,
    signatureBitmap: () -> Bitmap,
    hasAlpha: Boolean,
    clearComponent: @Composable (onClick: () -> Unit) -> Unit,
    completeComponent: @Composable (onClick: () -> Unit) -> Unit,
    onClear: () -> Unit,
    onComplete: (Bitmap?) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        clearComponent {
            onClear()
            viewModel.clearPathState()
        }
        completeComponent {
            onComplete(
                if (viewModel.isValidSignature) {
                    signatureBitmap.invoke().apply {
                        setHasAlpha(hasAlpha)
                    }
                } else {
                    null
                },
            )
        }
    }
}

@Suppress("LongParameterList")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    viewModel: SignaturePadViewModel,
    drawColor: MutableState<Color>,
    drawBrush: MutableState<Float>,
    path: MutableList<PathState>,
    modifier: Modifier,
    signaturePadColor: Color,
) {
    val currentPath = path.last().path
    val movePath = remember { mutableStateOf<Offset?>(null) }

    Canvas(
        modifier = modifier
            .background(signaturePadColor)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        currentPath.moveTo(it.x, it.y)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        movePath.value = Offset(it.x, it.y)
                    }

                    else -> {
                        movePath.value = null
                    }
                }
                true
            },
    ) {
        movePath.value?.let {
            viewModel.isValidSignature = true
            currentPath.lineTo(it.x, it.y)
            drawPath(
                path = currentPath,
                color = drawColor.value,
                style = Stroke(drawBrush.value),
            )
        }
        path.forEach {
            drawPath(
                path = it.path,
                color = it.color,
                style = Stroke(it.stroke),
            )
        }
    }
}

@Composable
fun ButtonComponent(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: Int? = null,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Red)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (icon != null) {
                Icon(
                    painter = painterResource(id = icon),
                    tint = Color.White,
                    contentDescription = null,
                )
            }
            Text(
                text = title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
fun captureBitmap(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
): () -> Bitmap {
    val context = LocalContext.current

    val composeView = remember { ComposeView(context) }

    fun captureBitmap(): Bitmap = composeView.drawToBitmap()

    AndroidView(
        modifier = modifier,
        factory = {
            composeView.apply {
                // MATCH_PARENT so the ComposeView fills the AndroidView's measured bounds,
                // ensuring drawToBitmap() captures the full visible area and not just
                // the default WRAP_CONTENT size.
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                )
                setContent {
                    content.invoke()
                }
            }
        },
    )

    return ::captureBitmap
}
