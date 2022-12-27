package core.tiles

import core.level.*

data class TileInfo(val tileUrl: String,
                    val tileType: TileType = TileType.DEFAULT,
                    val x: Double, val y: Double,
                    val off_x: Double = 0.0,
                    val off_y: Double = 0.0,
                    val a_x: Double,
                    val a_y: Double)


