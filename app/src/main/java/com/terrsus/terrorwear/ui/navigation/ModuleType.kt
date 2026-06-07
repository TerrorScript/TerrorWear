package com.terrsus.terrorwear.ui.navigation

import androidx.annotation.ColorRes
import com.terrsus.terrorwear.R

enum class ModuleType(@ColorRes val colorRes: Int) {
    SYSTEM(R.color.moduletype_system),
    TOOL(R.color.moduletype_tool),
    GAME(R.color.moduletype_game),
    DEBUG(R.color.moduletype_debug),
    NONE(android.R.color.white),
}