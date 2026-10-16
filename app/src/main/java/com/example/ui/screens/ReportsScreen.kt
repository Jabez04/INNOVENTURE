package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.SavingsGoalEntity
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.ExpenseRedLight
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.GreenIncomeLight
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealPrimary
import com.example.ui.viewmodel.StreetLedgerViewModel
import com.example.util.AppLanguage
import com.example.util.Formatters
import com.example.util.StreetLedgerStrings
import java.util.Calendar

@Composable
fun ReportsScreen(
    viewModel: StreetLedgerViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val allTransactions by viewModel.allTransactions.collectAsStateWithLifecycle()
    val todaySummary by viewModel.todaySummary.collectAsStateWithLifecycle()
    val savingsGoals by viewModel.savingsGoals.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Summary, 1: Demand Hours, 2: Savings
    var showAddGoalDialog by remember { mutableStateOf(false) }

    var goalForDeposit by remember { mutableStateOf<SavingsGoalEntity?>(null) }
    var depositInput by remember { mutableStateOf("") }

    // Aggregate stats
    val allIncome = allTransactions.filter { it.type == "INCOME" }.sumOf { it.amount }
    val allExpense = allTransactions.filter { it.type == "EXPENSE" }.sumOf { it.amount }
    val netOverallProfit = allIncome - allExpense

    // Peak demand hour bucketing
    val morningIncome = remember(allTransactions) {
        allTransactions.filter {
            it.type == "INCOME" && Calendar.getInstance().apply { timeInMillis = it.timestamp }.get(Calendar.HOUR_OF_DAY) in 6..10
        }.sumOf { it.amount }
    }
    val afternoonIncome = remember(allTransactions) {
        allTransactions.filter {
            it.type == "INCOME" && Calendar.getInstance().apply { timeInMillis = it.timestamp }.get(Calendar.HOUR_OF_DAY) in 11..15
        }.sumOf { it.amount }
    }
    val eveningIncome = remember(allTransactions) {
        allTransactions.filter {
            it.type == "INCOME" && Calendar.getInstance().apply { timeInMillis = it.timestamp }.get(Calendar.HOUR_OF_DAY) in 16..22
        }.sumOf { it.amount }
    }
    val totalTimeIncome = maxOf(1.0, morningIncome + afternoonIncome + eveningIncome)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("reports_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0F2F1)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Insights,
                        contentDescription = null,
                        tint = TealDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = StreetLedgerStrings.get("reports_title", language),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealDark
                    )
                }
            }
        }

        // Sub-tabs: P&L Summary, Demand Hours, Savings
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = TealPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = StreetLedgerStrings.get("tab_summary", language),
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = StreetLedgerStrings.get("tab_peak_demand", language),
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = StreetLedgerStrings.get("tab_savings", language),
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                // Tab 0: P&L Summary
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Today's Net Profit",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = Formatters.formatRupee(todaySummary.netProfit),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (todaySummary.netProfit >= 0) GreenIncome else ExpenseRed
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Today Income", fontSize = 11.sp, color = Color.Gray)
                                    Text(
                                        Formatters.formatRupee(todaySummary.totalIncome),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GreenIncome
                                    )
                                }
                                Column {
                                    Text("Today Expenses", fontSize = 11.sp, color = Color.Gray)
                                    Text(
                                        Formatters.formatRupee(todaySummary.totalExpense),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ExpenseRed
                                    )
                                }
                                Column {
                                    Text("Profit Margin", fontSize = 11.sp, color = Color.Gray)
                                    val margin = if (todaySummary.totalIncome > 0)
                                        (todaySummary.netProfit / todaySummary.totalIncome) * 100
                                    else 0.0
                                    Text(
                                        "${String.format("%.0f", margin)}%",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TealPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "All-Time Ledger Health",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealDark
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Income:", fontSize = 13.sp, color = Color.DarkGray)
                                Text(
                                    Formatters.formatRupee(allIncome),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenIncome
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Expenses:", fontSize = 13.sp, color = Color.DarkGray)
                                Text(
                                    Formatters.formatRupee(allExpense),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ExpenseRed
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            // Ratio progress bar
                            val incomeRatio = if (allIncome + allExpense > 0) (allIncome / (allIncome + allExpense)).toFloat() else 0.5f
                            LinearProgressIndicator(
                                progress = { incomeRatio },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = GreenIncome,
                                trackColor = ExpenseRedLight
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Cumulative Profit: ${Formatters.formatRupee(netOverallProfit)} across ${allTransactions.size} records",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            1 -> {
                // Tab 1: Demand Hours
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = TealPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = StreetLedgerStrings.get("peak_time_analysis", language),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TealDark
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Morning Rush
                            val morningPct = (morningIncome / totalTimeIncome).toFloat()
                            DemandTimeRow(
                                title = StreetLedgerStrings.get("morning_rush", language),
                                amount = morningIncome,
                                percentage = (morningPct * 100).toInt(),
                                progress = morningPct,
                                color = Color(0xFFE65100)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Afternoon Calm
                            val afternoonPct = (afternoonIncome / totalTimeIncome).toFloat()
                            DemandTimeRow(
                                title = StreetLedgerStrings.get("afternoon_calm", language),
                                amount = afternoonIncome,
                                percentage = (afternoonPct * 100).toInt(),
                                progress = afternoonPct,
                                color = TealPrimary
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Evening Peak
                            val eveningPct = (eveningIncome / totalTimeIncome).toFloat()
                            DemandTimeRow(
                                title = StreetLedgerStrings.get("evening_peak", language),
                                amount = eveningIncome,
                                percentage = (eveningPct * 100).toInt(),
                                progress = eveningPct,
                                color = GreenIncome
                            )
                        }
                    }
                }
            }

            2 -> {
                // Tab 2: Savings Goals
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = StreetLedgerStrings.get("tab_savings", language),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealDark
                        )
                        Button(
                            onClick = { showAddGoalDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(StreetLedgerStrings.get("add_goal", language), fontSize = 11.sp)
                        }
                    }
                }

                if (savingsGoals.isEmpty()) {
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
                                    text = "No savings targets added yet.\nSet a goal for festival stock or new stall equipment!",
                                    color = Color.Gray,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                } else {
                    items(savingsGoals, key = { it.id }) { goal ->
                        SavingsGoalCard(
                            goal = goal,
                            language = language,
                            onDeposit = {
                                goalForDeposit = goal
                                depositInput = ""
                            },
                            onDelete = { viewModel.deleteSavingsGoal(goal) }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Add Goal Dialog
    if (showAddGoalDialog) {
        var titleEnInput by remember { mutableStateOf("") }
        var titleTaInput by remember { mutableStateOf("") }
        var targetAmountInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddGoalDialog = false },
            title = {
                Text(StreetLedgerStrings.get("add_goal", language), fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = titleEnInput,
                        onValueChange = { titleEnInput = it },
                        label = { Text("Goal Title (English)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = titleTaInput,
                        onValueChange = { titleTaInput = it },
                        label = { Text("நோக்கம் (தமிழ்)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = targetAmountInput,
                        onValueChange = { targetAmountInput = it },
                        label = { Text(StreetLedgerStrings.get("target_amount", language)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val target = targetAmountInput.toDoubleOrNull() ?: 1000.0
                        viewModel.addSavingsGoal(
                            titleEn = if (titleEnInput.isNotBlank()) titleEnInput else titleTaInput,
                            titleTa = if (titleTaInput.isNotBlank()) titleTaInput else titleEnInput,
                            target = target
                        )
                        showAddGoalDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text(StreetLedgerStrings.get("save", language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddGoalDialog = false }) {
                    Text(StreetLedgerStrings.get("cancel", language))
                }
            }
        )
    }

    // Deposit to Goal Dialog
    goalForDeposit?.let { goal ->
        AlertDialog(
            onDismissRequest = { goalForDeposit = null },
            title = {
                Text(
                    text = "${StreetLedgerStrings.get("deposit_savings", language)}: ${goal.titleEn}",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Current: ${Formatters.formatRupee(goal.savedAmount)} / ${Formatters.formatRupee(goal.targetAmount)}",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = depositInput,
                        onValueChange = { depositInput = it },
                        label = { Text("Deposit Amount (₹)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = depositInput.toDoubleOrNull() ?: 0.0
                        if (amt > 0) {
                            viewModel.depositToSavingsGoal(goal, amt)
                        }
                        goalForDeposit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenIncome)
                ) {
                    Text(StreetLedgerStrings.get("save", language))
                }
            },
            dismissButton = {
                TextButton(onClick = { goalForDeposit = null }) {
                    Text(StreetLedgerStrings.get("cancel", language))
                }
            }
        )
    }
}

@Composable
fun DemandTimeRow(
    title: String,
    amount: Double,
    percentage: Int,
    progress: Float,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            Text(
                "${Formatters.formatRupee(amount)} ($percentage%)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color(0xFFF0F0F0)
        )
    }
}

@Composable
fun SavingsGoalCard(
    goal: SavingsGoalEntity,
    language: AppLanguage,
    onDeposit: () -> Unit,
    onDelete: () -> Unit
) {
    val progress = if (goal.targetAmount > 0) (goal.savedAmount / goal.targetAmount).toFloat() else 0f
    val displayTitle = if (language == AppLanguage.TAMIL && goal.titleTa.isNotBlank()) goal.titleTa else goal.titleEn

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("savings_goal_${goal.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = GreenIncome,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = displayTitle,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E2825)
                        )
                        Text(
                            text = "Target: ${Formatters.formatRupee(goal.targetAmount)}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Text(
                    text = "${(progress * 100).toInt()}%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GreenIncome
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = GreenIncome,
                trackColor = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Saved: ${Formatters.formatRupee(goal.savedAmount)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TealDark
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = onDeposit,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenIncome)
                    ) {
                        Text("+ Add Money", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
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
}
