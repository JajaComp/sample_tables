package sample.app

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TableItem(
    val title: String,
    val cell: List<CellItem>,
    val color: Color,
    val position: Offset, // Верхняя левая точка таблицы
    val size: Size,
    val id: Uuid = Uuid.random(),
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
                    width = 200f,
                    height = Constant.CELL_HEIGHT * (cell.size + 1)
                )
            )
        }
    }
}
