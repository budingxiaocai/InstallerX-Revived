package com.rosan.installer.ui.util

import java.io.File
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

const val MIN_FEEDBACK_DURATION_MS = 500L

fun Long.formatSize(): String {
    if (this <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()
    return String.format(
        Locale.getDefault(),
        "%.2f %s",
        this / 1024.0.pow(digitGroups.toDouble()),
        units[digitGroups]
    )
}

fun File.getDirectorySize(): Long {
    if (!this.exists() || !this.isDirectory) return 0L
    return this.walkTopDown().filter { it.isFile }.sumOf { it.length() }
}