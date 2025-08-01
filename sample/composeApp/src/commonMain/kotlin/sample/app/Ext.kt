package sample.app

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
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

@OptIn(ExperimentalUuidApi::class)
fun ReferenceItem.tryUpdate(
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

fun Path.moveTo(offset: Offset) {
    this.moveTo(x = offset.x, y = offset.y)
}

fun Path.lineTo(offset: Offset) {
    this.lineTo(x = offset.x, y = offset.y)
}

fun Path.arcTo(
    centerX: Float,
    centerY: Float,
    startAngle: Float,
    sweepAngle: Float,
    radius: Float = Constant.RADIUS,
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