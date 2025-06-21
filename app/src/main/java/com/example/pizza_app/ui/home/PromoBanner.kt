package com.example.pizza_app.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pizza_app.data.model.Banner
import com.example.pizza_app.data.source.getFullImageUrl
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun PromoBanner(
    banners: List<Banner>,
    autoScrollDuration: Long = 4000L
) {
    if (banners.isEmpty()) return

    var currentIndex by remember { mutableStateOf(0) }
    var lastInteractionTime by remember { mutableStateOf(System.currentTimeMillis()) }

    val density = LocalDensity.current
    val screenWidth = with(density) { 360.dp.toPx() }

    val animatedOffset by animateFloatAsState(
        targetValue = -currentIndex * screenWidth,
        animationSpec = tween(500),
        label = "banner_offset"
    )

    LaunchedEffect(currentIndex, lastInteractionTime) {
        delay(autoScrollDuration)
        if (System.currentTimeMillis() - lastInteractionTime >= autoScrollDuration) {
            currentIndex = (currentIndex + 1) % banners.size
        }
    }

    val goToPrevious = {
        lastInteractionTime = System.currentTimeMillis()
        currentIndex = if (currentIndex == 0) banners.size - 1 else currentIndex - 1
    }

    val goToNext = {
        lastInteractionTime = System.currentTimeMillis()
        currentIndex = (currentIndex + 1) % banners.size
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            banners.forEachIndexed { index, banner ->
                val offsetX = animatedOffset + (index * screenWidth)
                val isVisible = abs(offsetX) < screenWidth * 2

                if (isVisible) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                translationX = offsetX
                                val distance = abs(offsetX) / screenWidth
                                alpha = (1f - distance * 0.3f).coerceIn(0.3f, 1f)
                                scaleX = (1f - distance * 0.1f).coerceIn(0.8f, 1f)
                                scaleY = (1f - distance * 0.1f).coerceIn(0.8f, 1f)
                            }
                    ) {
                        AsyncImage(
                            model = getFullImageUrl(banner.url_hinh_anh),
                            contentDescription = banner.tieu_de,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    androidx.compose.ui.graphics.Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.2f))
                                    )
                                )
                        )
                    }
                }
            }

            // Navigation arrows
            IconButton(goToPrevious, Icons.Default.KeyboardArrowLeft, Modifier.align(Alignment.CenterStart))
            IconButton(goToNext, Icons.Default.KeyboardArrowRight, Modifier.align(Alignment.CenterEnd))

            // Indicators
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(banners.size) { index ->
                    val isSelected = index == currentIndex
                    val size by animateDpAsState(if (isSelected) 12.dp else 8.dp, label = "")
                    val alpha by animateFloatAsState(if (isSelected) 1f else 0.5f, label = "")
                    Box(
                        modifier = Modifier
                            .size(size)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = alpha))
                    )
                }
            }
        }
    }
}

@Composable
private fun IconButton(onClick: () -> Unit, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White)
    }
}
