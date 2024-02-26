package com.valkiria.uicomponents.bricks.signature

import android.graphics.Bitmap
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import androidx.lifecycle.viewmodel.compose.viewModel

@Suppress("LongParameterList", "MagicNumber")
@Composable
fun ComposeSignature(
    modifier: Modifier = Modifier,
    signaturePadColor: Color = Color(0xFFEEEEEE),
    signatureColor: Color = Color.Black,
    signaturePadCardElevation: Dp = 4.dp,
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

    Card(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = signaturePadColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = signaturePadCardElevation,
        ),
    ) {
        Column(modifier = Modifier.fillMaxHeight()) {

            viewModel.setPathState(PathState(Path(), drawColor.value, drawBrush.value))

            val signatureBitmap = captureBitmap {
                DrawingCanvas(
                    viewModel = viewModel,
                    drawColor = drawColor,
                    drawBrush = drawBrush,
                    path = path.value,
                    modifier = modifier.height(150.dp),
                    signaturePadColor = signaturePadColor,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

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
    content: @Composable () -> Unit,
): () -> Bitmap {
    val context = LocalContext.current

    /**
     * ComposeView that would take composable as its content
     * Kept in remember so recomposition doesn't re-initialize it
     **/
    val composeView = remember { ComposeView(context) }

    /**
     * Callback function which could get latest image bitmap
     **/
    fun captureBitmap(): Bitmap {
        return composeView.drawToBitmap()
    }

    /** Use Native View inside Composable **/
    AndroidView(
        factory = {
            composeView.apply {
                setContent {
                    content.invoke()
                }
            }
        },
    )

    /** returning callback to bitmap **/
    return ::captureBitmap
}
