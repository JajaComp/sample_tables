package sample.app

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

internal object Constant {
    const val CELL_WIDTH = 100f
    const val CELL_HEIGHT = 30f
    const val LINE_OFFSET = 10f
    const val LINE_RADIUS = 10f
    const val TABLE_TEXT_OFFSET = 2f
    const val TABLE_TEXT_HEIGHT = 20f

    val boldText = TextStyle.Default.copy(
        fontWeight = FontWeight.Bold,
    )
}