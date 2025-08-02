package sample.app.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import sample.app.generator.LinePathGenerator.generatePath

internal data class ReferenceItem(
    val source: Target,
    val target: Target,
    val color: Color = Color.Companion.Red,
) {
    data class Target(
        val table: TableItem,
        val index: Int,
    )

    val path: Path = generatePath()
}