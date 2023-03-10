package space.jay.mediastorequery.helper

import android.provider.MediaStore
import space.jay.mediastorequery.data.DataMedia
import space.jay.mediastorequery.data.OrderBy
import space.jay.mediastorequery.data.Paging
import space.jay.mediastorequery.type.TypeMedia

internal class HelperMediaList(private val helperMediaQuery : HelperMediaQuery) {

    fun getListMedia(
        type : TypeMedia,
        bucketId : String? = null,
        columns : Set<String> = emptySet(),
        orderBy: OrderBy? = null,
        paging : Paging? = null
    ) : List<DataMedia> {
        val columnsForUri = setOf<String>(MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DATA)
        val projection = (columns + columnsForUri).toTypedArray()
        val where = bucketId?.let { "${MediaStore.MediaColumns.BUCKET_ID} = $it" }
        val result = mutableListOf<DataMedia>()
        helperMediaQuery
            .query(
                uri = type.uri,
                columns = projection,
                where = where,
                orderBy = orderBy,
                limit = paging?.pageSize,
                offset = paging?.let { it.pageSize * it.page }
            )
            ?.use {
                while (it.moveToNext()) {
                    result.add(DataMedia(it.getMediaUri(type), it.convertToBundle()))
                }
            }
        return result
    }
}