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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.KhataPartyEntity
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.ExpenseRedLight
import com.example.ui.theme.GreenIncome
import com.example.ui.theme.GreenIncomeLight
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealPrimary
import com.example.ui.viewmodel.StreetLedgerViewModel
import com.example.util.AppLanguage
import com.example.util.Formatters
import com.example.util.SocialSharing
import com.example.util.StreetLedgerStrings

@Composable
fun KhataScreen(
    viewModel: StreetLedgerViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsStateWithLifecycle()
    val customers by viewModel.customers.collectAsStateWithLifecycle()
    val suppliers by viewModel.suppliers.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Customers, 1: Suppliers
    var showAddDialog by remember { mutableStateOf(false) }

    // Dialog for Payments
    var partyForPayment by remember { mutableStateOf<KhataPartyEntity?>(null) }
    var isPaymentReceived by remember { mutableStateOf(true) } // true: Jama, false: Udhar
    var paymentAmountInput by remember { mutableStateOf("") }

    val totalReceivable = customers.sumOf { it.balanceDue }
    val totalPayable = suppliers.sumOf { it.balanceDue }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp)
                .testTag("khata_screen")
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Khata Title
            Text(
                text = StreetLedgerStrings.get("khata_title", language),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TealDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tab Selector
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
                            text = StreetLedgerStrings.get("tab_customers", language),
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = StreetLedgerStrings.get("tab_suppliers", language),
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Total Balance Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedTab == 0) GreenIncomeLight else ExpenseRedLight
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (selectedTab == 0)
                            StreetLedgerStrings.get("total_receivables", language)
                        else
                            StreetLedgerStrings.get("total_payables", language),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selectedTab == 0) GreenIncome else ExpenseRed
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = Formatters.formatRupee(if (selectedTab == 0) totalReceivable else totalPayable),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (selectedTab == 0) GreenIncome else ExpenseRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val currentList = if (selectedTab == 0) customers else suppliers

            if (currentList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedTab == 0)
                            "No customers added yet.\nTap '+' to add credit customers."
                        else
                            "No suppliers added yet.\nTap '+' to add wholesale suppliers.",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(currentList, key = { it.id }) { party ->
                        KhataPartyCard(
                            party = party,
                            isCustomer = selectedTab == 0,
                            language = language,
                            onReceivePayment = {
                                partyForPayment = party
                                isPaymentReceived = true
                                paymentAmountInput = ""
                            },
                            onGiveCredit = {
                                partyForPayment = party
                                isPaymentReceived = false
                                paymentAmountInput = ""
                            },
                            onSendReminder = {
                                SocialSharing.shareWhatsAppReminder(
                                    context = context,
                                    recipientName = party.name,
                                    phone = party.phone,
                                    amount = party.balanceDue,
                                    shopName = "Street Ledger Vendor",
                                    language = language
                                )
                            },
                            onDelete = { viewModel.deleteKhataParty(party) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // Floating Action Button to Add Customer/Supplier
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 80.dp)
                .testTag("fab_add_khata"),
            containerColor = TealPrimary,
            contentColor = Color.White
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }

    // Add Customer/Supplier Dialog
    if (showAddDialog) {
        var nameInput by remember { mutableStateOf("") }
        var phoneInput by remember { mutableStateOf("") }
        var initialBalanceInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = if (selectedTab == 0)
                        StreetLedgerStrings.get("add_customer", language)
                    else
                        StreetLedgerStrings.get("add_supplier", language),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text(StreetLedgerStrings.get("name", language)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text(StreetLedgerStrings.get("phone", language)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = initialBalanceInput,
                        onValueChange = { initialBalanceInput = it },
                        label = { Text(StreetLedgerStrings.get("initial_balance", language)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val initBal = initialBalanceInput.toDoubleOrNull() ?: 0.0
                        if (selectedTab == 0) {
                            viewModel.addCustomer(nameInput, phoneInput, initBal)
                        } else {
                            viewModel.addSupplier(nameInput, phoneInput, initBal)
                        }
                        showAddDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text(StreetLedgerStrings.get("save", language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(StreetLedgerStrings.get("cancel", language))
                }
            }
        )
    }

    // Payment / Credit Adjustment Dialog
    partyForPayment?.let { party ->
        AlertDialog(
            onDismissRequest = { partyForPayment = null },
            title = {
                Text(
                    text = if (isPaymentReceived)
                        "${StreetLedgerStrings.get("receive_payment", language)}: ${party.name}"
                    else
                        "${StreetLedgerStrings.get("give_credit", language)}: ${party.name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "Current Balance: ${Formatters.formatRupee(party.balanceDue)}",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = paymentAmountInput,
                        onValueChange = { paymentAmountInput = it },
                        label = { Text("Amount (₹)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = paymentAmountInput.toDoubleOrNull() ?: 0.0
                        if (amt > 0) {
                            if (selectedTab == 0) {
                                if (isPaymentReceived) {
                                    viewModel.receiveCustomerPayment(party, amt)
                                } else {
                                    viewModel.giveCustomerCredit(party, amt)
                                }
                            } else {
                                if (isPaymentReceived) {
                                    viewModel.paySupplierDues(party, amt)
                                } else {
                                    viewModel.addSupplierCredit(party, amt)
                                }
                            }
                        }
                        partyForPayment = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPaymentReceived) GreenIncome else ExpenseRed
                    )
                ) {
                    Text(StreetLedgerStrings.get("save", language))
                }
            },
            dismissButton = {
                TextButton(onClick = { partyForPayment = null }) {
                    Text(StreetLedgerStrings.get("cancel", language))
                }
            }
        )
    }
}

@Composable
fun KhataPartyCard(
    party: KhataPartyEntity,
    isCustomer: Boolean,
    language: AppLanguage,
    onReceivePayment: () -> Unit,
    onGiveCredit: () -> Unit,
    onSendReminder: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("khata_party_${party.id}"),
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
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isCustomer) GreenIncomeLight else Color(0xFFECEFF1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isCustomer) Icons.Default.Person else Icons.Default.Store,
                            contentDescription = null,
                            tint = if (isCustomer) GreenIncome else TealDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = party.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E2825)
                        )
                        if (party.phone.isNotBlank()) {
                            Text(
                                text = party.phone,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = Formatters.formatRupee(party.balanceDue),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isCustomer) GreenIncome else ExpenseRed
                    )
                    Text(
                        text = if (isCustomer) "You'll Get" else "You'll Pay",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons Row: [Receive (Jama)] [Give Credit] [WhatsApp Reminder] [Delete]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onReceivePayment,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isCustomer) "Jama (Recv)" else "Pay Due",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenIncome
                    )
                }

                OutlinedButton(
                    onClick = onGiveCredit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isCustomer) "+ Udhar" else "+ Bill",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ExpenseRed
                    )
                }

                if (isCustomer && party.balanceDue > 0) {
                    IconButton(
                        onClick = onSendReminder,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE8F5E9), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "WhatsApp Reminder",
                            tint = Color(0xFF25D366),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
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
