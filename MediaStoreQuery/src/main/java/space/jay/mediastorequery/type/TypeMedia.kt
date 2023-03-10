package space.jay.mediastorequery.type

import android.net.Uri
import android.provider.MediaStore

enum class TypeMedia(internal val uri : Uri) {
    IMAGE(MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
    VIDEO(MediaStore.Video.Media.EXTERNAL_CONTENT_URI),
    AUDIO(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
}