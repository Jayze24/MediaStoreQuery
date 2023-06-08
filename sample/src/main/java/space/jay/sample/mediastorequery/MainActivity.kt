package space.jay.sample.mediastorequery

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import space.jay.mediastorequery.MediaStoreQuery
import space.jay.mediastorequery.data.OrderBy
import space.jay.mediastorequery.data.Paging
import space.jay.mediastorequery.type.TypeMedia
import space.jay.permissionmanager.PermissionManager

class MainActivity : AppCompatActivity() {

    private val media by lazy { MediaStoreQuery(applicationContext) }
    private val result by lazy { findViewById<TextView>(R.id.textViewResult) }
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.buttonAll).apply {
            setOnClickListener {
                checkPermissionReadStorage(::getCountAndGetListImageWithPaging)
            }
        }

        findViewById<Button>(R.id.buttonDirectory).apply {
            setOnClickListener {
                checkPermissionReadStorage(::getCountByDirectoryAndGetListImageFromDirectory)
            }
        }
    }

    @SuppressLint("InlinedApi")
    private fun checkPermissionReadStorage(runnable : ()->Unit) {
        PermissionManager.Builder(applicationContext)
            .setPermissionWithTarget(Manifest.permission.READ_EXTERNAL_STORAGE, maxSdk = Build.VERSION_CODES.S_V2)
            .setPermissionWithTarget(Manifest.permission.READ_MEDIA_IMAGES, minSdk = Build.VERSION_CODES.TIRAMISU)
            .setPermissionWithTarget(Manifest.permission.READ_MEDIA_VIDEO, minSdk = Build.VERSION_CODES.TIRAMISU)
            .setPermissionWithTarget(Manifest.permission.READ_MEDIA_AUDIO, minSdk = Build.VERSION_CODES.TIRAMISU)
            .setListenerShowDialogBeforeRequest { _, startRequest ->
                AlertDialog.Builder(this).apply {
                    title = "샘플앱을 이용하기 위해서 권한을 승인해 주세요."
                    setOnDismissListener{ startRequest() }
                    show()
                }
            }
            .setListenerResult { _, isGranted ->
                if (isGranted) {
                    runnable()
                }
            }
            .check()
    }

    private fun getCountAndGetListImageWithPaging() {
        val count = media.getCount(TypeMedia.IMAGE)
        val listImage = media.getListMedia(
            type = TypeMedia.IMAGE,
            paging = Paging(0, 20),
            orderBy = OrderBy.DESC(MediaStore.MediaColumns._ID),
            columns = setOf(MediaStore.Images.ImageColumns.SIZE)
        )
        result.text = "Total count : $count\n\nlist image paging(0~19)\n$listImage"
    }

    private fun getCountByDirectoryAndGetListImageFromDirectory() {
        val countByDirectory = media.getCountByDirectory(
            type = TypeMedia.IMAGE,
            columns = setOf(MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns._ID)
        )
        val listImageFromDirectory = media.getListMedia(
            type = TypeMedia.IMAGE,
            bucketId = countByDirectory.first().bucketId,
            columns = setOf(MediaStore.Images.ImageColumns.MIME_TYPE),
            orderBy = OrderBy.DESC(MediaStore.MediaColumns.DATA),
        )
        result.text = "count by directory : $countByDirectory\n\nlist from first directory\n$listImageFromDirectory"
    }
}