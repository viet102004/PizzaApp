package com.example.pizza_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.animation.animateColorAsState

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    BottomNavigation(
        modifier = modifier
            .padding(horizontal = 15.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFFCEBD5)),
//            .shadow(
//                elevation = 16.dp,
//                shape = RoundedCornerShape(24.dp),
//                ambientColor = Color.Black.copy(alpha = 0.35f),
//                spotColor = Color.Black.copy(alpha = 0.35f)
//            ),
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = currentDestination == item.route

            // Animate color when selected/unselected
            val animatedTint by animateColorAsState(
                targetValue = if (selected) Color(0xFF1A1A1A) else Color(0x461A1A1A)
            )

            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(28.dp),
                            tint = animatedTint
                        )
                        if (selected) {
                            Text(
                                text = item.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = animatedTint
                            )
                        }
                    }
                },
                selectedContentColor = animatedTint,
                unselectedContentColor = animatedTint,
                alwaysShowLabel = false
            )
        }
    }
}
