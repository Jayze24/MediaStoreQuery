package space.jay.mediastorequery.helper

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import space.jay.mediastorequery.data.OrderBy

internal class HelperMediaQuery(private val contentResolver: ContentResolver) {

    fun query(
        uri : Uri,
        columns : Array<out String>? = null,
        where : String? = null,
        whereArgs : Array<out String>? = null,
        groupBy : Array<String>? = null,
        orderBy : OrderBy? = null,
        limit : Int? = null,
        offset : Int? = null
    ) : Cursor? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            contentResolver.query(
                uri,
                columns,
                Bundle().apply {
                    putString(ContentResolver.QUERY_ARG_SQL_SELECTION, where)
                    putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, whereArgs)
                    putStringArray(ContentResolver.QUERY_ARG_GROUP_COLUMNS, groupBy)
                    putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, orderBy?.getString())
                    limit?.also { putInt(ContentResolver.QUERY_ARG_LIMIT, it) }
                    offset?.also { putInt(ContentResolver.QUERY_ARG_OFFSET, it) }
                },
                null
            )
        } else {
            val w = StringBuilder()
            where?.also { w.append(it) } ?: w.append("1")
            groupBy?.also { w.append(it.joinToString(prefix = ") GROUP BY (")) }

            val sort = StringBuilder()
            orderBy?.getString()?.also { sort.append(it) } ?: sort.append("1")
            limit?.also { sort.append(" LIMIT $it") }
            offset?.also { sort.append(" OFFSET $it") }

            contentResolver.query(
                uri,
                columns,
                w.toString(),
                whereArgs,
                sort.toString()
            )
        }
    }
}