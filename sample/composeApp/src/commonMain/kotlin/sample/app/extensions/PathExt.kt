package sample.app.extensions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import sample.app.Constant

internal fun Path.moveTo(offset: Offset) {
    this.moveTo(x = offset.x, y = offset.y)
}

internal fun Path.lineTo(offset: Offset) {
    this.lineTo(x = offset.x, y = offset.y)
}

internal fun Path.arcTo(
    centerX: Float,
    centerY: Float,
    startAngle: Float,
    sweepAngle: Float,
    radius: Float = Constant.LINE_RADIUS,
) {
    arcTo(
        rect = Rect(
            offset = Offset(
                x = centerX,
                y = centerY,
            ),
            size = Size(
                width = radius,
                height = radius
            )
        ),
        startAngleDegrees = startAngle,
        sweepAngleDegrees = sweepAngle,
        forceMoveTo = false,
    )
}
