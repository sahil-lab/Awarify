package com.example.awarify.ui.screens.hobbies

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.awarify.data.entities.Hobby
import com.example.awarify.navigation.NavigationDestinations
import com.example.awarify.ui.components.HobbyStopwatch
import com.example.awarify.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HobbiesScreen(
    navController: NavController,
    viewModel: HobbiesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Gradient animation
    val infiniteTransition = rememberInfiniteTransition(label = "backgroundAnimation")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GradientStart.copy(alpha = 0.08f),
                        GradientMiddle.copy(alpha = 0.04f),
                        Color.White
                    ),
                    startY = animatedOffset * 50
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Premium Header
            HobbiesHeader()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Content
            if (uiState.hobbies.isEmpty()) {
                EmptyHobbiesState()
            } else {
                HobbiesList(
                    hobbies = uiState.hobbies,
                    activeSessions = uiState.activeSessions,
                    onStartSession = viewModel::startHobbySession,
                    onPauseSession = viewModel::pauseHobbySession,
                    onStopSession = viewModel::stopHobbySession,
                    onDeleteHobby = viewModel::deleteHobby,
                    onEditHobby = { hobbyId -> 
                        navController.navigate("edit_hobby/$hobbyId")
                    }
                )
            }
        }
        
        // Floating Action Button
        FloatingActionButton(
            onClick = { navController.navigate(NavigationDestinations.ADD_HOBBY.route) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(64.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = CircleShape,
                    ambientColor = GradientStart.copy(alpha = 0.2f),
                    spotColor = GradientEnd.copy(alpha = 0.2f)
                ),
            containerColor = GradientStart,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 2.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Hobby",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun HobbiesHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = GradientStart.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            GradientStart.copy(alpha = 0.1f),
                            GradientEnd.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Hobbies",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Track and grow your passions",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    GradientStart.copy(alpha = 0.2f),
                                    GradientEnd.copy(alpha = 0.1f)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = GradientStart,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyHobbiesState() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = GradientStart.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                GradientStart.copy(alpha = 0.2f),
                                GradientStart.copy(alpha = 0.1f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = GradientStart,
                    modifier = Modifier.size(40.dp)
                )
            }
            
            Text(
                text = "No hobbies yet",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            
            Text(
                text = "Start your journey by adding your first hobby. Track your progress and build amazing habits!",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun HobbiesList(
    hobbies: List<Hobby>,
    activeSessions: Map<Long, com.example.awarify.data.entities.HobbySession>,
    onStartSession: (Long) -> Unit,
    onPauseSession: (Long) -> Unit,
    onStopSession: (Long) -> Unit,
    onDeleteHobby: (Long) -> Unit,
    onEditHobby: (Long) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        items(hobbies) { hobby ->
            val activeSession = activeSessions[hobby.id]
            
            HobbyCard(
                hobby = hobby,
                activeSession = activeSession,
                onStartSession = { onStartSession(hobby.id) },
                onPauseSession = { onPauseSession(hobby.id) },
                onStopSession = { onStopSession(hobby.id) },
                onDeleteHobby = { onDeleteHobby(hobby.id) },
                onEditHobby = { onEditHobby(hobby.id) }
            )
        }
    }
}

@Composable
private fun HobbyCard(
    hobby: Hobby,
    activeSession: com.example.awarify.data.entities.HobbySession?,
    onStartSession: () -> Unit,
    onPauseSession: () -> Unit,
    onStopSession: () -> Unit,
    onDeleteHobby: () -> Unit,
    onEditHobby: () -> Unit
) {
    val hobbyColor = Color(android.graphics.Color.parseColor(hobby.colorHex))
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = hobbyColor.copy(alpha = 0.1f),
                spotColor = hobbyColor.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            hobbyColor.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            hobbyColor.copy(alpha = 0.8f),
                                            hobbyColor.copy(alpha = 0.6f)
                                        )
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = hobby.iconName.firstOrNull()?.toString() ?: "🎯",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                        }
                        
                        Column {
                            Text(
                                text = hobby.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = hobby.description ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                maxLines = 2
                            )
                        }
                    }
                }
                
                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onEditHobby,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = AccentBlue.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Hobby",
                            tint = AccentBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    IconButton(
                        onClick = onDeleteHobby,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = AccentRed.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Hobby",
                            tint = AccentRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            // Goals Section
            if (!hobby.goalDescription.isNullOrEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = hobbyColor.copy(alpha = 0.05f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Goal",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = hobbyColor
                        )
                        Text(
                            text = hobby.goalDescription ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                        
                        if ((hobby.dailyGoalMinutes ?: 0) > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                GoalChip(
                                    label = "Daily",
                                    value = "${hobby.dailyGoalMinutes ?: 0} min",
                                    color = hobbyColor
                                )
                                if ((hobby.weeklyGoalMinutes ?: 0) > 0) {
                                    GoalChip(
                                        label = "Weekly",
                                        value = "${hobby.weeklyGoalMinutes ?: 0} min",
                                        color = hobbyColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Stopwatch Section
            HobbyStopwatch(
                isActive = activeSession?.isActive == true,
                elapsedTime = activeSession?.let {
                    if (it.isActive) {
                        (System.currentTimeMillis() - it.startTime.time) / 1000
                    } else {
                        it.durationMinutes * 60L
                    }
                } ?: 0L,
                onStart = onStartSession,
                onPause = onPauseSession,
                onStop = onStopSession,
                hobbyColor = hobbyColor
            )
        }
    }
}

@Composable
private fun GoalChip(
    label: String,
    value: String,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
} 