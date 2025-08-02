package sample.app.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import sample.app.Constant

@OptIn(ExperimentalUuidApi::class)
internal data class TableItem(
    val title: String,
    val cell: List<CellItem>,
    val color: Color,
    val position: Offset, // Верхняя левая точка таблицы
    val size: Size,
    val id: Uuid = Uuid.Companion.random(),
) {
    companion object {
        fun initial(
            title: String,
            cell: List<CellItem>,
            color: Color,
            position: Offset,
        ): TableItem {
            return TableItem(
                title = title,
                cell = cell,
                color = color,
                position = position,
                size = Size(
                    width = Constant.CELL_WIDTH,
                    height = Constant.CELL_HEIGHT * (cell.size + 1)
                )
            )
        }
    }
}