// CartViewModel.kt
package com.example.pizza_app.ui.cart

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.pizza_app.data.model.*

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()

    private val _itemCount = MutableStateFlow(0)
    val itemCount: StateFlow<Int> = _itemCount.asStateFlow()

    fun addToCart(
        product: Product,
        selectedOptions: Map<Int, Int>, // ma_loai_tuy_chon -> ma_gia_tri
        quantity: Int,
        imageUrl: String,
        options: List<ProductOption> // Để lấy thông tin chi tiết về tùy chọn
    ) {
        // Tạo CartOption từ selectedOptions và options
        val cartOptions = selectedOptions.mapNotNull { (maLoaiTuyChon, maGiaTri) ->
            val option = options.find { it.ma_loai_tuy_chon == maLoaiTuyChon }
            val giaTriOption = option?.gia_tri?.find { it.ma_gia_tri == maGiaTri }

            if (option != null && giaTriOption != null) {
                maLoaiTuyChon to CartOption(
                    maLoaiTuyChon = maLoaiTuyChon,
                    tenLoai = option.ten_loai,
                    maGiaTri = maGiaTri,
                    tenGiaTri = giaTriOption.ten_gia_tri,
                    giaThem = giaTriOption.gia_them
                )
            } else null
        }.toMap()

        // Tạo unique ID cho cart item
        val itemId = generateCartItemId(product.ma_san_pham.toInt(), selectedOptions)

        // Tính tổng giá
        val basePrice = product.gia_co_ban
        val optionsPrice = cartOptions.values.sumOf { it.giaThem }
        val totalPrice = (basePrice + optionsPrice) * quantity

        val newItem = CartItem(
            id = itemId,
            product = product,
            selectedOptions = cartOptions,
            quantity = quantity,
            imageUrl = imageUrl,
            totalPrice = totalPrice
        )

        val currentItems = _cartItems.value.toMutableList()

        // Kiểm tra xem item đã tồn tại chưa (cùng sản phẩm và cùng tùy chọn)
        val existingItemIndex = currentItems.indexOfFirst { it.id == itemId }

        if (existingItemIndex != -1) {
            // Nếu đã tồn tại, cập nhật số lượng
            val existingItem = currentItems[existingItemIndex]
            val updatedItem = existingItem.copy(
                quantity = existingItem.quantity + quantity,
                totalPrice = existingItem.calculateTotalPrice() + totalPrice
            )
            currentItems[existingItemIndex] = updatedItem
        } else {
            // Nếu chưa tồn tại, thêm mới
            currentItems.add(newItem)
        }

        _cartItems.value = currentItems
        updateCartSummary()
    }

    fun removeFromCart(itemId: String) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.removeAll { it.id == itemId }
        _cartItems.value = currentItems
        updateCartSummary()
    }

    fun updateQuantity(itemId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(itemId)
            return
        }

        val currentItems = _cartItems.value.toMutableList()
        val itemIndex = currentItems.indexOfFirst { it.id == itemId }

        if (itemIndex != -1) {
            val item = currentItems[itemIndex]
            val updatedItem = item.copy(
                quantity = newQuantity,
                totalPrice = item.calculateTotalPrice()
            )
            currentItems[itemIndex] = updatedItem
            _cartItems.value = currentItems
            updateCartSummary()
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        updateCartSummary()
    }

    private fun updateCartSummary() {
        val items = _cartItems.value
        _totalAmount.value = items.sumOf { it.totalPrice }
        _itemCount.value = items.sumOf { it.quantity }
    }

    // Kiểm tra xem sản phẩm có trong giỏ hàng không
    fun isProductInCart(productId: Int, selectedOptions: Map<Int, Int>): Boolean {
        val itemId = generateCartItemId(productId, selectedOptions)
        return _cartItems.value.any { it.id == itemId }
    }

    // Lấy số lượng của sản phẩm trong giỏ hàng
    fun getProductQuantityInCart(productId: Int, selectedOptions: Map<Int, Int>): Int {
        val itemId = generateCartItemId(productId, selectedOptions)
        return _cartItems.value.find { it.id == itemId }?.quantity ?: 0
    }
}