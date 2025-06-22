package com.example.pizza_app.ui.cart

import androidx.lifecycle.ViewModel
import com.example.pizza_app.data.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.pizza_app.data.model.Product

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(
        product: Product,
        selectedSize: String,
        selectedCrust: String,
        quantity: Int,
        imageUrl: String
    ) {
        val currentItems = _cartItems.value.toMutableList()

        // Kiểm tra xem sản phẩm với cùng thuộc tính đã có trong giỏ hàng chưa
        val existingItemIndex = currentItems.indexOfFirst {
            it.productId == product.ma_san_pham.toString() &&
                    it.size == selectedSize &&
                    it.thickness == selectedCrust
        }

        if (existingItemIndex != -1) {
            // Nếu đã có, tăng số lượng
            val existingItem = currentItems[existingItemIndex]
            currentItems[existingItemIndex] = existingItem.copy(
                quantity = existingItem.quantity + quantity
            )
        } else {
            // Nếu chưa có, thêm mới
            val newCartItem = CartItem(
                id = "${product.ma_san_pham}_${selectedSize}_${selectedCrust}_${System.currentTimeMillis()}",
                productId = product.ma_san_pham.toString(),
                name = product.ten_san_pham,
                price = calculatePrice(product.gia_co_ban, selectedSize),
                quantity = quantity,
                size = selectedSize,
                thickness = selectedCrust,
                img = imageUrl
            )
            currentItems.add(newCartItem)
        }

        _cartItems.value = currentItems
    }

    fun updateQuantity(itemId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeItem(itemId)
            return
        }

        val currentItems = _cartItems.value.toMutableList()
        val itemIndex = currentItems.indexOfFirst { it.id == itemId }

        if (itemIndex != -1) {
            currentItems[itemIndex] = currentItems[itemIndex].copy(quantity = newQuantity)
            _cartItems.value = currentItems
        }
    }

    fun removeItem(itemId: String) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.removeAll { it.id == itemId }
        _cartItems.value = currentItems
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun getCartItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }

    fun getCartTotal(): Double {
        return _cartItems.value.sumOf { it.price * it.quantity }
    }

    private fun calculatePrice(basePrice: Double, size: String): Double {
        return when (size) {
            "S" -> basePrice * 0.8
            "M" -> basePrice
            "L" -> basePrice * 1.3
            else -> basePrice
        }
    }
}