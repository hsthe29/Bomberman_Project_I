package ui

import com.soywiz.korim.font.*
import com.soywiz.korio.file.std.*

object TextFont {
    lateinit var pacifico: TtfFont
    lateinit var aller_rg: TtfFont
    lateinit var clear_sans_bold: TtfFont
    lateinit var blomberg: TtfFont
    lateinit var plaguard: TtfFont

    suspend fun load() {
        pacifico = resourcesVfs["fonts/Pacifico.ttf"].readTtfFont()
        aller_rg = resourcesVfs["fonts/Aller_rg.ttf"].readTtfFont()
        clear_sans_bold = resourcesVfs["fonts/ClearSans-Bold.ttf"].readTtfFont()
        blomberg = resourcesVfs["fonts/Blomberg.ttf"].readTtfFont()
        plaguard = resourcesVfs["fonts/Plaguard.ttf"].readTtfFont()
    }
}
