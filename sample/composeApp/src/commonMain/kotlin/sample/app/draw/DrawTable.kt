package sample.app.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import sample.app.Constant
import sample.app.data.TableItem

internal fun DrawScope.drawTable(
    table: TableItem,
    textMeasurer: TextMeasurer,
) {
    val tableStrokeWidthPx = 2.dp.toPx()

    translate(left = table.position.x, top = table.position.y) {
        drawRect(
            color = table.color,
            topLeft = Offset.Zero,
            size = table.size,
            style = Stroke(tableStrokeWidthPx)
        )
        drawTitle(
            table,
            textMeasurer
        )
        // Рисуем горизонтальные линии (разделители строк)
        for (i in 0 until table.cell.size) {
            drawCell(
                table = table,
                index = i,
                textMeasurer = textMeasurer,
            )
        }
    }
}

private fun DrawScope.drawTitle(
    table: TableItem,
    textMeasurer: TextMeasurer,
) {
    val tableStrokeWidthPx = 2.dp.toPx()
    drawText(
        textMeasurer = textMeasurer,
        text = table.title,
        topLeft = Offset(
            x = Constant.TABLE_TEXT_OFFSET,
            y = Constant.CELL_HEIGHT - Constant.TABLE_TEXT_HEIGHT
        ),
        style = Constant.boldText
    )
    drawLine(
        color = table.color,
        start = Offset.Zero,
        end = Offset(table.size.width, 0f),
        strokeWidth = tableStrokeWidthPx / 2
    )
}

private fun DrawScope.drawCell(
    table: TableItem,
    index: Int,
    textMeasurer: TextMeasurer,
) {
    val tableStrokeWidthPx = 2.dp.toPx()
    val textY = Constant.CELL_HEIGHT + (index + 1) * Constant.CELL_HEIGHT
    val lineY = (index + 1) * Constant.CELL_HEIGHT
    drawText(
        textMeasurer = textMeasurer,
        text = table.cell[index].text,
        topLeft = Offset(
            x = Constant.TABLE_TEXT_OFFSET,
            y = textY - Constant.TABLE_TEXT_HEIGHT,
        ),
    )
    drawLine(
        color = table.color,
        start = Offset(
            0f,
            lineY
        ),
        end = Offset(
            table.size.width,
            lineY
        ),
        strokeWidth = tableStrokeWidthPx / 2
    )
}