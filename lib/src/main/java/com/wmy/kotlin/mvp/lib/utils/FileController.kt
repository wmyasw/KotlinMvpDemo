package com.wmy.kotlin.mvp.lib.utils

import android.content.Context
import java.io.*

class FileController private constructor() {
    private var mContext: Context? = null
    private val LOG_FILENAME = "LOG_FILE"
    fun init(context: Context?) {
        mContext = context
    }



    fun deleteLogFile() {
        try {
            val file = File(mContext!!.getFilesDir(), LOG_FILENAME)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun readFromLogFile(): String? {
        val file = File(mContext!!.getFilesDir(), LOG_FILENAME)
        var data: String? = null
        var br: BufferedReader? = null
        if (!file.exists()) return null
        try {
            br = BufferedReader(FileReader(file))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            val sb = StringBuilder()
            var line: String = br!!.readLine()
            while (line != null) {
                sb.append(line)
                sb.append("\n\r")
                line = br.readLine()
            }
            data = sb.toString()
            if (!data.isEmpty()) {
                return data
            }
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            return data
        }
    }

    fun writeToLogFile(s: String?) {
        if (s == null) return
        var outputStream: FileOutputStream? = null
        try {
            val file = File(mContext!!.getFilesDir(), LOG_FILENAME)
            outputStream = FileOutputStream(file, true) // will overwrite existing data
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            outputStream!!.write(s.toByteArray())
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        @Volatile
        private var m_instance: FileController? = null

        @get:Synchronized
        val fileControl: FileController?
            get() {
                if (m_instance == null) {
                    synchronized(FileController::class.java) {
                        if (m_instance == null) {
                            m_instance = FileController()
                        }
                    }
                }
                return m_instance
            }
    }
}