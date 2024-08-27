package com.safepulse.app.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import android.content.pm.PackageManager
import com.safepulse.app.SettingsActivity // Adjust this import based on your actual package
import kotlinx.coroutines.flow.collect

@Composable
fun MainScreen(viewModel: PulseRateViewModel = viewModel()) {
    val context = LocalContext.current
    val pulseRate by viewModel.pulseRate.collectAsState("Pulse Rate: -- bpm")

    // Set the emergency alert callback
    LaunchedEffect(Unit) {
        viewModel.onEmergencyAlert = {
            sendEmergencyAlert(context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { sendEmergencyAlert(context) }) {
            Text("Panic Button")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(pulseRate)
        Text("Oxygen Level: -- %")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Settings")
        }
    }
}

fun sendEmergencyAlert(context: Context) {
    val phoneNumber = "+1234567890" // Replace with the emergency contact number
    val message = "Emergency Alert: Immediate assistance required. Please check on me."

    // Send SMS
    sendSMS(context, phoneNumber, message)

    // Send WhatsApp Message
    sendWhatsAppMessage(context, phoneNumber, message)

    Toast.makeText(context, "Emergency Alert Sent", Toast.LENGTH_SHORT).show()
}

fun sendSMS(context: Context, phoneNumber: String, message: String) {
    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
        // Request SMS permission if not granted
        // You need to handle this in your Activity
        return
    }
    SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null)
}

fun sendWhatsAppMessage(context: Context, phoneNumber: String, message: String) {
    val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    context.startActivity(intent)
}
