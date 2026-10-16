package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransactionEntity
import com.example.ui.theme.CashGold
import com.example.ui.theme.CashGoldLight
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.ExpenseRedLight
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.GreenIncomeLight
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.UpiBlue
import com.example.ui.theme.UpiBlueLight
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
                    color = TealDark
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (summary.netProfit >= 0) GreenIncomeLight else ExpenseRedLight,
                    modifier = Modifier.padding(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (summary.netProfit >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = if (summary.netProfit >= 0) GreenIncome else ExpenseRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (summary.netProfit >= 0) {
                                "${StreetLedgerStrings.get("net_profit", language)}: ${Formatters.formatRupee(summary.netProfit)}"
                            } else {
                                "${StreetLedgerStrings.get("net_loss", language)}: ${Formatters.formatRupee(-summary.netProfit)}"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (summary.netProfit >= 0) GreenIncome else ExpenseRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2 Columns: Total Income vs Total Expense
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Income Card
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    color = GreenIncomeLight
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = null,
                                tint = GreenIncome,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = StreetLedgerStrings.get("today_income", language),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = GreenIncome
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = Formatters.formatRupee(summary.totalIncome),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GreenIncome
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        // Cash vs UPI Pills
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Cash: ${Formatters.formatRupee(summary.cashIncome)}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2E5E36)
                            )
                            Text(
                                text = "|",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "UPI: ${Formatters.formatRupee(summary.upiIncome)}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = UpiBlue
                            )
                        }
                    }
                }

                // Expense Card
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    color = ExpenseRedLight
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = ExpenseRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = StreetLedgerStrings.get("today_expenses", language),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ExpenseRed
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = Formatters.formatRupee(summary.totalExpense),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ExpenseRed
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${summary.transactionCount} transactions today",
                            fontSize = 10.sp,
                            color = Color.DarkGray
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF1F6F4)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealDark
                    )
                    Text(
                        text = if (amount.isEmpty() || amount == "0") "0" else amount,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (amount == "0") Color.Gray else TealPrimary,
                        modifier = Modifier.testTag("amount_text")
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Amount Increment Chips (+10, +20, +50, +100, +500)
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
                        color = Color(0xFFE8F2EF),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "+₹$inc",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealDark
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
                                .clip(RoundedCornerShape(12.dp))
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
                                "C" -> Color(0xFFFFECEB)
                                "⌫" -> Color(0xFFF0F3F2)
                                else -> Color(0xFFF7FAF9)
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (key == "⌫") {
                                    Icon(
                                        imageVector = Icons.Default.Backspace,
                                        contentDescription = "Backspace",
                                        tint = Color.DarkGray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Text(
                                        text = key,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (key == "C") ExpenseRed else Color(0xFF263238)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Big Action Buttons: + Cash In, + UPI In, - Expense
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Cash In Button
                Button(
                    onClick = {
                        Haptics.tick(context)
                        onCashIn()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("btn_cash_in"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenIncome)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = StreetLedgerStrings.get("action_cash_in", language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // UPI In Button
                Button(
                    onClick = {
                        Haptics.tick(context)
                        onUpiIn()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("btn_upi_in"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UpiBlue)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = StreetLedgerStrings.get("action_upi_in", language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Expense Out Button
                Button(
                    onClick = {
                        Haptics.tick(context)
                        onExpenseOut()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("btn_expense_out"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ExpenseRed)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = StreetLedgerStrings.get("action_expense_out", language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Payment Mode Badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                transaction.type == "EXPENSE" -> ExpenseRedLight
                                transaction.paymentMode == "UPI" -> UpiBlueLight
                                else -> GreenIncomeLight
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
                            transaction.type == "EXPENSE" -> ExpenseRed
                            transaction.paymentMode == "UPI" -> UpiBlue
                            else -> GreenIncome
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
                            color = Color(0xFF1E2825)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (transaction.paymentMode == "UPI") UpiBlueLight else CashGoldLight
                        ) {
                            Text(
                                text = transaction.paymentMode,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (transaction.paymentMode == "UPI") UpiBlue else CashGold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    if (transaction.note.isNotBlank()) {
                        Text(
                            text = transaction.note,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Text(
                        text = Formatters.formatTime(transaction.timestamp),
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (transaction.type == "INCOME") "+${Formatters.formatRupee(transaction.amount)}" else "-${Formatters.formatRupee(transaction.amount)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (transaction.type == "INCOME") GreenIncome else ExpenseRed
                )
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.LightGray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
