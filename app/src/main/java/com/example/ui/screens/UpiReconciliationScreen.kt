package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TransactionEntity
import com.example.ui.theme.CosmicPurpleBright
import com.example.ui.theme.CosmicPurpleDeep
import com.example.ui.theme.CosmicPurplePrimary
import com.example.ui.theme.ExpensePink
import com.example.ui.theme.GlassBorderPurple
import com.example.ui.theme.NeonMint
import com.example.ui.theme.NeonMintDark
import com.example.ui.theme.NeonMintGlow
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianGlass
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.UpiCyan
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
            .background(ObsidianBackground)
            .padding(horizontal = 14.dp)
            .testTag("upi_reconciliation_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x338B5CF6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = null,
                        tint = CosmicPurpleBright,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = StreetLedgerStrings.get("upi_reconciliation_title", language),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = StreetLedgerStrings.get("upi_sub", language),
                        fontSize = 11.sp,
                        color = TextLavender
                    )
                }
            }
        }

        // Summary Comparison Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(GlassBorderPurple, Color(0x158B5CF6))
                    )
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = StreetLedgerStrings.get("logged_upi_total", language),
                                fontSize = 12.sp,
                                color = TextLavender
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = Formatters.formatRupee(totalLoggedUpi),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CosmicPurpleBright
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (difference == 0.0) NeonMintGlow else Color(0x33FF5252),
                            modifier = Modifier.border(
                                1.dp,
                                if (difference == 0.0) NeonMint else ExpensePink,
                                RoundedCornerShape(12.dp)
                            )
                        ) {
                            Text(
                                text = if (difference == 0.0)
                                    "Balanced ✓"
                                else
                                    "Diff: ${Formatters.formatRupee(abs(difference))}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (difference == 0.0) NeonMint else ExpensePink,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Soundbox input field
                    OutlinedTextField(
                        value = soundboxInput,
                        onValueChange = { viewModel.updateSoundboxInput(it) },
                        label = { Text(StreetLedgerStrings.get("bank_soundbox_total", language), color = TextLavender) },
                        placeholder = { Text(Formatters.formatPlainAmount(totalLoggedUpi), color = TextMuted) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = CosmicPurpleBright
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("soundbox_input_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianSurface,
                            unfocusedContainerColor = ObsidianSurface,
                            focusedBorderColor = CosmicPurpleBright,
                            unfocusedBorderColor = Color(0x338B5CF6),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status Pill
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = if (difference == 0.0) Color(0x1A00F298) else Color(0x22FF5252),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (difference == 0.0) Color(0x4400F298) else Color(0x44FF5252)
                        )
                    ) {
                        Text(
                            text = if (difference == 0.0)
                                StreetLedgerStrings.get("all_balanced", language)
                            else
                                "Attention: Difference of ${Formatters.formatRupee(abs(difference))}. Check unverified entries below.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (difference == 0.0) NeonMint else ExpensePink,
                            modifier = Modifier.padding(12.dp)
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
                    color = TextWhite
                )

                if (upiTransactions.isNotEmpty() && verifiedCount < upiTransactions.size) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(NeonMint)
                            .clickable { viewModel.reconcileAllTodayUpi() }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = null,
                                tint = NeonMintDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = StreetLedgerStrings.get("reconcile_all", language),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonMintDark
                            )
                        }
                    }
                }
            }
        }

        // List of UPI transactions
        if (upiTransactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(GlassBorderPurple, Color(0x158B5CF6))
                        )
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No UPI payments recorded today yet.\nTransactions logged with '+ UPI In' appear here for verification.",
                            color = TextMuted,
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
            Spacer(modifier = Modifier.height(100.dp))
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (transaction.isUpiReconciled) ObsidianCard else Color(0xFF201636)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(
                    if (transaction.isUpiReconciled) GlassBorderPurple else Color(0x668B5CF6),
                    Color(0x108B5CF6)
                )
            )
        )
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
                        color = CosmicPurpleBright
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (transaction.isUpiReconciled) NeonMintGlow else Color(0x33FF9800)
                    ) {
                        Text(
                            text = if (transaction.isUpiReconciled)
                                StreetLedgerStrings.get("status_verified", language)
                            else
                                StreetLedgerStrings.get("status_unverified", language),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (transaction.isUpiReconciled) NeonMint else Color(0xFFFFB74D),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "${transaction.category} • ${Formatters.formatTime(transaction.timestamp)}",
                    fontSize = 12.sp,
                    color = TextLavender
                )
                if (transaction.note.isNotBlank()) {
                    Text(
                        text = transaction.note,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onToggleVerify() },
                shape = CircleShape,
                color = if (transaction.isUpiReconciled) NeonMintGlow else ObsidianGlass,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (transaction.isUpiReconciled) NeonMint else Color(0x448B5CF6)
                )
            ) {
                Icon(
                    imageVector = if (transaction.isUpiReconciled) Icons.Default.CheckCircle else Icons.Default.Pending,
                    contentDescription = "Toggle status",
                    tint = if (transaction.isUpiReconciled) NeonMint else TextMuted,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                )
            }
        }
    }
}
