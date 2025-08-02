package sample.app

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.rememberTextMeasurer
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import sample.app.data.CellItem
import sample.app.data.ReferenceItem
import sample.app.data.TableItem
import sample.app.draw.drawReference
import sample.app.draw.drawTable
import sample.app.extensions.getRect
import sample.app.extensions.tryUpdate


@OptIn(ExperimentalUuidApi::class, ExperimentalComposeUiApi::class)
@Composable
fun App() {
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
    val references = listOf(
//        ReferenceItem(
//            source = ReferenceItem.Target(
//                table = tables[0],
//                index = 1,
//            ),
//            target = ReferenceItem.Target(
//                table = tables[0],
//                index = 2
//            )
//        ),
        ReferenceItem(
            source = ReferenceItem.Target(
                table = tables[0],
                index = 1,
            ),
            target = ReferenceItem.Target(
                table = tables[0],
                index = 2
            )
        ),
        ReferenceItem(
            source = ReferenceItem.Target(
                table = tables[0],
                index = 0,
            ),
            target = ReferenceItem.Target(
                table = tables[1],
                index = 1
            )
        ),
        ReferenceItem(
            source = ReferenceItem.Target(
                table = tables[1],
                index = 2,
            ),
            target = ReferenceItem.Target(
                table = tables[2],
                index = 2
            )
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {

        Place(tables, references)
    }
}

@OptIn(ExperimentalUuidApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun Place(
    initTables: List<TableItem>,
    initReferences: List<ReferenceItem>,
) {
    val tables = remember {
        mutableStateMapOf<Uuid, TableItem>().apply {
            putAll(
                initTables
                    .associateBy(TableItem::id)
                    .toMutableMap()
            )
        }
    }
    var references by remember {
        mutableStateOf(initReferences)
    }
    val textMeasurer = rememberTextMeasurer()
    var selectedTableId by remember { mutableStateOf<Uuid?>(null) }
    var dragStartOffsetInTable by remember { mutableStateOf<Offset?>(null) }
    var canvasOffset by remember { mutableStateOf(Offset.Zero) }
    var scale by remember { mutableStateOf(1f) }

    val dashIntervals = floatArrayOf(10f, 5f)
    val totalDashLength = dashIntervals.sum()

    val phase = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        phase.animateTo(
            targetValue = totalDashLength, // Анимируем на полную длину одного цикла черта+пробел
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart // Начинаем заново после каждого цикла
            )
        )
    }
    val dashedPathEffect = PathEffect.dashPathEffect(dashIntervals, phase.value)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { startOffset ->
                        val adjustedStartOffset = (startOffset - canvasOffset) / scale
                        val tableClicked = tables.values.firstOrNull { table ->
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
                            selectedTableId?.also { selectedTableId ->
                                val table = tables.getValue(selectedTableId)
                                val newPosition = (table.position + dragAmount / scale)
//                                val newPosition = change.position - dragStartOffsetInTable!! - canvasOffset
                                tables[selectedTableId] = table.copy(position = newPosition)
                                references = references.map { reference ->
                                    reference.tryUpdate(table)
                                }
                            }
                        } else {
                            canvasOffset += dragAmount
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
                scale = (scale + delta / 5f).coerceIn(0.4f, 4f)
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
                references.forEach { references ->
                    drawReference(
                        references,
                        dashedPathEffect
                    )
                }
                tables.values.forEach { table ->
                    drawTable(
                        table,
                        textMeasurer,
                    )
                }
            }
        }
    }
}
