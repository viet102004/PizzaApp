package com.example.pizza_app.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pizza_app.R
import com.example.pizza_app.data.model.BannerItem
import kotlinx.coroutines.delay
import kotlin.math.abs


@Composable
fun PromoBanner(
    bannerItems: List<BannerItem> = getDefaultBannerItems(),
    autoScrollDuration: Long = 4000L
) {
    if (bannerItems.isEmpty()) return

    var currentIndex by remember { mutableStateOf(0) }
    var lastInteractionTime by remember { mutableStateOf(System.currentTimeMillis()) }

    val density = LocalDensity.current
    val screenWidth = with(density) { 360.dp.toPx() } // Approximate screen width

    // Animated offset for smooth transitions
    val animatedOffset by animateFloatAsState(
        targetValue = -currentIndex * screenWidth,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "banner_offset"
    )

    // Auto scroll effect with reset timer after user interaction
    LaunchedEffect(currentIndex, lastInteractionTime) {
        delay(autoScrollDuration)
        if (System.currentTimeMillis() - lastInteractionTime >= autoScrollDuration) {
            currentIndex = (currentIndex + 1) % bannerItems.size
        }
    }

    // Functions to handle navigation
    val goToPrevious = {
        lastInteractionTime = System.currentTimeMillis()
        currentIndex = if (currentIndex == 0) {
            bannerItems.size - 1
        } else {
            currentIndex - 1
        }
    }

    val goToNext = {
        lastInteractionTime = System.currentTimeMillis()
        currentIndex = (currentIndex + 1) % bannerItems.size
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Render all images with offset
            bannerItems.forEachIndexed { index, item ->
                val imageOffset = animatedOffset + (index * screenWidth)
                val isVisible = abs(imageOffset) < screenWidth * 2 // Only render visible items for performance

                if (isVisible) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                translationX = imageOffset
                                val distance = abs(imageOffset) / screenWidth
                                alpha = (1f - distance * 0.3f).coerceIn(0.3f, 1f)
                                scaleX = (1f - distance * 0.1f).coerceIn(0.8f, 1f)
                                scaleY = (1f - distance * 0.1f).coerceIn(0.8f, 1f)
                            }
                    ) {
                        // Background color
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(item.backgroundColor)
                        )

                        // Image
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = "Pizza Banner ${index + 1}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Overlay gradient for better indicator visibility
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    androidx.compose.ui.graphics.Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.2f)
                                        )
                                    )
                                )
                        )
                    }
                }
            }

            // Left navigation button
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { goToPrevious() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Previous",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Right navigation button
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { goToNext() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Indicator dots with animation
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(bannerItems.size) { index ->
                    val isSelected = index == currentIndex
                    val animatedSize by animateDpAsState(
                        targetValue = if (isSelected) 12.dp else 8.dp,
                        animationSpec = tween(300),
                        label = "indicator_size"
                    )
                    val animatedAlpha by animateFloatAsState(
                        targetValue = if (isSelected) 1f else 0.5f,
                        animationSpec = tween(300),
                        label = "indicator_alpha"
                    )

                    Box(
                        modifier = Modifier
                            .size(animatedSize)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = animatedAlpha))
                    )
                }
            }
        }
    }
}

// Default banner items với placeholder images
private fun getDefaultBannerItems(): List<BannerItem> {
    return listOf(
        BannerItem(
            imageRes = R.drawable.pizza2,
            backgroundColor = Color(0xFFFF6B6B)
        ),
        BannerItem(
            imageRes = R.drawable.pizza3,
            backgroundColor = Color(0xFF4ECDC4)
        ),
        BannerItem(
            imageRes = R.drawable.pizza4,
            backgroundColor = Color(0xFFFFE66D)
        )
    )
}

// Alternative usage with custom data
@Composable
fun PromoBannerWithData(
    pizzaImages: List<Int>, // List of drawable resource IDs
    backgroundColors: List<Color> = listOf(
        Color(0xFFFF6B6B),
        Color(0xFF4ECDC4),
        Color(0xFFFFE66D)
    ),
    autoScrollDuration: Long = 5000L
) {
    val bannerItems = pizzaImages.mapIndexed { index, imageRes ->
        BannerItem(
            imageRes = imageRes,
            backgroundColor = backgroundColors.getOrNull(index) ?: Color(0xFFB5FC00)
        )
    }

    PromoBanner(
        bannerItems = bannerItems,
        autoScrollDuration = autoScrollDuration
    )
}