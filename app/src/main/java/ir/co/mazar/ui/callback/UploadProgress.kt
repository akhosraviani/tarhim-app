package ir.co.mazar.ui.callback

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadProgress(
    private var file: File,
    var callBack: UploadCallBack
) :
    RequestBody() {

    private val defaultBufferd = 1024

    override fun contentLength(): Long {
        return file.length()
    }

    override fun writeTo(sink: BufferedSink) {
        var fileLenght = file.length()
        var upload: Long = 0
        var read: Int
        var inputStream = FileInputStream(file)
        var buffer = ByteArray(defaultBufferd)
        var handler = Handler(Looper.getMainLooper())
        try {
            while (inputStream.read(buffer).also {
                    read = it
                } != -1) {
                upload += read
                handler.post(ProgressUpload(fileLenght, upload,callBack))
                sink.write(buffer, 0, read)
            }
        } finally {
            inputStream.close()
        }
    }

    override fun contentType(): MediaType? {
        return MediaType.get("file/*")
    }


    private class ProgressUpload(
        private var fileLenght: Long,
        private var upload: Long,
        private var callBack: UploadCallBack
    ) : Runnable {
        override fun run() {
//            callBack.updateProgress((upload *100/ fileLenght)to Int)
        }

    }
}