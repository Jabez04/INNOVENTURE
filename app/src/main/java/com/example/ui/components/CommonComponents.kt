package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransactionEntity
import com.example.ui.theme.CosmicPurpleBright
import com.example.ui.theme.CosmicPurpleDeep
import com.example.ui.theme.CosmicPurpleGlow
import com.example.ui.theme.CosmicPurplePrimary
import com.example.ui.theme.ExpensePink
import com.example.ui.theme.GlassBorderPurple
import com.example.ui.theme.GlowPillBorder
import com.example.ui.theme.NeonMint
import com.example.ui.theme.NeonMintDark
import com.example.ui.theme.NeonMintGlow
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardElevated
import com.example.ui.theme.ObsidianGlass
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.UpiCyan
import com.example.ui.viewmodel.TodayBusinessSummary
import com.example.util.AppLanguage
import com.example.util.Formatters
import com.example.util.Haptics
import com.example.util.StreetLedgerStrings

@Composable
fun BusinessSummaryCard(
    summary: TodayBusinessSummary,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("business_summary_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(GlassBorderPurple, Color(0x158B5CF6))
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = StreetLedgerStrings.get("business_today", language),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                // Luminous Profit Badge
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (summary.netProfit >= 0) NeonMintGlow else Color(0x33FF5252),
                    modifier = Modifier.border(
                        1.dp,
                        if (summary.netProfit >= 0) NeonMint else ExpensePink,
                        RoundedCornerShape(14.dp)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (summary.netProfit >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = if (summary.netProfit >= 0) NeonMint else ExpensePink,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (summary.netProfit >= 0) {
                                "${StreetLedgerStrings.get("net_profit", language)}: ${Formatters.formatRupee(summary.netProfit)}"
                            } else {
                                "${StreetLedgerStrings.get("net_loss", language)}: ${Formatters.formatRupee(-summary.netProfit)}"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (summary.netProfit >= 0) NeonMint else ExpensePink
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2 Columns: Total Income vs Total Expense
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Income Glass Card
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = ObsidianCardElevated
                ) {
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                            .border(1.dp, Color(0x2200F298), RoundedCornerShape(16.dp))
                            .padding(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = null,
                                tint = NeonMint,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = StreetLedgerStrings.get("today_income", language),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextLavender
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = Formatters.formatRupee(summary.totalIncome),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonMint
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Cash: ${Formatters.formatRupee(summary.cashIncome)}",
                                fontSize = 10.sp,
                                color = TextLavender
                            )
                            Text("•", fontSize = 10.sp, color = TextMuted)
                            Text(
                                text = "UPI: ${Formatters.formatRupee(summary.upiIncome)}",
                                fontSize = 10.sp,
                                color = CosmicPurpleGlow
                            )
                        }
                    }
                }

                // Expense Glass Card
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = ObsidianCardElevated
                ) {
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                            .border(1.dp, Color(0x22FF5252), RoundedCornerShape(16.dp))
                            .padding(2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = ExpensePink,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = StreetLedgerStrings.get("today_expenses", language),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextLavender
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = Formatters.formatRupee(summary.totalExpense),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ExpensePink
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${summary.transactionCount} transactions",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TactileNumpad(
    amount: String,
    language: AppLanguage,
    onDigit: (String) -> Unit,
    onIncrement: (Int) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit,
    onCashIn: () -> Unit,
    onUpiIn: () -> Unit,
    onExpenseOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tactile_numpad_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(GlassBorderPurple, Color(0x158B5CF6))
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Amount Display
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("amount_display_surface"),
                shape = RoundedCornerShape(16.dp),
                color = ObsidianSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderPurple)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonMint
                    )
                    Text(
                        text = if (amount.isEmpty() || amount == "0") "0" else amount,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (amount == "0") TextMuted else TextWhite,
                        modifier = Modifier.testTag("amount_text")
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Chips (+10, +20, +50, +100, +500)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val increments = listOf(10, 20, 50, 100, 500)
                increments.forEach { inc ->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                Haptics.tick(context)
                                onIncrement(inc)
                            }
                            .testTag("chip_add_$inc"),
                        color = ObsidianGlass,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x338B5CF6))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "+₹$inc",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CosmicPurpleBright
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3x4 Number Grid
            val rows = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("C", "0", "⌫")
            )

            rows.forEach { rowList ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowList.forEach { key ->
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    Haptics.tick(context)
                                    when (key) {
                                        "C" -> onClear()
                                        "⌫" -> onBackspace()
                                        else -> onDigit(key)
                                    }
                                }
                                .testTag("num_key_$key"),
                            color = when (key) {
                                "C" -> Color(0x33FF5252)
                                "⌫" -> Color(0x228B5CF6)
                                else -> ObsidianGlass
                            },
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                when (key) {
                                    "C" -> Color(0x66FF5252)
                                    "⌫" -> Color(0x448B5CF6)
                                    else -> Color(0x22FFFFFF)
                                }
                            )
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (key == "⌫") {
                                    Icon(
                                        imageVector = Icons.Default.Backspace,
                                        contentDescription = "Backspace",
                                        tint = TextLavender,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Text(
                                        text = key,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (key) {
                                            "C" -> ExpensePink
                                            else -> TextWhite
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Glowing Capsule Action Buttons: + Cash In, + UPI In, - Expense
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Cash In Button (Neon Mint Pill)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(NeonMint)
                        .clickable {
                            Haptics.tick(context)
                            onCashIn()
                        }
                        .testTag("btn_cash_in"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = null,
                            tint = NeonMintDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = StreetLedgerStrings.get("action_cash_in", language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonMintDark
                        )
                    }
                }

                // UPI In Button (Glowing Cosmic Purple Pill)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(CosmicPurpleDeep, CosmicPurplePrimary)
                            )
                        )
                        .border(1.dp, CosmicPurpleBright, RoundedCornerShape(26.dp))
                        .clickable {
                            Haptics.tick(context)
                            onUpiIn()
                        }
                        .testTag("btn_upi_in"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = null,
                            tint = TextWhite,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = StreetLedgerStrings.get("action_upi_in", language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                // Expense Out Button (Dark Coral Glow Pill)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(Color(0x33FF5252))
                        .border(1.5.dp, ExpensePink, RoundedCornerShape(26.dp))
                        .clickable {
                            Haptics.tick(context)
                            onExpenseOut()
                        }
                        .testTag("btn_expense_out"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = ExpensePink,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = StreetLedgerStrings.get("action_expense_out", language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ExpensePink
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItemRow(
    transaction: TransactionEntity,
    language: AppLanguage,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tx_item_${transaction.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(Color(0x338B5CF6), Color(0x108B5CF6))
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                transaction.type == "EXPENSE" -> Color(0x33FF5252)
                                transaction.paymentMode == "UPI" -> Color(0x338B5CF6)
                                else -> NeonMintGlow
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when {
                            transaction.type == "EXPENSE" -> Icons.Default.ArrowUpward
                            transaction.paymentMode == "UPI" -> Icons.Default.QrCode2
                            else -> Icons.Default.Payments
                        },
                        contentDescription = null,
                        tint = when {
                            transaction.type == "EXPENSE" -> ExpensePink
                            transaction.paymentMode == "UPI" -> CosmicPurpleBright
                            else -> NeonMint
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = transaction.category,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (transaction.paymentMode == "UPI") Color(0x338B5CF6) else NeonMintGlow
                        ) {
                            Text(
                                text = transaction.paymentMode,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (transaction.paymentMode == "UPI") CosmicPurpleBright else NeonMint,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    if (transaction.note.isNotBlank()) {
                        Text(
                            text = transaction.note,
                            fontSize = 12.sp,
                            color = TextLavender
                        )
                    }
                    Text(
                        text = Formatters.formatTime(transaction.timestamp),
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (transaction.type == "INCOME") "+${Formatters.formatRupee(transaction.amount)}" else "-${Formatters.formatRupee(transaction.amount)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (transaction.type == "INCOME") NeonMint else ExpensePink
                )
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
