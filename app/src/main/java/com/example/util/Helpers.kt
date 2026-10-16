package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatters {
    fun formatRupee(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        val formatted = format.format(amount)
        // Clean any weird symbols to standard ₹
        return if (formatted.startsWith("₹")) formatted else "₹${String.format(Locale.getDefault(), "%,.0f", amount)}"
    }

    fun formatPlainAmount(amount: Double): String {
        return if (amount % 1.0 == 0.0) {
            String.format(Locale.getDefault(), "%.0f", amount)
        } else {
            String.format(Locale.getDefault(), "%.2f", amount)
        }
    }

    fun formatTime(millis: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(millis))
    }

    fun formatDate(millis: Long): String {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        return sdf.format(Date(millis))
    }
}

object Haptics {
    fun tick(context: Context) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(25)
                }
            }
        } catch (_: Exception) {}
    }
}

object SocialSharing {
    fun shareWhatsAppReminder(
        context: Context,
        recipientName: String,
        phone: String,
        amount: Double,
        shopName: String,
        language: AppLanguage
    ) {
        val template = StreetLedgerStrings.get("whatsapp_reminder_template", language)
        val message = String.format(Locale.getDefault(), template, recipientName, shopName, amount)

        val cleanPhone = phone.replace(Regex("[^0-9]"), "")
        val fullPhone = if (cleanPhone.length == 10) "91$cleanPhone" else cleanPhone

        val intent = if (fullPhone.isNotEmpty()) {
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$fullPhone&text=${Uri.encode(message)}")
            Intent(Intent.ACTION_VIEW, uri)
        } else {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
        }

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            // Fallback to generic text share
            val genericIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            context.startActivity(Intent.createChooser(genericIntent, "Share Payment Reminder"))
        }
    }
}
