package com.example.pizza_app.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageUtils {

    private const val TAG = "ImageUtils"
    private const val MAX_IMAGE_SIZE = 1024 // Max width/height in pixels
    private const val JPEG_QUALITY = 85 // JPEG compression quality (0-100)

    /**
     * Nén và resize ảnh từ URI
     */
    fun compressImage(context: Context, uri: Uri, maxSizeKB: Int = 500): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Log.e(TAG, "Không thể mở InputStream từ URI")
                return null
            }

            // Decode ảnh với options để tối ưu memory
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }

            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close()

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)
            options.inJustDecodeBounds = false

            // Decode ảnh thực sự
            val newInputStream = context.contentResolver.openInputStream(uri)
            var bitmap = BitmapFactory.decodeStream(newInputStream, null, options)
            newInputStream?.close()

            if (bitmap == null) {
                Log.e(TAG, "Không thể decode bitmap từ URI")
                return null
            }

            // Xử lý orientation
            bitmap = handleImageOrientation(context, uri, bitmap)

            // Tạo file tạm thời
            val tempFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")

            // Nén và lưu ảnh
            val outputStream = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, outputStream)
            outputStream.close()

            // Giải phóng bitmap
            bitmap.recycle()

            // Kiểm tra kích thước file
            val fileSizeKB = tempFile.length() / 1024
            Log.d(TAG, "Kích thước file sau nén: ${fileSizeKB}KB")

            if (fileSizeKB > maxSizeKB) {
                // Nếu vẫn quá lớn, thử nén với quality thấp hơn
                return compressWithLowerQuality(tempFile, maxSizeKB)
            }

            tempFile
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi nén ảnh: ${e.message}")
            null
        }
    }

    /**
     * Tính toán inSampleSize để resize ảnh
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    /**
     * Xử lý orientation của ảnh
     */
    private fun handleImageOrientation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream == null) return bitmap

            val exif = ExifInterface(inputStream)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            inputStream.close()

            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
                else -> return bitmap
            }

            val rotatedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
            )

            if (rotatedBitmap != bitmap) {
                bitmap.recycle()
            }

            rotatedBitmap
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi xử lý orientation: ${e.message}")
            bitmap
        }
    }

    /**
     * Nén ảnh với quality thấp hơn nếu file vẫn quá lớn
     */
    private fun compressWithLowerQuality(file: File, maxSizeKB: Int): File? {
        return try {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            if (bitmap == null) return null

            var quality = 70
            var compressedFile: File

            do {
                compressedFile = File(file.parent, "compressed_low_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(compressedFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                outputStream.close()

                val fileSizeKB = compressedFile.length() / 1024
                Log.d(TAG, "Nén với quality $quality: ${fileSizeKB}KB")

                if (fileSizeKB <= maxSizeKB) {
                    bitmap.recycle()
                    file.delete() // Xóa file cũ
                    return compressedFile
                }

                compressedFile.delete()
                quality -= 10

            } while (quality >= 20)

            bitmap.recycle()
            file.delete()
            null
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi nén với quality thấp: ${e.message}")
            null
        }
    }

    /**
     * Kiểm tra xem file có phải là ảnh hợp lệ không
     */
    fun isValidImageFile(context: Context, uri: Uri): Boolean {
        return try {
            val mimeType = context.contentResolver.getType(uri)
            val validTypes = listOf("image/jpeg", "image/jpg", "image/png", "image/webp")
            mimeType in validTypes
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi kiểm tra loại file: ${e.message}")
            false
        }
    }

    /**
     * Lấy kích thước file theo KB
     */
    fun getFileSizeKB(context: Context, uri: Uri): Long {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val size = inputStream?.available()?.toLong() ?: 0L
            inputStream?.close()
            size / 1024
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi lấy kích thước file: ${e.message}")
            0L
        }
    }

    /**
     * Tạo URI cho file ảnh tạm thời (dùng cho camera)
     */
    fun createTempImageUri(context: Context): Uri? {
        return try {
            val tempFile = File(context.cacheDir, "temp_camera_${System.currentTimeMillis()}.jpg")
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI.let { uri ->
                context.contentResolver.insert(uri, android.content.ContentValues().apply {
                    put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, tempFile.name)
                    put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                })
            }
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi tạo URI tạm thời: ${e.message}")
            null
        }
    }
}