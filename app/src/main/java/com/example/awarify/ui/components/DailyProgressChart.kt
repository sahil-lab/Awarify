package com.example.awarify.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.awarify.ui.theme.*
import kotlin.math.max

data class DayProgress(
    val dayName: String,
    val completionPercentage: Float
)

@Composable
fun DailyProgressChart(
    weeklyProgress: List<DayProgress>,
    modifier: Modifier = Modifier
) {
    // Animation for the bars
    val animatedPercentages = weeklyProgress.map { dayProgress ->
        val animatedValue = remember { Animatable(0f) }
        
        LaunchedEffect(dayProgress.completionPercentage) {
            animatedValue.animateTo(
                targetValue = dayProgress.completionPercentage,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        
        animatedValue.value
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Chart Header with Stats
        ChartHeader(weeklyProgress = weeklyProgress)
        
        // Main Chart
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            GradientStart.copy(alpha = 0.03f),
                            Color.Transparent
                        )
                    )
                )
                .padding(16.dp)
        ) {
            drawProgressChart(
                weeklyProgress = weeklyProgress,
                animatedPercentages = animatedPercentages
            )
        }
        
        // Legend
        ChartLegend()
    }
}

@Composable
private fun ChartHeader(weeklyProgress: List<DayProgress>) {
    val averageCompletion = weeklyProgress.map { it.completionPercentage }.average().toFloat()
    val maxCompletion = weeklyProgress.maxOfOrNull { it.completionPercentage } ?: 0f
    val completedDays = weeklyProgress.count { it.completionPercentage >= 100f }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Average Completion
        StatChip(
            modifier = Modifier.weight(1f),
            label = "Average",
            value = "${averageCompletion.toInt()}%",
            color = AccentBlue,
            icon = "📊"
        )
        
        // Best Day
        StatChip(
            modifier = Modifier.weight(1f),
            label = "Best Day",
            value = "${maxCompletion.toInt()}%",
            color = AccentGreen,
            icon = "🏆"
        )
        
        // Perfect Days
        StatChip(
            modifier = Modifier.weight(1f),
            label = "Perfect Days",
            value = completedDays.toString(),
            color = AccentOrange,
            icon = "⭐"
        )
    }
}

@Composable
private fun StatChip(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    color: Color,
    icon: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun ChartLegend() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Progress levels
        LegendItem(
            color = AccentGreen,
            label = "100%+ Complete",
            modifier = Modifier.weight(1f)
        )
        
        LegendItem(
            color = AccentOrange,
            label = "50-99% Complete",
            modifier = Modifier.weight(1f)
        )
        
        LegendItem(
            color = Color.Gray.copy(alpha = 0.4f),
            label = "Below 50%",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = color, shape = CircleShape)
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            maxLines = 1
        )
    }
}

private fun DrawScope.drawProgressChart(
    weeklyProgress: List<DayProgress>,
    animatedPercentages: List<Float>
) {
    if (weeklyProgress.isEmpty()) return
    
    val chartWidth = size.width
    val chartHeight = size.height - 40.dp.toPx() // Leave space for labels
    val barCount = weeklyProgress.size
    val barSpacing = 8.dp.toPx()
    val totalSpacing = barSpacing * (barCount - 1)
    val barWidth = (chartWidth - totalSpacing) / barCount
    
    weeklyProgress.forEachIndexed { index, dayProgress ->
        val animatedPercentage = animatedPercentages.getOrElse(index) { 0f }
        val barHeight = (chartHeight * (animatedPercentage / 100f)).coerceAtLeast(4.dp.toPx())
        
        val startX = index * (barWidth + barSpacing)
        val startY = chartHeight - barHeight
        
        // Determine bar color based on completion percentage
        val barColor = when {
            animatedPercentage >= 100f -> AccentGreen
            animatedPercentage >= 50f -> AccentOrange
            else -> Color.Gray.copy(alpha = 0.4f)
        }
        
        // Create gradient for the bar
        val gradient = Brush.verticalGradient(
            colors = listOf(
                barColor.copy(alpha = 0.9f),
                barColor.copy(alpha = 0.6f)
            ),
            startY = startY,
            endY = chartHeight
        )
        
        // Draw the bar with rounded corners
        drawRoundRect(
            brush = gradient,
            topLeft = Offset(startX, startY),
            size = Size(barWidth, barHeight),
            cornerRadius = CornerRadius(
                x = 6.dp.toPx(),
                y = 6.dp.toPx()
            )
        )
        
        // Draw subtle shadow/highlight effect
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.2f),
                    Color.Transparent
                ),
                startY = startY,
                endY = startY + (barHeight * 0.3f)
            ),
            topLeft = Offset(startX, startY),
            size = Size(barWidth, barHeight * 0.3f),
            cornerRadius = CornerRadius(
                x = 6.dp.toPx(),
                y = 6.dp.toPx()
            )
        )
        
        // Draw percentage text on top of bar if there's space
        if (barHeight > 30.dp.toPx()) {
            drawContext.canvas.nativeCanvas.apply {
                val textPaint = android.graphics.Paint().apply {
                    color = Color.White.toArgb()
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = 10.sp.toPx()
                    isFakeBoldText = true
                }
                
                drawText(
                    "${animatedPercentage.toInt()}%",
                    startX + barWidth / 2,
                    startY + 20.dp.toPx(),
                    textPaint
                )
            }
        }
        
        // Draw day label below the bar
        drawContext.canvas.nativeCanvas.apply {
            val textPaint = android.graphics.Paint().apply {
                color = TextSecondary.toArgb()
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 12.sp.toPx()
                isFakeBoldText = true
            }
            
            drawText(
                dayProgress.dayName,
                startX + barWidth / 2,
                chartHeight + 25.dp.toPx(),
                textPaint
            )
        }
    }
    
    // Draw grid lines for better readability
    val gridColor = Color.Gray.copy(alpha = 0.1f)
    val gridLines = listOf(25f, 50f, 75f, 100f)
    
    gridLines.forEach { percentage ->
        val y = chartHeight - (chartHeight * (percentage / 100f))
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(chartWidth, y),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 10f), 0f)
        )
    }
} 