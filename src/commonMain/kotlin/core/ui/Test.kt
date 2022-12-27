package core.ui

import com.soywiz.klogger.*
import com.soywiz.korev.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import com.soywiz.korma.geom.shape.*
import com.soywiz.korma.geom.vector.*
import core.level.*

interface Demo {
    var a: Int
    class MK: Demo {
        override var a: Int = 5
    }
}

class Test: Scene() {
    override suspend fun SContainer.sceneInit() {
//        world {
//            val cc = circle(radius = 20.0) {
//                color = Colors.BLUE
//            }
//
//            val bl = block(resourcesVfs["icons/sound-icon.png"].readBitmap()) {
//                anchor(1.0, 1.0)
//                scale(0.5)
//                radius = 100.0
//                position(700, 400)
//
////                Console.log(width, scale, center.x)
//            }
//
//            val tt = block(resourcesVfs["items/huutomerkki.png"].readBitmap()) {
//                position(700.0, 400.0)
//                addUpdater {
//                    upperLeft.x = x
//                    upperLeft.y = y
//                    bottomRight.x = x + width*scale
//                    bottomRight.y = y + height*scale
//                }
//            }
//            bl.onClick {
//                Console.log(bl.radius, bl.center)
//                Console.log(tt.upperLeft, tt.bottomRight)
//            }
//            bl.addUpdater {
//                cc.color = Colors.BLUE
//                center.x = x - width * scale / 2
//                center.y = y - height * scale / 2
//
//                if(collidesWith(tt)) {
//                    cc.color = Colors.VIOLET
//                }
//
//                if (input.keys[Key.LEFT]) x--
//                if (input.keys.pressing(Key.RIGHT)) x++
//                if (input.keys[Key.UP]) y--
//                if (input.keys.pressing(Key.DOWN)) y++
//            }
//
//        }

    }
}
