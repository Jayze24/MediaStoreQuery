package space.jay.mediastorequery.helper

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import space.jay.mediastorequery.type.TypeMedia
import java.io.File

internal fun Cursor.convertToBundle(exception : Set<String>? = null) : Bundle {
    return Bundle().apply {
        repeat(columnCount) { index ->
            val key = getColumnName(index)
            val isException = exception?.contains(key) ?: false
            if (!isException) {
                when (getType(index)) {
                    Cursor.FIELD_TYPE_INTEGER -> putLong(key, getLong(index))
                    Cursor.FIELD_TYPE_FLOAT -> putFloat(key, getFloat(index))
                    Cursor.FIELD_TYPE_BLOB -> putByteArray(key, getBlob(index))
                    else -> putString(key, getString(index))
                }
            }
        }
    }
}

internal fun Cursor.getMediaUri(type : TypeMedia) : Uri? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val id = getLong(getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
        ContentUris.withAppendedId(type.uri, id)
    } else {
        val path = getString(getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
        Uri.fromFile(File(path))
    }
}