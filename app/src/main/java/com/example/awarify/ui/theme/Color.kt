package com.example.awarify.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Primary Brand Colors - Modern Purple/Blue Gradient Theme
val Purple80 = Color(0xFF6C63FF)
val PurpleGrey80 = Color(0xFF8B7CF6)
val Pink80 = Color(0xFFEC4899)

val Purple40 = Color(0xFF4F46E5)
val PurpleGrey40 = Color(0xFF7C3AED)
val Pink40 = Color(0xFFDB2777)

// Modern Gradient Colors
val GradientStart = Color(0xFF6366F1) // Indigo
val GradientMiddle = Color(0xFF8B5CF6) // Violet  
val GradientEnd = Color(0xFFA855F7) // Purple

// Surface Colors
val SurfaceLight = Color(0xFFFFFBFE)
val SurfaceDark = Color(0xFF1C1B1F)
val SurfaceVariantLight = Color(0xFFF7F2FA)
val SurfaceVariantDark = Color(0xFF49454F)

// Accent Colors
val AccentBlue = Color(0xFF3B82F6)
val AccentGreen = Color(0xFF10B981)
val AccentOrange = Color(0xFFF59E0B)
val AccentRed = Color(0xFFEF4444)

// Card Colors
val CardLight = Color(0xFFFFFFFF)
val CardDark = Color(0xFF2D2D2D)

// Text Colors - Enhanced for better visibility
val TextPrimary = Color(0xFF1F2937)
val TextSecondary = Color(0xFF6B7280)
val TextLight = Color(0xFFFFFFFF)

// Background Gradients
val BackgroundGradientLight = listOf(
    Color(0xFFF8FAFC),
    Color(0xFFF1F5F9),
    Color(0xFFE2E8F0)
)

val BackgroundGradientDark = listOf(
    Color(0xFF0F172A),
    Color(0xFF1E293B),
    Color(0xFF334155)
)

// Hobby Category Colors
val HobbyColors = listOf(
    Color(0xFF6366F1), // Indigo
    Color(0xFF8B5CF6), // Violet
    Color(0xFFEC4899), // Pink
    Color(0xFF10B981), // Emerald
    Color(0xFFF59E0B), // Amber
    Color(0xFFEF4444), // Red
    Color(0xFF3B82F6), // Blue
    Color(0xFF06B6D4), // Cyan
)

// Utility function for consistent text field colors
@Composable
fun hobbyTrackerTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = GradientStart,
        focusedLabelColor = GradientStart,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedTextColor = Color(0xFF1C1B1F), // Force dark text
        unfocusedTextColor = Color(0xFF1C1B1F), // Force dark text
        focusedPlaceholderColor = Color(0xFF49454F),
        unfocusedPlaceholderColor = Color(0xFF49454F),
        cursorColor = GradientStart,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedLeadingIconColor = GradientStart,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedTrailingIconColor = GradientStart,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}