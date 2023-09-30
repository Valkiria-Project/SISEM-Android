package com.skgtecnologia.sisem.ui.humanbody.area

const val BASE_WIDTH = 1440 - ((40 * 160) / 1400)
const val BASE_HEIGHT = 2900
const val REAL_BASE_HEIGHT = 3120

data class Point(val x: Float, val y: Float)

fun Point.toProportionalPoint(width: Int, height: Int): Point = Point(
    x = this.x * width / BASE_WIDTH,
    y = if (height == BASE_HEIGHT) {
        this.y * height / BASE_HEIGHT
    } else {
        this.y * height / REAL_BASE_HEIGHT
    }
)

@Suppress("NestedBlockDepth")
fun List<Point>.isInArea(x: Float, y: Float): Boolean {
    var inside = false
    var p1x = this[this.size - 1].x
    var p1y = this[this.size - 1].y
    var p2x: Float
    var p2y: Float
    for (element in this) {
        p2x = element.x
        p2y = element.y
        if (y >= p1y.coerceAtMost(p2y)) {
            if (y <= p1y.coerceAtLeast(p2y)) {
                if (x <= p1x.coerceAtLeast(p2x)) {
                    if (p1y != p2y) {
                        val xinters = (y - p1y) * (p2x - p1x) / (p2y - p1y) + p1x
                        if (p1x == p2x || x <= xinters) inside = !inside
                    }
                }
            }
        }
        p1x = p2x
        p1y = p2y
    }
    return inside
}
