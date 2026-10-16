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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ReceiptLong
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BusinessSummaryCard
import com.example.ui.components.TactileNumpad
import com.example.ui.components.TransactionItemRow
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealPrimary
import com.example.ui.viewmodel.StreetLedgerViewModel
import com.example.util.AppLanguage
import com.example.util.StreetLedgerStrings

@Composable
fun DashboardScreen(
    viewModel: StreetLedgerViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val todaySummary by viewModel.todaySummary.collectAsStateWithLifecycle()
    val todayTransactions by viewModel.todayTransactions.collectAsStateWithLifecycle()
    val numpadAmount by viewModel.numpadAmount.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val noteInput by viewModel.noteInput.collectAsStateWithLifecycle()

    val categories = listOf(
        "General" to if (language == AppLanguage.TAMIL) "பொதுவானது" else "General",
        "Tea/Snacks" to if (language == AppLanguage.TAMIL) "டீ / சிற்றுண்டி" else "Tea/Snacks",
        "Raw Materials" to if (language == AppLanguage.TAMIL) "சரக்கு பொருட்கள்" else "Raw Materials",
        "Gas & Fuel" to if (language == AppLanguage.TAMIL) "எரிவாயு / சிலிண்டர்" else "Gas & Fuel",
        "Transport" to if (language == AppLanguage.TAMIL) "வண்டி வாடகை" else "Transport",
        "Stall Fee" to if (language == AppLanguage.TAMIL) "கடை கட்டணம்" else "Stall Fee",
        "Personal" to if (language == AppLanguage.TAMIL) "வீட்டு செலவு" else "Personal"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Welcome Greeting & Tagline
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                Text(
                    text = StreetLedgerStrings.get("welcome_greeting", language),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealDark
                )
                Text(
                    text = StreetLedgerStrings.get("tagline", language),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
        }

        // Financial Summary Card ("Your Business Today")
        item {
            BusinessSummaryCard(
                summary = todaySummary,
                language = language
            )
        }

        // Quick Category Picker
        item {
            Column {
                Text(
                    text = StreetLedgerStrings.get("category", language),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { (key, label) ->
                        val isSelected = selectedCategory == key
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { viewModel.selectCategory(key) }
                                .testTag("cat_$key"),
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) TealPrimary else Color.White,
                            shadowElevation = if (isSelected) 2.dp else 1.dp
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color.DarkGray,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                            )
                        }
                    }
                }
            }
        }

        // Big Tactile Numpad
        item {
            TactileNumpad(
                amount = numpadAmount,
                language = language,
                onDigit = { viewModel.onNumpadDigit(it) },
                onIncrement = { viewModel.onNumpadAddIncrement(it) },
                onBackspace = { viewModel.onNumpadBackspace() },
                onClear = { viewModel.onNumpadClear() },
                onCashIn = { viewModel.recordCashIncome() },
                onUpiIn = { viewModel.recordUpiIncome() },
                onExpenseOut = { viewModel.recordExpense() }
            )
        }

        // Optional Note Input
        item {
            OutlinedTextField(
                value = noteInput,
                onValueChange = { viewModel.updateNoteInput(it) },
                placeholder = {
                    Text(
                        text = StreetLedgerStrings.get("note_optional", language),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("note_input_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = TealPrimary,
                    unfocusedBorderColor = Color(0xFFDCE6E2)
                ),
                singleLine = true
            )
        }

        // Today's Activity Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = StreetLedgerStrings.get("today_activity", language),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealDark
                )
                Text(
                    text = "${todayTransactions.size} entries",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        if (todayTransactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = StreetLedgerStrings.get("no_transactions_yet", language),
                            fontSize = 13.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(todayTransactions, key = { it.id }) { tx ->
                TransactionItemRow(
                    transaction = tx,
                    language = language,
                    onDelete = { viewModel.deleteTransaction(tx) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp)) // Space for bottom bar
        }
    }
}
