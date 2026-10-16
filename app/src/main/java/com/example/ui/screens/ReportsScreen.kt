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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.SavingsGoalEntity
import com.example.ui.theme.CosmicPurpleBright
import com.example.ui.theme.CosmicPurpleDeep
import com.example.ui.theme.CosmicPurpleGlow
import com.example.ui.theme.CosmicPurplePrimary
import com.example.ui.theme.ExpensePink
import com.example.ui.theme.GlassBorderPurple
import com.example.ui.theme.NeonMint
import com.example.ui.theme.NeonMintDark
import com.example.ui.theme.NeonMintGlow
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardElevated
import com.example.ui.theme.ObsidianGlass
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
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

    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddGoalDialog by remember { mutableStateOf(false) }

    var goalForDeposit by remember { mutableStateOf<SavingsGoalEntity?>(null) }
    var depositInput by remember { mutableStateOf("") }

    val allIncome = allTransactions.filter { it.type == "INCOME" }.sumOf { it.amount }
    val allExpense = allTransactions.filter { it.type == "EXPENSE" }.sumOf { it.amount }
    val netOverallProfit = allIncome - allExpense

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
            .background(ObsidianBackground)
            .padding(horizontal = 14.dp)
            .testTag("reports_screen"),
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
                        imageVector = Icons.Default.Insights,
                        contentDescription = null,
                        tint = CosmicPurpleBright,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = StreetLedgerStrings.get("reports_title", language),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }
        }

        // Sub-tabs
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = ObsidianCard,
                contentColor = NeonMint,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = NeonMint,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, GlassBorderPurple, RoundedCornerShape(14.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = StreetLedgerStrings.get("tab_summary", language),
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) NeonMint else TextLavender
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
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) NeonMint else TextLavender
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
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 2) NeonMint else TextLavender
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> {
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
                            Text(
                                text = "Today's Net Profit",
                                fontSize = 13.sp,
                                color = TextLavender
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = Formatters.formatRupee(todaySummary.netProfit),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (todaySummary.netProfit >= 0) NeonMint else ExpensePink
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Today Income", fontSize = 11.sp, color = TextLavender)
                                    Text(
                                        Formatters.formatRupee(todaySummary.totalIncome),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonMint
                                    )
                                }
                                Column {
                                    Text("Today Expenses", fontSize = 11.sp, color = TextLavender)
                                    Text(
                                        Formatters.formatRupee(todaySummary.totalExpense),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ExpensePink
                                    )
                                }
                                Column {
                                    Text("Profit Margin", fontSize = 11.sp, color = TextLavender)
                                    val margin = if (todaySummary.totalIncome > 0)
                                        (todaySummary.netProfit / todaySummary.totalIncome) * 100
                                    else 0.0
                                    Text(
                                        "${String.format("%.0f", margin)}%",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CosmicPurpleBright
                                    )
                                }
                            }
                        }
                    }
                }

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
                            Text(
                                text = "All-Time Ledger Health",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Income:", fontSize = 13.sp, color = TextLavender)
                                Text(
                                    Formatters.formatRupee(allIncome),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonMint
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Expenses:", fontSize = 13.sp, color = TextLavender)
                                Text(
                                    Formatters.formatRupee(allExpense),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ExpensePink
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            val incomeRatio = if (allIncome + allExpense > 0) (allIncome / (allIncome + allExpense)).toFloat() else 0.5f
                            LinearProgressIndicator(
                                progress = { incomeRatio },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = NeonMint,
                                trackColor = Color(0x44FF5252)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Cumulative Profit: ${Formatters.formatRupee(netOverallProfit)} across ${allTransactions.size} records",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            1 -> {
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = CosmicPurpleBright,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = StreetLedgerStrings.get("peak_time_analysis", language),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            val morningPct = (morningIncome / totalTimeIncome).toFloat()
                            DemandTimeRow(
                                title = StreetLedgerStrings.get("morning_rush", language),
                                amount = morningIncome,
                                percentage = (morningPct * 100).toInt(),
                                progress = morningPct,
                                color = Color(0xFFFFB74D)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            val afternoonPct = (afternoonIncome / totalTimeIncome).toFloat()
                            DemandTimeRow(
                                title = StreetLedgerStrings.get("afternoon_calm", language),
                                amount = afternoonIncome,
                                percentage = (afternoonPct * 100).toInt(),
                                progress = afternoonPct,
                                color = CosmicPurpleBright
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            val eveningPct = (eveningIncome / totalTimeIncome).toFloat()
                            DemandTimeRow(
                                title = StreetLedgerStrings.get("evening_peak", language),
                                amount = eveningIncome,
                                percentage = (eveningPct * 100).toInt(),
                                progress = eveningPct,
                                color = NeonMint
                            )
                        }
                    }
                }
            }

            2 -> {
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
                            color = TextWhite
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(NeonMint)
                                .clickable { showAddGoalDialog = true }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = NeonMintDark, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(StreetLedgerStrings.get("add_goal", language), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonMintDark)
                            }
                        }
                    }
                }

                if (savingsGoals.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = ObsidianCard)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No savings targets added yet.\nSet a goal for festival stock or new stall equipment!",
                                    color = TextMuted,
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
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    // Add Goal Dialog
    if (showAddGoalDialog) {
        var titleEnInput by remember { mutableStateOf("") }
        var titleTaInput by remember { mutableStateOf("") }
        var targetAmountInput by remember { mutableStateOf("") }

        AlertDialog(
            containerColor = ObsidianCardElevated,
            titleContentColor = TextWhite,
            textContentColor = TextLavender,
            onDismissRequest = { showAddGoalDialog = false },
            title = {
                Text(StreetLedgerStrings.get("add_goal", language), fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = titleEnInput,
                        onValueChange = { titleEnInput = it },
                        label = { Text("Goal Title (English)", color = TextLavender) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianSurface,
                            unfocusedContainerColor = ObsidianSurface,
                            focusedBorderColor = CosmicPurpleBright,
                            unfocusedBorderColor = Color(0x338B5CF6),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = titleTaInput,
                        onValueChange = { titleTaInput = it },
                        label = { Text("நோக்கம் (தமிழ்)", color = TextLavender) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianSurface,
                            unfocusedContainerColor = ObsidianSurface,
                            focusedBorderColor = CosmicPurpleBright,
                            unfocusedBorderColor = Color(0x338B5CF6),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = targetAmountInput,
                        onValueChange = { targetAmountInput = it },
                        label = { Text(StreetLedgerStrings.get("target_amount", language), color = TextLavender) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianSurface,
                            unfocusedContainerColor = ObsidianSurface,
                            focusedBorderColor = CosmicPurpleBright,
                            unfocusedBorderColor = Color(0x338B5CF6),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
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
                    colors = ButtonDefaults.buttonColors(containerColor = NeonMint, contentColor = NeonMintDark)
                ) {
                    Text(StreetLedgerStrings.get("save", language), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddGoalDialog = false }) {
                    Text(StreetLedgerStrings.get("cancel", language), color = TextLavender)
                }
            }
        )
    }

    // Deposit to Goal Dialog
    goalForDeposit?.let { goal ->
        AlertDialog(
            containerColor = ObsidianCardElevated,
            titleContentColor = TextWhite,
            textContentColor = TextLavender,
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
                        color = TextLavender
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = depositInput,
                        onValueChange = { depositInput = it },
                        label = { Text("Deposit Amount (₹)", color = TextLavender) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ObsidianSurface,
                            unfocusedContainerColor = ObsidianSurface,
                            focusedBorderColor = CosmicPurpleBright,
                            unfocusedBorderColor = Color(0x338B5CF6),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
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
                    colors = ButtonDefaults.buttonColors(containerColor = NeonMint, contentColor = NeonMintDark)
                ) {
                    Text(StreetLedgerStrings.get("save", language), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { goalForDeposit = null }) {
                    Text(StreetLedgerStrings.get("cancel", language), color = TextLavender)
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
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextLavender)
            Text(
                "${Formatters.formatRupee(amount)} ($percentage%)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color(0x228B5CF6)
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
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(GlassBorderPurple, Color(0x108B5CF6))
            )
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NeonMintGlow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = NeonMint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = displayTitle,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Target: ${Formatters.formatRupee(goal.targetAmount)}",
                            fontSize = 12.sp,
                            color = TextLavender
                        )
                    }
                }

                Text(
                    text = "${(progress * 100).toInt()}%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NeonMint
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = NeonMint,
                trackColor = Color(0x228B5CF6)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Saved: ${Formatters.formatRupee(goal.savedAmount)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CosmicPurpleBright
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonMintGlow)
                            .border(1.dp, NeonMint, RoundedCornerShape(8.dp))
                            .clickable { onDeposit() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("+ Add Money", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonMint)
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
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
}
