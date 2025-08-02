package sample.app.extensions

import androidx.compose.ui.geometry.Rect
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import sample.app.Constant
import sample.app.data.ReferenceItem
import sample.app.data.TableItem

internal fun ReferenceItem.Target.cellPosition(): Rect {
    return this.table.cellPosition(this.index)
}

internal fun TableItem.cellPosition(index: Int): Rect {
    return Rect(
        left = this.position.x,
        top = this.position.y + (Constant.CELL_HEIGHT * (index + 1)),
        right = this.position.x + this.size.width,
        bottom = this.position.y + (Constant.CELL_HEIGHT * (index + 2)),
    )
}

internal fun TableItem.getRect(): Rect {
    return Rect(
        offset = this.position,
        size = this.size
    )
}

@OptIn(ExperimentalUuidApi::class)
internal fun ReferenceItem.hasTable(tableId: Uuid): Boolean {
    return (this.source.table.id == tableId || this.target.table.id == tableId)
}

@OptIn(ExperimentalUuidApi::class)
internal fun ReferenceItem.tryUpdate(
    table: TableItem,
): ReferenceItem {
    if (!this.hasTable(table.id)) {
        return this
    }
    val newSource = if (table.id == source.table.id) {
        source.copy(table = table)
    } else {
        source
    }
    val newTarget = if (table.id == target.table.id) {
        target.copy(table = table)
    } else {
        target
    }
    return copy(
        source = newSource,
        target = newTarget
    )
}
