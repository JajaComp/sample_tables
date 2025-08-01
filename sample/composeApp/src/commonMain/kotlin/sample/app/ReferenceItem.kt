package sample.app

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlin.math.abs

data class ReferenceItem(
    val source: Target,
    val target: Target,
    val color: Color = Color.Red,
    val version: Int = 0,
) {
    data class Target(
        val table: TableItem,
        val index: Int,
    )

    val path: Path = generatePath()

    private fun generatePath(): Path {
        return Path().apply {
            generatePoints().also { (point1, point2) ->
                val radius = Constant.RADIUS.coerceAtMost(abs(point1.y - point2.y))
                val halfRadius = radius / 2f
                val from: LinePath.Position
                val to: LinePath.Position
                if (point2.x < point1.x) {
                    from = point2
                    to = point1
                } else {
                    from = point1
                    to = point2
                }
                val fromTopToBottom = from.y < to.y
                // Стартовая линия
                moveTo(from.position)
                // Маленькая линия от объекта
                when (from.side) {
                    LinePath.Side.LEFT -> {
                        lineTo(from.x - Constant.LINE_OFFSET, from.y)
                    }

                    LinePath.Side.RIGHT -> {
                        lineTo(from.x + Constant.LINE_OFFSET, from.y)
                    }
                }

                if (from.side != to.side) {
                    lineTo(
                        x = from.x + (abs(to.x - from.x) / 2f),
                        y = from.y
                    )
                    arcTo(
                        centerX = from.x + (abs(to.x - from.x) / 2f),
                        centerY = from.y - if (fromTopToBottom) 0f else radius,
                        startAngle = if (fromTopToBottom) 270f else 90f,
                        sweepAngle = if (fromTopToBottom) 90f else -90f,
                        radius = radius,
                    )
                    lineTo(
                        x = from.x + (abs(to.x - from.x) / 2f) + radius,
                        y = to.y - if (fromTopToBottom) radius else -radius
                    )
                    arcTo(
                        centerX = from.x + (abs(to.x - from.x) / 2f) + radius,
                        centerY = to.y - if (fromTopToBottom) radius else 0f,
                        startAngle = if (fromTopToBottom) 180f else 180f,
                        sweepAngle = if (fromTopToBottom) -90f else 90f,
                        radius = radius,
                    )
                } else {
                    arcTo(
                        centerX = to.x + Constant.LINE_OFFSET - halfRadius,
                        centerY = from.y - if (fromTopToBottom) 0f else radius,
                        startAngle = if (fromTopToBottom) 270f else 90f,
                        sweepAngle = if (fromTopToBottom) 90f else -90f,
                        radius = radius,
                    )
                }

                // Финишная линия
                when (to.side) {
                    LinePath.Side.LEFT -> {
                        lineTo(to.x - Constant.LINE_OFFSET, to.y)
                        lineTo(to.position)
                    }

                    LinePath.Side.RIGHT -> {
                        lineTo(
                            to.x + Constant.LINE_OFFSET + halfRadius,
                            to.y - if (fromTopToBottom) halfRadius else -halfRadius
                        )
                        arcTo(
                            centerX = to.x + Constant.LINE_OFFSET - halfRadius,
                            centerY = to.y - if (fromTopToBottom) radius else 0f,
                            startAngle = if (fromTopToBottom) 0f else 0f,
                            sweepAngle = if (fromTopToBottom) 90f else -90f,
                            radius = radius,
                        )
                        lineTo(to.position)
                    }
                }
            }
        }
    }

    private fun generatePoints(): LinePath {
        if (source.table == target.table) {
            return LinePath(
                from = LinePath.Position(
                    source.cellPosition().centerRight,
                    side = LinePath.Side.RIGHT
                ),
                to = LinePath.Position(
                    target.cellPosition().centerRight,
                    side = LinePath.Side.RIGHT
                ),
            )
        }
        val sourcePosition = source.cellPosition()
        val targetPosition = target.cellPosition()
        val pointFrom: Offset
        val pointTo: Offset
        val sideFrom: LinePath.Side
        val sideTo: LinePath.Side
        val sourceLeft = sourcePosition.left - Constant.LINE_OFFSET
        val sourceRight = sourcePosition.right + Constant.LINE_OFFSET
        val targetLeft = targetPosition.left - Constant.LINE_OFFSET
        val targetRight = targetPosition.right + Constant.LINE_OFFSET
        if (sourceRight < targetLeft) {
            pointFrom = sourcePosition.centerRight
            pointTo = targetPosition.centerLeft
            sideFrom = LinePath.Side.RIGHT
            sideTo = LinePath.Side.LEFT
        } else if (sourceRight > targetLeft && sourceRight < targetRight) {
            pointFrom = sourcePosition.centerRight
            pointTo = targetPosition.centerRight
            sideFrom = LinePath.Side.RIGHT
            sideTo = LinePath.Side.RIGHT
        } else if (sourceRight > targetRight && sourceLeft < targetRight) {
            pointFrom = sourcePosition.centerRight
            pointTo = targetPosition.centerRight
            sideFrom = LinePath.Side.RIGHT
            sideTo = LinePath.Side.RIGHT
        } else {
            pointFrom = sourcePosition.centerLeft
            pointTo = targetPosition.centerRight
            sideFrom = LinePath.Side.LEFT
            sideTo = LinePath.Side.RIGHT
        }
        return LinePath(
            LinePath.Position(
                position = pointFrom,
                side = sideFrom
            ),
            LinePath.Position(
                position = pointTo,
                side = sideTo
            )
        )
    }

    data class LinePath(
        val from: Position,
        val to: Position,
    ) {
        data class Position(
            val position: Offset,
            val side: Side,
        ) {
            val x: Float
                get() = position.x
            val y: Float
                get() = position.y
        }

        enum class Side {
            LEFT,
            RIGHT,
        }
    }
}