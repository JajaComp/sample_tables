package sample.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private val boldText = TextStyle.Default.copy(
    fontWeight = FontWeight.Bold,
)

@OptIn(ExperimentalUuidApi::class, ExperimentalComposeUiApi::class)
@Composable
fun App() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val tables = List(3) { index ->
            TableItem.initial(
                "Title ${index + 1}",
                listOf(
                    CellItem("Line 1"),
                    CellItem("Line 2"),
                    CellItem("Line 3"),
                ),
                Color.Black,
                Offset(0 + 150f * index, 0f),
            )
        }
        Place(tables)
    }
}

@OptIn(ExperimentalUuidApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Place(
    initTables: List<TableItem>,
) {
    var tables by remember {
        mutableStateOf(initTables)
    }
    val textMeasurer = rememberTextMeasurer()
    var selectedTableId by remember { mutableStateOf<Uuid?>(null) }
    var dragStartOffsetInTable by remember { mutableStateOf<Offset?>(null) }
    var canvasOffset by remember { mutableStateOf(Offset.Zero) }
    var scale by remember { mutableStateOf(1f) }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { startOffset ->
                        val adjustedStartOffset = (startOffset - canvasOffset) / scale
                        val tableClicked = tables.firstOrNull { table ->
                            val tableRect = table.getRect()
                            tableRect.contains(adjustedStartOffset)
                        }

                        if (tableClicked != null) {
                            selectedTableId = tableClicked.id
                            dragStartOffsetInTable = adjustedStartOffset - tableClicked.position
                        } else {
                            selectedTableId = null
                            dragStartOffsetInTable = null
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        if (selectedTableId != null && dragStartOffsetInTable != null) {
                            tables = tables.map { table ->
                                if (table.id == selectedTableId) {
//                                    val newPosition = change.position - dragStartOffsetInTable!! - canvasOffset
                                    val newPosition = (table.position + dragAmount / scale)
                                    table.copy(position = newPosition)
                                } else {
                                    table
                                }
                            }
                        } else {
                            canvasOffset += (dragAmount)
                        }
                    },
                    onDragEnd = {
                        selectedTableId = null
                        dragStartOffsetInTable = null
                    }
                )
            }
            .onPointerEvent(PointerEventType.Scroll) {
                val delta = it.changes.first().scrollDelta.y
                scale = (scale + delta / 5f).coerceAtLeast(0.4f).coerceAtMost(4f)
            }
    ) {
        translate(
            left = canvasOffset.x,
            top = canvasOffset.y,
        ) {
            scale(
                scale = scale,
                pivot = Offset.Zero
            ) {
                tables.forEach { table ->
                    drawTable(
                        table,
                        textMeasurer
                    )
                }
            }
        }
    }
}

fun DrawScope.drawTable(
    table: TableItem,
    textMeasurer: TextMeasurer,
) {
    val tableStrokeWidthPx = 2.dp.toPx()


    translate(left = table.position.x, top = table.position.y) {
        // Рисуем внешнюю рамку таблицы
        drawRect(
            color = table.color,
            topLeft = Offset.Zero,
            size = table.size,
            style = androidx.compose.ui.graphics.drawscope.Stroke(tableStrokeWidthPx)
        )
        drawTitle(
            table,
            textMeasurer
        )
        // Рисуем горизонтальные линии (разделители строк)
        for (i in 0 until table.cell.size) {
            val y = (i + 1) * Constant.CELL_HEIGHT
            drawText(
                textMeasurer = textMeasurer,
                text = table.cell[i].text,
                topLeft = Offset(
                    x = 0f,
                    y = y,
                ),
            )
            drawLine(
                color = table.color,
                start = Offset(0f, y),
                end = Offset(table.size.width, y),
                strokeWidth = tableStrokeWidthPx / 2
            )
        }
    }
}

fun DrawScope.drawTitle(
    table: TableItem,
    textMeasurer: TextMeasurer,
) {
    val tableStrokeWidthPx = 2.dp.toPx()
    drawText(
        textMeasurer = textMeasurer,
        text = table.title,
        topLeft = Offset.Zero,
        style = boldText
    )
    drawLine(
        color = table.color,
        start = Offset.Zero,
        end = Offset(table.size.width, 0f),
        strokeWidth = tableStrokeWidthPx / 2
    )
}

fun TableItem.getRect(): androidx.compose.ui.geometry.Rect {
    return androidx.compose.ui.geometry.Rect(
        offset = this.position,
        size = this.size
    )
}