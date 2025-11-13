package com.abdulmateen.cmpskeleton.core.utils

import androidx.window.core.layout.WindowSizeClass

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {
            // Use the new minimum-dp breakpoints (or your own) instead of deprecated enums
            val widthIsExpanded = windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)
            val widthIsMedium   = windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) && !widthIsExpanded
            val heightIsExpanded = windowSizeClass.isHeightAtLeastBreakpoint(HEIGHT_DP_EXPANDED_LOWER_BOUND)
            val heightIsMedium   = windowSizeClass.isHeightAtLeastBreakpoint(HEIGHT_DP_MEDIUM_LOWER_BOUND) && !heightIsExpanded

            return when {
                !widthIsMedium && !widthIsExpanded &&
                        (heightIsMedium || heightIsExpanded) -> MOBILE_PORTRAIT

                widthIsExpanded && !windowSizeClass.isHeightAtLeastBreakpoint(HEIGHT_DP_MEDIUM_LOWER_BOUND) -> MOBILE_LANDSCAPE

                widthIsMedium && heightIsExpanded -> TABLET_PORTRAIT

                widthIsExpanded && heightIsMedium -> TABLET_LANDSCAPE

                else -> DESKTOP
            }
        }

        private const val WIDTH_DP_MEDIUM_LOWER_BOUND   = 600
        private const val WIDTH_DP_EXPANDED_LOWER_BOUND = 840
        private const val HEIGHT_DP_MEDIUM_LOWER_BOUND  = 480
        private const val HEIGHT_DP_EXPANDED_LOWER_BOUND= 900

        }
}