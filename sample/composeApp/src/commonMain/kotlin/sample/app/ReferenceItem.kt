package sample.app

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

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
            generatePoints().also { (from, to) ->
                when (from.side) {
                    LinePath.Side.LEFT -> {
                        moveTo(from.position)
                        lineTo(from.x - Constant.LINE_OFFSET, from.y)
                    }

                    LinePath.Side.RIGHT -> {
                        moveTo(from.position)
                        lineTo(from.x + Constant.LINE_OFFSET, from.y)
                    }
                }

                when (to.side) {
                    LinePath.Side.LEFT -> {
                        lineTo(to.x - Constant.LINE_OFFSET, to.y)
                        lineTo(to.position)
                    }

                    LinePath.Side.RIGHT -> {
                        lineTo(to.x + Constant.LINE_OFFSET, to.y)
                        lineTo(to.position)
                    }
                }
            }
        }
    }

    private fun generatePoints(): LinePath {
        val sourcePosition = source.cellPosition()
        val targetPosition = target.cellPosition()
        val pointFrom: Offset
        val pointTo: Offset
        val sideFrom: LinePath.Side
        val sideTo: LinePath.Side
        if (sourcePosition.right < targetPosition.left) {
            pointFrom = sourcePosition.centerRight
            pointTo = targetPosition.centerLeft
            sideFrom = LinePath.Side.RIGHT
            sideTo = LinePath.Side.LEFT
        } else if (sourcePosition.right > targetPosition.left && sourcePosition.right < targetPosition.right) {
            pointFrom = sourcePosition.centerRight
            pointTo = targetPosition.centerRight
            sideFrom = LinePath.Side.RIGHT
            sideTo = LinePath.Side.RIGHT
        } else if (sourcePosition.right > targetPosition.right && sourcePosition.left < targetPosition.right) {
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