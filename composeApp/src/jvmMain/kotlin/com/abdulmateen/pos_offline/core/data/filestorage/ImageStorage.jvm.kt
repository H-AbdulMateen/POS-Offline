package com.abdulmateen.pos_offline.core.data.filestorage

import java.io.File
import java.util.UUID

actual class ImageStorage {
    actual suspend fun saveImage(bytes: ByteArray): String {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val picturesDir = File(
            System.getProperty("user.home"),
            "POS/Images"
        )
        picturesDir.mkdirs()
        val file = File(picturesDir, fileName)
        file.writeBytes(bytes)
        return file.absolutePath
    }

    actual suspend fun getImage(fileName: String): ByteArray? {
        val picturesDir = File(
            System.getProperty("user.home"),
            "POS/Images"
        )
        val file = File(picturesDir, fileName)

        return if (file.exists()) {
            file.readBytes()
        } else {
            null
        }
    }

    actual suspend fun deleteImage(fileName: String) {
        val picturesDir = File(
            System.getProperty("user.home"),
            "POS/Images"
        )
        val file = File(picturesDir, fileName)
        if (file.exists()) {
            file.delete()
        }
    }
}