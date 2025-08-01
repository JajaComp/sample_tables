package sample.app

import androidx.compose.ui.geometry.Rect
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun ReferenceItem.Target.cellPosition(): Rect {
    return this.table.cellPosition(this.index)
}

fun TableItem.cellPosition(index: Int): Rect {
    return Rect(
        left = this.position.x,
        top = this.position.y + (Constant.CELL_HEIGHT * (index + 1)),
        right = this.position.x + this.size.width,
        bottom = this.position.y + (Constant.CELL_HEIGHT * (index + 2)),
    )
}

fun TableItem.getRect(): Rect {
    return Rect(
        offset = this.position,
        size = this.size
    )
}

@OptIn(ExperimentalUuidApi::class)
fun ReferenceItem.hasTable(tableId: Uuid): Boolean {
    return (this.source.table.id == tableId || this.target.table.id == tableId)
}