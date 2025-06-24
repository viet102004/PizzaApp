package com.example.pizza_app.data.model

data class SearchRequest(
    val query: String,
    val categories: List<String> = emptyList(),
    val sortBy: String = "relevance", // relevance, price_low, price_high, newest, rating
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val page: Int = 1,
    val limit: Int = 20
)

data class SearchResponse(
    val success: Boolean,
    val data: List<Product>,
    val totalCount: Int,
    val page: Int,
    val totalPages: Int,
    val message: String = ""
)

data class SuggestionsResponse(
    val success: Boolean,
    val suggestions: List<String>,
    val message: String = ""
)

data class PopularSearchesResponse(
    val success: Boolean,
    val searches: List<String>,
    val message: String = ""
)

data class SearchHistoryResponse(
    val success: Boolean,
    val history: List<String>,
    val message: String = ""
)

data class SaveSearchHistoryRequest(
    val userId: Long,
    val query: String
)

data class SearchFiltersResponse(
    val success: Boolean,
    val categories: List<Category>,
    val priceRanges: List<PriceRange>,
    val message: String = ""
)

data class PriceRange(
    val min: Double,
    val max: Double,
    val label: String
)
