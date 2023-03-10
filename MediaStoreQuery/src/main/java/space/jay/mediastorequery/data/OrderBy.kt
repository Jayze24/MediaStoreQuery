package space.jay.mediastorequery.data

sealed class OrderBy(private val column: String) {
    data class ASC(val column: String) : OrderBy(column)
    data class DESC(val column: String) : OrderBy(column)

    internal fun getString() : String? {
        return when {
            column.isBlank() -> null
            this is ASC -> "$column ASC"
            this is DESC -> "$column DESC"
            else -> null
        }
    }
}