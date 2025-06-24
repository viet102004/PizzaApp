package com.example.pizza_app.ui.home

//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.pizza_app.data.model.Product
//import com.example.pizza_app.data.repository.SearchRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.delay
//
//class SearchViewModel(
//    private val searchRepository: SearchRepository = SearchRepository()
//) : ViewModel() {
//
//    private val _searchQuery = MutableStateFlow("")
//    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
//
//    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
//    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    private val _hasSearched = MutableStateFlow(false)
//    val hasSearched: StateFlow<Boolean> = _hasSearched.asStateFlow()
//
//    private val _searchSuggestions = MutableStateFlow<List<String>>(emptyList())
//    val searchSuggestions: StateFlow<List<String>> = _searchSuggestions.asStateFlow()
//
//    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
//    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()
//
//    private val _popularSearches = MutableStateFlow<List<String>>(emptyList())
//    val popularSearches: StateFlow<List<String>> = _popularSearches.asStateFlow()
//
//    init {
//        loadRecentSearches()
//        loadPopularSearches()
//    }
//
//    fun updateSearchQuery(query: String) {
//        _searchQuery.value = query
//        if (query.isNotEmpty()) {
//            loadSearchSuggestions(query)
//        } else {
//            _searchSuggestions.value = emptyList()
//        }
//    }
//
//    fun searchProducts() {
//        val query = _searchQuery.value.trim()
//        if (query.isEmpty()) return
//
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//                _hasSearched.value = true
//
//                // Thêm delay nhỏ để UX mượt hơn
//                delay(300)
//
//                val results = searchRepository.searchProducts(
//                    query = query,
//                    categories = emptyList(), // Có thể mở rộng để filter theo category
//                    sortBy = "relevance"
//                )
//
//                _searchResults.value = results
//
//                // Lưu vào recent searches
//                addToRecentSearches(query)
//
//            } catch (e: Exception) {
//                _searchResults.value = emptyList()
//                // Handle error - có thể emit error state
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun searchWithSuggestion(suggestion: String) {
//        _searchQuery.value = suggestion
//        searchProducts()
//    }
//
//    fun clearSearch() {
//        _searchQuery.value = ""
//        _searchResults.value = emptyList()
//        _hasSearched.value = false
//        _searchSuggestions.value = emptyList()
//    }
//
//    private fun loadSearchSuggestions(query: String) {
//        viewModelScope.launch {
//            try {
//                val suggestions = searchRepository.getSearchSuggestions(query)
//                _searchSuggestions.value = suggestions
//            } catch (e: Exception) {
//                _searchSuggestions.value = emptyList()
//            }
//        }
//    }
//
//    private fun loadRecentSearches() {
//        viewModelScope.launch {
//            try {
//                val recent = searchRepository.getRecentSearches()
//                _recentSearches.value = recent
//            } catch (e: Exception) {
//                _recentSearches.value = emptyList()
//            }
//        }
//    }
//
//    private fun loadPopularSearches() {
//        viewModelScope.launch {
//            try {
//                val popular = searchRepository.getPopularSearches()
//                _popularSearches.value = popular
//            } catch (e: Exception) {
//                _popularSearches.value = emptyList()
//            }
//        }
//    }
//
//    private fun addToRecentSearches(query: String) {
//        viewModelScope.launch {
//            try {
//                searchRepository.addToRecentSearches(query)
//                loadRecentSearches() // Reload recent searches
//            } catch (e: Exception) {
//                // Handle error
//            }
//        }
//    }
//
//    fun removeFromRecentSearches(query: String) {
//        viewModelScope.launch {
//            try {
//                searchRepository.removeFromRecentSearches(query)
//                loadRecentSearches()
//            } catch (e: Exception) {
//                // Handle error
//            }
//        }
//    }
//
//    fun clearRecentSearches() {
//        viewModelScope.launch {
//            try {
//                searchRepository.clearRecentSearches()
//                _recentSearches.value = emptyList()
//            } catch (e: Exception) {
//                // Handle error
//            }
//        }
//    }
//}