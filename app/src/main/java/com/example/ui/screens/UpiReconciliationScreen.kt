package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TransactionEntity
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.GreenIncomeLight
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.UpiBlue
import com.example.ui.theme.UpiBlueLight
import com.example.ui.viewmodel.StreetLedgerViewModel
import com.example.util.AppLanguage
import com.example.util.Formatters
import com.example.util.StreetLedgerStrings
import kotlin.math.abs

@Composable
fun UpiReconciliationScreen(
    viewModel: StreetLedgerViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val todayTransactions by viewModel.todayTransactions.collectAsStateWithLifecycle()
    val soundboxInput by viewModel.soundboxActualInput.collectAsStateWithLifecycle()

    val upiTransactions = todayTransactions.filter { it.paymentMode == "UPI" }
    val totalLoggedUpi = upiTransactions.sumOf { it.amount }
    val bankSoundboxAmount = soundboxInput.toDoubleOrNull() ?: totalLoggedUpi
    val difference = totalLoggedUpi - bankSoundboxAmount
    val verifiedCount = upiTransactions.count { it.isUpiReconciled }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("upi_reconciliation_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(UpiBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = null,
                        tint = UpiBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = StreetLedgerStrings.get("upi_reconciliation_title", language),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealDark
                    )
                    Text(
                        text = StreetLedgerStrings.get("upi_sub", language),
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Summary Comparison Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Total Recorded vs Soundbox
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = StreetLedgerStrings.get("logged_upi_total", language),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = Formatters.formatRupee(totalLoggedUpi),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = UpiBlue
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (difference == 0.0) GreenIncomeLight else Color(0xFFFFF3E0),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(
                                text = if (difference == 0.0)
                                    "Balanced ✓"
                                else
                                    "Diff: ${Formatters.formatRupee(abs(difference))}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (difference == 0.0) GreenIncome else Color(0xFFE65100),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Soundbox input field
                    OutlinedTextField(
                        value = soundboxInput,
                        onValueChange = { viewModel.updateSoundboxInput(it) },
                        label = { Text(StreetLedgerStrings.get("bank_soundbox_total", language)) },
                        placeholder = { Text(Formatters.formatPlainAmount(totalLoggedUpi)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = TealPrimary
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("soundbox_input_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF7FAF9),
                            unfocusedContainerColor = Color(0xFFF7FAF9)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status Pill
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = if (difference == 0.0) GreenIncomeLight else Color(0xFFFFECEB)
                    ) {
                        Text(
                            text = if (difference == 0.0)
                                StreetLedgerStrings.get("all_balanced", language)
                            else
                                "Attention: Difference of ${Formatters.formatRupee(abs(difference))}. Check unverified entries below.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (difference == 0.0) GreenIncome else ExpenseRed,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }

        // Action: Reconcile All
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's UPI Receipts (${verifiedCount}/${upiTransactions.size} Verified)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealDark
                )

                if (upiTransactions.isNotEmpty() && verifiedCount < upiTransactions.size) {
                    Button(
                        onClick = { viewModel.reconcileAllTodayUpi() },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = StreetLedgerStrings.get("reconcile_all", language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // List of UPI transactions with 1-tap checkmark
        if (upiTransactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No UPI payments recorded today yet.\nTransactions logged with '+ UPI In' appear here for verification.",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else {
            items(upiTransactions, key = { it.id }) { tx ->
                UpiAuditRow(
                    transaction = tx,
                    language = language,
                    onToggleVerify = {
                        viewModel.toggleUpiVerification(tx.id, tx.isUpiReconciled)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun UpiAuditRow(
    transaction: TransactionEntity,
    language: AppLanguage,
    onToggleVerify: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("upi_audit_${transaction.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (transaction.isUpiReconciled) Color.White else Color(0xFFFFFDE7)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = Formatters.formatRupee(transaction.amount),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = UpiBlue
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (transaction.isUpiReconciled) GreenIncomeLight else Color(0xFFFFE0B2)
                    ) {
                        Text(
                            text = if (transaction.isUpiReconciled)
                                StreetLedgerStrings.get("status_verified", language)
                            else
                                StreetLedgerStrings.get("status_unverified", language),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (transaction.isUpiReconciled) GreenIncome else Color(0xFFE65100),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "${transaction.category} • ${Formatters.formatTime(transaction.timestamp)}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (transaction.note.isNotBlank()) {
                    Text(
                        text = transaction.note,
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }
            }

            // Clickable Verification Button
            Surface(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onToggleVerify() },
                shape = CircleShape,
                color = if (transaction.isUpiReconciled) GreenIncomeLight else Color(0xFFEEEEEE)
            ) {
                Icon(
                    imageVector = if (transaction.isUpiReconciled) Icons.Default.CheckCircle else Icons.Default.Pending,
                    contentDescription = "Toggle status",
                    tint = if (transaction.isUpiReconciled) GreenIncome else Color.Gray,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                )
            }
        }
    }
}
