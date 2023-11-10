package com.valkiria.uicomponents.bricks.banner

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.button.ButtonUiModel

const val DEFAULT_ICON_COLOR = "#F55757" // FIXME: update with backend

data class BannerUiModel(
    val icon: String? = null,
    val iconColor: String = DEFAULT_ICON_COLOR,
    val title: String,
    val description: String,
    val leftButton: ButtonUiModel? = null,
    val rightButton: ButtonUiModel? = null
)

private var _vector: ImageVector? = null

val AssignedIncidentVector: ImageVector
    get() {
        if (_vector != null) {
            return _vector!!
        }
        _vector = ImageVector.Builder(
            name = "vector",
            defaultWidth = 27.dp,
            defaultHeight = 27.dp,
            viewportWidth = 27f,
            viewportHeight = 27f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF3CF2DD)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(13.5f, 0f)
                curveTo(16.17f, 0f, 18.7801f, 0.7918f, 21.0002f, 2.2752f)
                curveTo(23.2203f, 3.7586f, 24.9506f, 5.867f, 25.9724f, 8.3338f)
                curveTo(26.9942f, 10.8006f, 27.2615f, 13.515f, 26.7406f, 16.1337f)
                curveTo(26.2197f, 18.7525f, 24.9339f, 21.1579f, 23.0459f, 23.0459f)
                curveTo(21.1579f, 24.9339f, 18.7525f, 26.2197f, 16.1337f, 26.7406f)
                curveTo(13.515f, 27.2615f, 10.8006f, 26.9942f, 8.3338f, 25.9724f)
                curveTo(5.867f, 24.9506f, 3.7586f, 23.2203f, 2.2752f, 21.0002f)
                curveTo(0.7918f, 18.7801f, 0f, 16.17f, 0f, 13.5f)
                curveTo(0f, 9.9196f, 1.4223f, 6.4858f, 3.9541f, 3.9541f)
                curveTo(6.4858f, 1.4223f, 9.9196f, 0f, 13.5f, 0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.3f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12.45f, 18.7469f)
                verticalLineTo(12.4493f)
                horizontalLineTo(14.5485f)
                lineTo(12.45f, 18.7469f)
                close()
                moveTo(14.5485f, 10.3507f)
                horizontalLineTo(12.45f)
                verticalLineTo(8.25208f)
                horizontalLineTo(14.5485f)
                verticalLineTo(10.3507f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12.45f, 8.25203f)
                horizontalLineTo(14.5485f)
                verticalLineTo(10.3497f)
                horizontalLineTo(12.45f)
                verticalLineTo(8.25203f)
                close()
                moveTo(12.45f, 12.4501f)
                horizontalLineTo(14.5485f)
                verticalLineTo(18.7469f)
                horizontalLineTo(12.45f)
                verticalLineTo(12.4501f)
                close()
                moveTo(13.4997f, 3.00415f)
                curveTo(11.424f, 3.0042f, 9.395f, 3.6197f, 7.6691f, 4.7729f)
                curveTo(5.9432f, 5.926f, 4.5981f, 7.5651f, 3.8038f, 9.4828f)
                curveTo(3.0094f, 11.4005f, 2.8016f, 13.5106f, 3.2065f, 15.5464f)
                curveTo(3.6115f, 17.5822f, 4.611f, 19.4522f, 6.0788f, 20.9199f)
                curveTo(7.5465f, 22.3877f, 9.4165f, 23.3872f, 11.4523f, 23.7922f)
                curveTo(13.4881f, 24.1971f, 15.5982f, 23.9893f, 17.5159f, 23.1949f)
                curveTo(19.4336f, 22.4006f, 21.0727f, 21.0555f, 22.2259f, 19.3296f)
                curveTo(23.379f, 17.6037f, 23.9945f, 15.5747f, 23.9945f, 13.499f)
                curveTo(23.9936f, 10.7159f, 22.8875f, 8.0471f, 20.9196f, 6.0791f)
                curveTo(18.9516f, 4.1112f, 16.2828f, 3.0051f, 13.4997f, 3.0042f)
                close()
                moveTo(13.4997f, 21.8952f)
                curveTo(11.8391f, 21.8952f, 10.2158f, 21.4028f, 8.835f, 20.4802f)
                curveTo(7.4543f, 19.5576f, 6.3781f, 18.2463f, 5.7426f, 16.7121f)
                curveTo(5.1071f, 15.1779f, 4.9408f, 13.4897f, 5.2648f, 11.861f)
                curveTo(5.5888f, 10.2322f, 6.3884f, 8.7362f, 7.5627f, 7.562f)
                curveTo(8.7369f, 6.3877f, 10.233f, 5.588f, 11.8617f, 5.2641f)
                curveTo(13.4904f, 4.9401f, 15.1786f, 5.1064f, 16.7128f, 5.7419f)
                curveTo(18.247f, 6.3774f, 19.5583f, 7.4535f, 20.4809f, 8.8343f)
                curveTo(21.4035f, 10.215f, 21.896f, 11.8384f, 21.896f, 13.499f)
                curveTo(21.893f, 15.7249f, 21.0075f, 17.8588f, 19.4335f, 19.4328f)
                curveTo(17.8595f, 21.0067f, 15.7256f, 21.8923f, 13.4997f, 21.8952f)
                close()
            }
        }.build()
        return _vector!!
    }
