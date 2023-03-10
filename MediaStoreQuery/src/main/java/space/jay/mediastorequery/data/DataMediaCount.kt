package space.jay.mediastorequery.data

import android.os.Bundle

data class DataMediaCount(
    val bucketId : Long,
    val bucketDisplayName : String,
    val count : Int,
    val data : Bundle? = null
)