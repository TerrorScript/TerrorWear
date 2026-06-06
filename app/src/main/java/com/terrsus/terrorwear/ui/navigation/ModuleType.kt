package com.terrsus.terrorwear.ui.navigation

import androidx.annotation.ColorRes
import com.terrsus.terrorwear.R

enum class ModuleType(@ColorRes val colorRes: Int) {
    NONE(android.R.color.white),
    DEBUG(R.color.moduletype_debug),
    TOOL(R.color.moduletype_tool),
    GAME(R.color.moduletype_game),
    SYSTEM(R.color.moduletype_system)
}