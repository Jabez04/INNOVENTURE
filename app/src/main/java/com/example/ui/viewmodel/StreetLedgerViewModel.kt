package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.StreetLedgerDatabase
import com.example.data.model.InventoryItemEntity
import com.example.data.model.KhataPartyEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.TransactionEntity
import com.example.data.repository.StreetLedgerRepository
import com.example.util.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TodayBusinessSummary(
    val totalIncome: Double = 0.0,
    val cashIncome: Double = 0.0,
    val upiIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val netProfit: Double = 0.0,
    val transactionCount: Int = 0
)

class StreetLedgerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StreetLedgerRepository

    init {
        val database = StreetLedgerDatabase.getDatabase(application, viewModelScope)
        repository = StreetLedgerRepository(
            transactionDao = database.transactionDao(),
            khataDao = database.khataDao(),
            inventoryDao = database.inventoryDao(),
            savingsGoalDao = database.savingsGoalDao()
        )
    }

    // Language State
    private val _language = MutableStateFlow(AppLanguage.ENGLISH)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.ENGLISH) AppLanguage.TAMIL else AppLanguage.ENGLISH
    }

    // Navigation Tab (0: Dashboard, 1: Khata, 2: UPI, 3: Inventory, 4: Reports)
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    fun selectTab(index: Int) {
        _currentTab.value = index
    }

    // Flow Data
    val todayTransactions: StateFlow<List<TransactionEntity>> = repository.getTodayTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customers: StateFlow<List<KhataPartyEntity>> = repository.getCustomers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val suppliers: StateFlow<List<KhataPartyEntity>> = repository.getSuppliers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inventoryItems: StateFlow<List<InventoryItemEntity>> = repository.allInventory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savingsGoals: StateFlow<List<SavingsGoalEntity>> = repository.allGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Financial calculations for "Your Business Today"
    val todaySummary: StateFlow<TodayBusinessSummary> = todayTransactions.combine(_language) { list, _ ->
        var cashIn = 0.0
        var upiIn = 0.0
        var expense = 0.0

        for (tx in list) {
            if (tx.type == "INCOME") {
                if (tx.paymentMode == "CASH") {
                    cashIn += tx.amount
                } else {
                    upiIn += tx.amount
                }
            } else if (tx.type == "EXPENSE") {
                expense += tx.amount
            }
        }
        val totalInc = cashIn + upiIn
        val profit = totalInc - expense
        TodayBusinessSummary(
            totalIncome = totalInc,
            cashIncome = cashIn,
            upiIncome = upiIn,
            totalExpense = expense,
            netProfit = profit,
            transactionCount = list.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodayBusinessSummary())

    // Numpad Entry State
    private val _numpadAmount = MutableStateFlow("0")
    val numpadAmount: StateFlow<String> = _numpadAmount.asStateFlow()

    private val _selectedCategory = MutableStateFlow("General")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _noteInput = MutableStateFlow("")
    val noteInput: StateFlow<String> = _noteInput.asStateFlow()

    fun onNumpadDigit(digit: String) {
        val current = _numpadAmount.value
        if (current == "0" && digit != "0" && digit != "00") {
            _numpadAmount.value = digit
        } else if (current != "0") {
            if (current.length < 7) { // Support up to ₹99,99,999
                _numpadAmount.value = current + digit
            }
        }
    }

    fun onNumpadAddIncrement(increment: Int) {
        val currentVal = _numpadAmount.value.toDoubleOrNull() ?: 0.0
        val newVal = currentVal + increment
        _numpadAmount.value = newVal.toInt().toString()
    }

    fun onNumpadBackspace() {
        val current = _numpadAmount.value
        if (current.length > 1) {
            _numpadAmount.value = current.substring(0, current.length - 1)
        } else {
            _numpadAmount.value = "0"
        }
    }

    fun onNumpadClear() {
        _numpadAmount.value = "0"
        _noteInput.value = ""
    }

    fun selectCategory(cat: String) {
        _selectedCategory.value = cat
    }

    fun updateNoteInput(note: String) {
        _noteInput.value = note
    }

    fun recordCashIncome() {
        val amount = _numpadAmount.value.toDoubleOrNull() ?: 0.0
        if (amount > 0) {
            viewModelScope.launch {
                repository.addTransaction(
                    TransactionEntity(
                        type = "INCOME",
                        paymentMode = "CASH",
                        amount = amount,
                        category = _selectedCategory.value,
                        note = _noteInput.value.trim(),
                        timestamp = System.currentTimeMillis()
                    )
                )
                onNumpadClear()
            }
        }
    }

    fun recordUpiIncome() {
        val amount = _numpadAmount.value.toDoubleOrNull() ?: 0.0
        if (amount > 0) {
            viewModelScope.launch {
                repository.addTransaction(
                    TransactionEntity(
                        type = "INCOME",
                        paymentMode = "UPI",
                        amount = amount,
                        category = _selectedCategory.value,
                        note = _noteInput.value.trim(),
                        timestamp = System.currentTimeMillis(),
                        isUpiReconciled = false
                    )
                )
                onNumpadClear()
            }
        }
    }

    fun recordExpense() {
        val amount = _numpadAmount.value.toDoubleOrNull() ?: 0.0
        if (amount > 0) {
            viewModelScope.launch {
                repository.addTransaction(
                    TransactionEntity(
                        type = "EXPENSE",
                        paymentMode = "CASH",
                        amount = amount,
                        category = if (_selectedCategory.value == "General") "Raw Materials" else _selectedCategory.value,
                        note = _noteInput.value.trim(),
                        timestamp = System.currentTimeMillis()
                    )
                )
                onNumpadClear()
            }
        }
    }

    fun deleteTransaction(tx: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(tx)
        }
    }

    // UPI Reconciliation
    private val _soundboxActualInput = MutableStateFlow("")
    val soundboxActualInput: StateFlow<String> = _soundboxActualInput.asStateFlow()

    fun updateSoundboxInput(text: String) {
        _soundboxActualInput.value = text
    }

    fun toggleUpiVerification(id: Long, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleUpiReconciled(id, !currentStatus)
        }
    }

    fun reconcileAllTodayUpi() {
        viewModelScope.launch {
            repository.reconcileAllTodayUpi()
        }
    }

    // Khata
    fun addCustomer(name: String, phone: String, initialCredit: Double) {
        if (name.isNotBlank()) {
            viewModelScope.launch {
                repository.addKhataParty(
                    KhataPartyEntity(
                        name = name.trim(),
                        phone = phone.trim(),
                        partyType = "CUSTOMER",
                        balanceDue = initialCredit,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun addSupplier(name: String, phone: String, initialCredit: Double) {
        if (name.isNotBlank()) {
            viewModelScope.launch {
                repository.addKhataParty(
                    KhataPartyEntity(
                        name = name.trim(),
                        phone = phone.trim(),
                        partyType = "SUPPLIER",
                        balanceDue = initialCredit,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun receiveCustomerPayment(party: KhataPartyEntity, amount: Double) {
        if (amount > 0) {
            viewModelScope.launch {
                val newBal = maxOf(0.0, party.balanceDue - amount)
                repository.updateKhataBalance(party.id, newBal)
                // Also log as Cash Income
                repository.addTransaction(
                    TransactionEntity(
                        type = "INCOME",
                        paymentMode = "CASH",
                        amount = amount,
                        category = "Khata Jama",
                        note = "Payment from ${party.name}",
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun giveCustomerCredit(party: KhataPartyEntity, amount: Double) {
        if (amount > 0) {
            viewModelScope.launch {
                val newBal = party.balanceDue + amount
                repository.updateKhataBalance(party.id, newBal)
            }
        }
    }

    fun paySupplierDues(party: KhataPartyEntity, amount: Double) {
        if (amount > 0) {
            viewModelScope.launch {
                val newBal = maxOf(0.0, party.balanceDue - amount)
                repository.updateKhataBalance(party.id, newBal)
                // Also log as Expense
                repository.addTransaction(
                    TransactionEntity(
                        type = "EXPENSE",
                        paymentMode = "CASH",
                        amount = amount,
                        category = "Supplier Khata",
                        note = "Paid to ${party.name}",
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun addSupplierCredit(party: KhataPartyEntity, amount: Double) {
        if (amount > 0) {
            viewModelScope.launch {
                val newBal = party.balanceDue + amount
                repository.updateKhataBalance(party.id, newBal)
            }
        }
    }

    fun deleteKhataParty(party: KhataPartyEntity) {
        viewModelScope.launch {
            repository.deleteKhataParty(party)
        }
    }

    // Inventory
    fun addInventoryItem(
        nameEn: String,
        nameTa: String,
        quantity: Double,
        unit: String,
        threshold: Double,
        cost: Double,
        supplier: String
    ) {
        viewModelScope.launch {
            repository.addInventoryItem(
                InventoryItemEntity(
                    nameEn = nameEn.trim(),
                    nameTa = nameTa.trim(),
                    quantity = quantity,
                    unit = unit,
                    minThreshold = threshold,
                    unitCost = cost,
                    supplierName = supplier.trim()
                )
            )
        }
    }

    fun adjustStock(item: InventoryItemEntity, delta: Double) {
        val newQty = maxOf(0.0, item.quantity + delta)
        viewModelScope.launch {
            repository.updateStockQuantity(item.id, newQty)
        }
    }

    fun deleteInventoryItem(item: InventoryItemEntity) {
        viewModelScope.launch {
            repository.deleteInventoryItem(item)
        }
    }

    // Savings Goals
    fun addSavingsGoal(titleEn: String, titleTa: String, target: Double) {
        viewModelScope.launch {
            repository.addGoal(
                SavingsGoalEntity(
                    titleEn = titleEn.trim(),
                    titleTa = titleTa.trim(),
                    targetAmount = target,
                    savedAmount = 0.0
                )
            )
        }
    }

    fun depositToSavingsGoal(goal: SavingsGoalEntity, amount: Double) {
        if (amount > 0) {
            val newAmount = goal.savedAmount + amount
            viewModelScope.launch {
                repository.updateGoalSavedAmount(goal.id, newAmount)
            }
        }
    }

    fun deleteSavingsGoal(goal: SavingsGoalEntity) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }
}
