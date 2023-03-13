package space.jay.mediastorequery

import android.content.Context
import android.net.Uri
import space.jay.mediastorequery.data.DataMedia
import space.jay.mediastorequery.data.DataMediaCount
import space.jay.mediastorequery.data.OrderBy
import space.jay.mediastorequery.data.Paging
import space.jay.mediastorequery.helper.HelperMediaCount
import space.jay.mediastorequery.helper.HelperMediaList
import space.jay.mediastorequery.helper.HelperMediaQuery
import space.jay.mediastorequery.type.TypeMedia

class MediaStoreQuery(private val contextApplication : Context) {

    private val helperMediaQuery by lazy { HelperMediaQuery(contextApplication.contentResolver) }

    fun query(
        uri : Uri,
        columns : Array<out String>? = null,
        where : String? = null,
        whereArgs : Array<out String>? = null,
        groupBy : Array<String>? = null,
        orderBy : OrderBy? = null,
        limit : Int? = null,
        offset : Int? = null
    ) = helperMediaQuery.query(uri, columns, where, whereArgs, groupBy, orderBy, limit, offset)

    fun getCount(type : TypeMedia, bucketId : Long? = null) : Int {
        return HelperMediaCount(helperMediaQuery).getCount(type, bucketId)
    }

    fun getCountByDirectory(
        type : TypeMedia,
        columns : Set<String> = emptySet(),
    ) : List<DataMediaCount> {
        return HelperMediaCount(helperMediaQuery).getCountByDirectory(type, columns)
    }

    fun getListMedia(
        type : TypeMedia,
        bucketId : Long? = null,
        columns : Set<String> = emptySet(),
        orderBy: OrderBy? = null,
        paging : Paging? = null
    ) : List<DataMedia> {
        return HelperMediaList(helperMediaQuery)
            .getListMedia(type, bucketId, columns, orderBy, paging)
    }
}