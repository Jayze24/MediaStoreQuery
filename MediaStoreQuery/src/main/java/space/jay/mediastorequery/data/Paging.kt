package space.jay.mediastorequery.data

data class Paging(
    @androidx.annotation.IntRange(from = 0)
    val page : Int,
    @androidx.annotation.IntRange(from = 1)
    val pageSize : Int
)
