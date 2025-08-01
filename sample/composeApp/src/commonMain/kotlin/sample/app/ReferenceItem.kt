package sample.app

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

data class ReferenceItem(
    val source: Target,
    val target: Target,
    val color: Color = Color.Red,
    val version: Int = 0
) {
    data class Target(
        val table: TableItem,
        val index: Int,
    )

    val path: Path = generatePath()

    private fun generatePath(): Path {
        return Path().apply {
            val sourcePosition = source.cellPosition()
            val targetPosition = target.cellPosition()
            moveTo(sourcePosition.left, sourcePosition.top)
            lineTo(targetPosition.left, targetPosition.top)
        }
    }
}