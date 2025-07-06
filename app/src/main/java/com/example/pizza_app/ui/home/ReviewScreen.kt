package com.example.pizza_app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pizza_app.R
import com.example.pizza_app.data.model.ReviewResponse
import com.example.pizza_app.data.model.ReviewStatsResponse
import com.example.pizza_app.data.source.getFullImageUrl

@Composable
fun ReviewStatsCard(
    reviewStats: ReviewStatsResponse?,
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFFF6B35),
    cardColor: Color = Color.White
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = cardColor
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Đánh giá sản phẩm",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (reviewStats != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left side - Average rating
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = reviewStats.diem_trung_binh.toString(),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            repeat(5) { index ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (index < reviewStats.diem_trung_binh.toInt())
                                        Color(0xFFFFB700) else Color(0xFFE0E0E0),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${reviewStats.tong_so_danh_gia} đánh giá",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    // Right side - Rating distribution
                    Column(
                        modifier = Modifier.weight(2f)
                    ) {
                        for (star in 5 downTo 1) {
                            val count = reviewStats.phan_phoi_diem[star.toString()] ?: 0
                            val percentage = if (reviewStats.tong_so_danh_gia > 0)
                                (count.toFloat() / reviewStats.tong_so_danh_gia) * 100 else 0f

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "$star",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.width(12.dp)
                                )

                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB700),
                                    modifier = Modifier.size(12.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(6.dp)
                                        .background(Color(0xFFF0F0F0), RoundedCornerShape(3.dp))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(percentage / 100f)
                                            .background(primaryColor, RoundedCornerShape(3.dp))
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = count.toString(),
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.width(20.dp)
                                )
                            }

                            if (star > 1) {
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "Chưa có đánh giá",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ReviewItem(
    review: ReviewResponse,
    modifier: Modifier = Modifier,
    cardColor: Color = Color.White
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = cardColor
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                AsyncImage(
                    model = getFullImageUrl(review.hinh_anh_nguoi_danh_gia),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F0F0)),
                    placeholder = painterResource(
                        id = R.drawable.ic_order_empty
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.ten_nguoi_danh_gia ?: "Người dùng ẩn danh",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < review.diem_so)
                                    Color(0xFFFFB700) else Color(0xFFE0E0E0),
                                modifier = Modifier.size(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = review.ngay_danh_gia.formatDateTime(),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Comment
            if (!review.binh_luan.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = review.binh_luan,
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 20.sp
                )
            }

            // Review image
            if (!review.hinh_anh_danh_gia.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                AsyncImage(
                    model = getFullImageUrl(review.hinh_anh_danh_gia),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF0F0F0))
                )
            }
        }
    }
}

@Composable
fun ReviewsSection(
    reviews: List<ReviewResponse>,
    reviewStats: ReviewStatsResponse?,
    isLoading: Boolean,
    hasMoreReviews: Boolean,
    onLoadMoreReviews: () -> Unit,
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFFF6B35),
    cardColor: Color = Color.White
) {
    // State để theo dõi xem có hiển thị tất cả review hay không
    var showAllReviews by remember { mutableStateOf(false) }
    var isLoadingMore by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // Review stats
        ReviewStatsCard(
            reviewStats = reviewStats,
            primaryColor = primaryColor,
            cardColor = cardColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Reviews list
        if (reviews.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                backgroundColor = cardColor
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Đánh giá gần đây",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        // Chỉ hiển thị "Xem tất cả" nếu chưa show all và có nhiều hơn 3 review
                        if (!showAllReviews && reviews.size > 3) {
                            Text(
                                text = "Xem tất cả",
                                fontSize = 14.sp,
                                color = primaryColor,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable {
                                    showAllReviews = true
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Hiển thị reviews
                    val reviewsToShow = if (showAllReviews) reviews else reviews.take(3)

                    reviewsToShow.forEachIndexed { index, review ->
                        ReviewItem(
                            review = review,
                            cardColor = Color(0xFFF8F9FA)
                        )

                        if (index < reviewsToShow.size - 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    // Hiển thị nút "Xem thêm" nếu đang show all và còn có more reviews
                    if (showAllReviews && hasMoreReviews) {
                        Spacer(modifier = Modifier.height(16.dp))

                        if (isLoadingMore) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = primaryColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "Xem thêm đánh giá",
                                fontSize = 14.sp,
                                color = primaryColor,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        isLoadingMore = true
                                        onLoadMoreReviews()
                                        // Reset loading state sau khi load xong
                                        isLoadingMore = false
                                    },
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }

                    // Hiển thị thông báo nếu đang show all nhưng không còn review để load
                    if (showAllReviews && !hasMoreReviews && reviews.size > 3) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Đã hiển thị tất cả đánh giá",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        // Loading indicator ban đầu
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = primaryColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}