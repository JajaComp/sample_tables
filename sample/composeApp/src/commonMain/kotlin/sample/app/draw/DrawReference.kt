package sample.app.draw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import sample.app.data.ReferenceItem

internal fun DrawScope.drawReference(
    reference: ReferenceItem,
    dashedPathEffect: PathEffect,
) {
    drawPath(
        reference.path,
        Color.Red,
        style = Stroke(
            width = 3f,
//            pathEffect = dashedPathEffect,
        )
    )
}
