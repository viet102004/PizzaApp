package com.example.pizza_app.ui.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.AddressCreateRequest
import com.example.pizza_app.data.model.AddressInfo
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddressViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _addressList = MutableStateFlow<List<AddressInfo>>(emptyList())
    val addressList: StateFlow<List<AddressInfo>> = _addressList

    companion object {
        private const val ERROR_CANNOT_DELETE_DEFAULT = "Không thể xóa địa chỉ mặc định. Vui lòng đặt địa chỉ khác làm mặc định trước."
        private const val ERROR_USER_NOT_FOUND = "Không tìm thấy thông tin người dùng"
        private const val ERROR_INVALID_USER_ID = "ID người dùng không hợp lệ"
        private const val SUCCESS_DELETE = "Xóa địa chỉ thành công"
        private const val SUCCESS_SET_DEFAULT = "Đã đặt làm địa chỉ mặc định"
        private const val SUCCESS_CREATE = "Thêm địa chỉ thành công"
        private const val ERROR_CREATE_FAILED = "Không thể thêm địa chỉ"
        private const val ERROR_VALIDATION = "Vui lòng kiểm tra lại thông tin"
        private const val ERROR_NETWORK = "Lỗi kết nối mạng"
    }

    fun resetState() {
        _message.value = ""
        _success.value = false
    }

    private fun getUserId(): Long? {
        val user = UserManager.getUser()
        if (user == null) {
            Log.e("AddressViewModel", "User is NULL")
            _message.value = ERROR_USER_NOT_FOUND
            return null
        }

        return try {
            user.ma_nguoi_dung.toLong()
        } catch (e: NumberFormatException) {
            Log.e("AddressViewModel", "Invalid user ID format: ${user.ma_nguoi_dung}")
            _message.value = ERROR_INVALID_USER_ID
            null
        }
    }


    fun isDefaultAddress(addressId: Long): Boolean {
        val address = _addressList.value.find {
            it.ma_thong_tin_giao_hang?.toLong() == addressId
        }
        return address?.la_dia_chi_mac_dinh == 1
    }

    fun getDeliveryAddresses() {
        Log.d("AddressViewModel", "=== getDeliveryAddresses called ===")

        val userId = getUserId() ?: return
        Log.d("AddressViewModel", "User ID: $userId")

        _isLoading.value = true
        Log.d("AddressViewModel", "Loading state set to true")

        viewModelScope.launch {
            Log.d("AddressViewModel", "Inside coroutine - starting API call")
            try {
                Log.d("AddressViewModel", "About to call getDeliveryAddresses with userId: $userId")
                val startTime = System.currentTimeMillis()

                val response = RetrofitInstance.api.getDeliveryAddresses(userId)

                val endTime = System.currentTimeMillis()
                Log.d("AddressViewModel", "API call completed in ${endTime - startTime}ms")

                Log.d("AddressViewModel", "Response received!")
                Log.d("AddressViewModel", "Success: ${response.success}")
                Log.d("AddressViewModel", "Message: '${response.message}'")
                Log.d("AddressViewModel", "Data: ${response.data}")

                if (response.success) {
                    val addresses = response.data ?: emptyList()
                    Log.d("AddressViewModel", "Processing ${addresses.size} addresses")

                    addresses.forEachIndexed { index, address ->
                        Log.d("AddressViewModel", "Address $index:")
                        Log.d("AddressViewModel", "  ID: ${address.ma_thong_tin_giao_hang}")
                        Log.d("AddressViewModel", "  Name: ${address.ten_nguoi_nhan}")
                        Log.d("AddressViewModel", "  Is Default: ${address.la_dia_chi_mac_dinh}")
                    }

                    _addressList.value = addresses
                    _success.value = true
                    Log.d("AddressViewModel", "Success flag set to true")
                } else {
                    Log.w("AddressViewModel", "API returned success=false")
                    _message.value = response.message ?: "Unknown error"
                    _addressList.value = emptyList()
                }

            } catch (e: retrofit2.HttpException) {
                Log.e("AddressViewModel", "HTTP Exception: ${e.code()} - ${e.message()}")
                _message.value = "Lỗi HTTP: ${e.code()}"
                _addressList.value = emptyList()

            } catch (e: java.net.SocketTimeoutException) {
                Log.e("AddressViewModel", "Timeout Exception: ${e.message}")
                _message.value = "Timeout - vui lòng thử lại"
                _addressList.value = emptyList()

            } catch (e: java.net.UnknownHostException) {
                Log.e("AddressViewModel", "Network Exception: ${e.message}")
                _message.value = "Không có kết nối mạng"
                _addressList.value = emptyList()

            } catch (e: Exception) {
                Log.e("AddressViewModel", "General Exception: ${e.message}")
                _message.value = "Lỗi: ${e.message}"
                _addressList.value = emptyList()

            } finally {
                _isLoading.value = false
                Log.d("AddressViewModel", "Finally block - loading set to false")
            }
        }
    }

    fun getAddressDetail(addressId: Long, onResult: (AddressInfo?) -> Unit) {
        val userId = getUserId() ?: return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getDeliveryAddressDetail(userId, addressId)

                if (response.success) {
                    onResult(response.data)
                    _success.value = true
                } else {
                    _message.value = response.message
                    onResult(null)
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối máy chủ"
                onResult(null)
                Log.e("AddressViewModel", "Error getting address detail: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAddress(addressId: Long) {
        Log.d("AddressViewModel", "=== deleteAddress called with ID: $addressId ===")

        // Kiểm tra xem có phải địa chỉ mặc định không
        if (isDefaultAddress(addressId)) {
            Log.w("AddressViewModel", "Cannot delete default address")
            _message.value = ERROR_CANNOT_DELETE_DEFAULT
            return
        }

        val userId = getUserId() ?: return
        Log.d("AddressViewModel", "User ID: $userId")

        _isLoading.value = true
        Log.d("AddressViewModel", "Starting delete operation...")

        viewModelScope.launch {
            try {
                Log.d("AddressViewModel", "Calling deleteDeliveryAddress API...")
                val response = RetrofitInstance.api.deleteDeliveryAddress(userId, addressId)

                Log.d("AddressViewModel", "Delete response: $response")
                Log.d("AddressViewModel", "Delete success: ${response.success}")

                if (response.success) {
                    // Cập nhật danh sách địa chỉ trong UI ngay lập tức
                    val currentList = _addressList.value
                    val updatedList = currentList.filter { address ->
                        val addressIdToCheck = address.ma_thong_tin_giao_hang?.toLong()
                        addressIdToCheck != addressId
                    }

                    Log.d("AddressViewModel", "Original list size: ${currentList.size}")
                    Log.d("AddressViewModel", "Updated list size: ${updatedList.size}")

                    _addressList.value = updatedList
                    _success.value = true
                    _message.value = SUCCESS_DELETE

                    Log.d("AddressViewModel", "Address deleted successfully")
                } else {
                    Log.w("AddressViewModel", "Delete failed: ${response.message}")
                    _message.value = response.message ?: "Không thể xóa địa chỉ"
                }
            } catch (e: retrofit2.HttpException) {
                Log.e("AddressViewModel", "HTTP Exception during delete: ${e.code()} - ${e.message()}")
                _message.value = "Lỗi HTTP: ${e.code()}"

            } catch (e: Exception) {
                Log.e("AddressViewModel", "Exception during delete: ${e.message}", e)
                _message.value = "Lỗi kết nối máy chủ"

            } finally {
                _isLoading.value = false
                Log.d("AddressViewModel", "Delete operation completed")
            }
        }
    }

    fun setDefaultAddress(addressId: Long) {
        Log.d("AddressViewModel", "=== setDefaultAddress called with ID: $addressId ===")

        val userId = getUserId() ?: return
        Log.d("AddressViewModel", "User ID: $userId")

        _isLoading.value = true
        Log.d("AddressViewModel", "Starting set default operation...")

        viewModelScope.launch {
            try {
                Log.d("AddressViewModel", "Calling setDefaultDeliveryAddress API...")
                val response = RetrofitInstance.api.setDefaultDeliveryAddress(userId, addressId)

                Log.d("AddressViewModel", "Set default response: $response")
                Log.d("AddressViewModel", "Set default success: ${response.success}")
                Log.d("AddressViewModel", "Set default message: ${response.message}")

                if (response.success) {
                    // Cập nhật danh sách địa chỉ trong UI
                    try {
                        val currentList = _addressList.value
                        Log.d("AddressViewModel", "Current list size before update: ${currentList.size}")

                        val updatedList = currentList.map { address ->
                            val addressIdToCheck = address.ma_thong_tin_giao_hang
                            val newAddress = address.copy(
                                la_dia_chi_mac_dinh = if (addressIdToCheck == addressId) 1 else 0
                            )
                            Log.d("AddressViewModel", "Address ${address.ma_thong_tin_giao_hang}: default changed from ${address.la_dia_chi_mac_dinh} to ${newAddress.la_dia_chi_mac_dinh}")
                            newAddress
                        }

                        Log.d("AddressViewModel", "Updated address list with new default - size: ${updatedList.size}")
                        _addressList.value = updatedList
                        _success.value = true
                        _message.value = SUCCESS_SET_DEFAULT

                        Log.d("AddressViewModel", "Default address set successfully")
                    } catch (updateException: Exception) {
                        Log.e("AddressViewModel", "Error updating UI list: ${updateException.message}", updateException)
                        // Vẫn báo thành công vì API đã success
                        _success.value = true
                        _message.value = SUCCESS_SET_DEFAULT
                        // Refresh lại danh sách để đảm bảo UI sync
                        refreshAddresses()
                    }
                } else {
                    Log.w("AddressViewModel", "Set default failed: ${response.message}")
                    _message.value = response.message ?: "Không thể đặt làm địa chỉ mặc định"
                }
            } catch (e: retrofit2.HttpException) {
                Log.e("AddressViewModel", "HTTP Exception during set default: ${e.code()} - ${e.message()}")
                Log.e("AddressViewModel", "HTTP Exception body: ${e.response()?.errorBody()?.string()}")

                val errorMessage = when (e.code()) {
                    400 -> "Yêu cầu không hợp lệ"
                    401 -> "Phiên đăng nhập đã hết hạn"
                    403 -> "Không có quyền thực hiện"
                    404 -> "Không tìm thấy địa chỉ"
                    422 -> "Dữ liệu không hợp lệ"
                    500 -> "Lỗi máy chủ nội bộ"
                    else -> "Lỗi HTTP: ${e.code()}"
                }
                _message.value = errorMessage

            } catch (e: java.net.SocketTimeoutException) {
                Log.e("AddressViewModel", "Timeout Exception during set default: ${e.message}")
                _message.value = "Hết thời gian chờ - vui lòng thử lại"

            } catch (e: java.net.UnknownHostException) {
                Log.e("AddressViewModel", "Network Exception during set default: ${e.message}")
                _message.value = "Không có kết nối mạng"

            } catch (e: com.google.gson.JsonSyntaxException) {
                Log.e("AddressViewModel", "JSON parsing error during set default: ${e.message}")
                _message.value = "Lỗi xử lý dữ liệu từ máy chủ"

            } catch (e: Exception) {
                Log.e("AddressViewModel", "Exception during set default: ${e.message}", e)
                Log.e("AddressViewModel", "Exception type: ${e.javaClass.simpleName}")

                // Log stack trace để debug
                e.printStackTrace()

                _message.value = "Lỗi không xác định: ${e.javaClass.simpleName}"

            } finally {
                _isLoading.value = false
                Log.d("AddressViewModel", "Set default operation completed")
            }
        }
    }

    fun createAddress(
        name: String,
        phone: String,
        street: String,
        ward: String,
        district: String,
        province: String,
        isDefault: Boolean,
        notes: String = ""
    ) {
        Log.d("AddressViewModel", "=== createAddress called ===")

        // Validate input
        if (name.isBlank() || phone.isBlank() || street.isBlank() ||
            ward.isBlank() || district.isBlank() || province.isBlank()) {
            Log.w("AddressViewModel", "Validation failed - empty fields")
            _message.value = ERROR_VALIDATION
            return
        }

        // Validate phone number
        if (phone.length < 10 || !phone.all { it.isDigit() }) {
            Log.w("AddressViewModel", "Phone validation failed: $phone")
            _message.value = "Số điện thoại không hợp lệ"
            return
        }

        val userId = getUserId() ?: return
        Log.d("AddressViewModel", "User ID: $userId")

        val addressRequest = AddressCreateRequest(
            ten_nguoi_nhan = name.trim(),
            so_dien_thoai_nguoi_nhan = phone.trim(),
            so_duong = street.trim(),
            phuong_xa = ward.trim(),
            quan_huyen = district.trim(),
            tinh_thanh_pho = province.trim(),
            la_dia_chi_mac_dinh = if (isDefault) 1 else 0,
            ghi_chu = notes.trim().ifEmpty { null }
        )

        Log.d("AddressViewModel", "Address request: $addressRequest")

        _isLoading.value = true
        Log.d("AddressViewModel", "Starting create address operation...")

        viewModelScope.launch {
            try {
                Log.d("AddressViewModel", "Calling createDeliveryAddress API...")
                val response = RetrofitInstance.api.createDeliveryAddress(userId, addressRequest)

                Log.d("AddressViewModel", "Create response: $response")
                Log.d("AddressViewModel", "Create success: ${response.success}")

                if (response.success) {
                    _success.value = true
                    _message.value = SUCCESS_CREATE
                    Log.d("AddressViewModel", "Address created successfully")

                    // Refresh address list to show the new address
                    refreshAddresses()
                } else {
                    Log.w("AddressViewModel", "Create failed: ${response.message}")
                    _message.value = response.message ?: ERROR_CREATE_FAILED
                }
            } catch (e: retrofit2.HttpException) {
                Log.e("AddressViewModel", "HTTP Exception during create: ${e.code()} - ${e.message()}")
                val errorMessage = when (e.code()) {
                    400 -> "Thông tin không hợp lệ"
                    401 -> "Phiên đăng nhập đã hết hạn"
                    403 -> "Không có quyền thực hiện"
                    404 -> "Không tìm thấy dịch vụ"
                    422 -> "Dữ liệu không hợp lệ"
                    500 -> "Lỗi máy chủ"
                    else -> "Lỗi HTTP: ${e.code()}"
                }
                _message.value = errorMessage

            } catch (e: java.net.SocketTimeoutException) {
                Log.e("AddressViewModel", "Timeout Exception during create: ${e.message}")
                _message.value = "Hết thời gian chờ - vui lòng thử lại"

            } catch (e: java.net.UnknownHostException) {
                Log.e("AddressViewModel", "Network Exception during create: ${e.message}")
                _message.value = ERROR_NETWORK

            } catch (e: Exception) {
                Log.e("AddressViewModel", "Exception during create: ${e.message}", e)
                _message.value = "Lỗi không xác định: ${e.message}"

            } finally {
                _isLoading.value = false
                Log.d("AddressViewModel", "Create address operation completed")
            }
        }
    }

    fun refreshAddresses() {
        getDeliveryAddresses()
    }
}