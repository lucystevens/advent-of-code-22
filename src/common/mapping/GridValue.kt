package common.mapping

import common.Point

data class GridValue<T>(
    val point: Point,
    val value: T
)
