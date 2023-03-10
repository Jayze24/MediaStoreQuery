package space.jay.mediastorequery.helper

import android.provider.MediaStore
import space.jay.mediastorequery.data.DataMediaCount
import space.jay.mediastorequery.type.TypeMedia

internal class HelperMediaCount(private val helperMediaQuery : HelperMediaQuery) {

    fun getCount(type : TypeMedia, bucketId : Long? = null) : Int {
        val selection = bucketId?.let { "${MediaStore.MediaColumns.BUCKET_ID}=$it" }
        var count = 0
        helperMediaQuery
            .query(
                uri = type.uri,
                columns = arrayOf(MediaStore.MediaColumns._ID),
                where = selection
            )?.use {
                count = it.count
            }
        return count
    }

    fun getCountByDirectory(
        type : TypeMedia,
        columns : Set<String> = emptySet(),
    ) : List<DataMediaCount> {
        val keyCount by lazy { "COUNT(${MediaStore.MediaColumns._ID})" }
        val columnsForCount by lazy { setOf<String>(MediaStore.MediaColumns.BUCKET_ID, MediaStore.MediaColumns.BUCKET_DISPLAY_NAME, keyCount) }
        val projection = (columns + columnsForCount).toTypedArray()
        val result = mutableListOf<DataMediaCount>()
        helperMediaQuery
            .query(
                uri = type.uri,
                columns = projection,
                groupBy = arrayOf(MediaStore.MediaColumns.BUCKET_ID)
            )?.use {
                while (it.moveToNext()) {
                    result.add(
                        DataMediaCount(
                            it.getLong(it.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID)),
                            it.getString(it.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)),
                            it.getInt(it.getColumnIndexOrThrow(keyCount)),
                            it.convertToBundle(columnsForCount)
                        )
                    )
                }
            }
        return result
    }
}