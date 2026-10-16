package com.example.data.repository

import com.example.data.local.InventoryDao
import com.example.data.local.KhataDao
import com.example.data.local.SavingsGoalDao
import com.example.data.local.TransactionDao
import com.example.data.model.InventoryItemEntity
import com.example.data.model.KhataPartyEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class StreetLedgerRepository(
    private val transactionDao: TransactionDao,
    private val khataDao: KhataDao,
    private val inventoryDao: InventoryDao,
    private val savingsGoalDao: SavingsGoalDao
) {
    // Transactions
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val upiTransactions: Flow<List<TransactionEntity>> = transactionDao.getUpiTransactions()

    fun getTodayTransactions(): Flow<List<TransactionEntity>> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = calendar.timeInMillis

        return transactionDao.getTransactionsForDay(startOfDay, endOfDay)
    }

    suspend fun addTransaction(transaction: TransactionEntity): Long {
        return transactionDao.insertTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun toggleUpiReconciled(id: Long, reconciled: Boolean) {
        transactionDao.setUpiReconciled(id, reconciled)
    }

    suspend fun reconcileAllTodayUpi() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfDay = calendar.timeInMillis

        transactionDao.reconcileAllUpiForDay(startOfDay, endOfDay)
    }

    // Khata / Debt
    val allKhataParties: Flow<List<KhataPartyEntity>> = khataDao.getAllParties()

    fun getCustomers(): Flow<List<KhataPartyEntity>> = khataDao.getPartiesByType("CUSTOMER")
    fun getSuppliers(): Flow<List<KhataPartyEntity>> = khataDao.getPartiesByType("SUPPLIER")

    suspend fun addKhataParty(party: KhataPartyEntity): Long = khataDao.insertParty(party)

    suspend fun updateKhataBalance(id: Long, newBalance: Double) {
        khataDao.updateBalance(id, newBalance)
    }

    suspend fun deleteKhataParty(party: KhataPartyEntity) {
        khataDao.deleteParty(party)
    }

    // Inventory
    val allInventory: Flow<List<InventoryItemEntity>> = inventoryDao.getAllInventory()

    suspend fun addInventoryItem(item: InventoryItemEntity): Long = inventoryDao.insertItem(item)

    suspend fun updateStockQuantity(id: Long, newQuantity: Double) {
        inventoryDao.updateQuantity(id, newQuantity)
    }

    suspend fun deleteInventoryItem(item: InventoryItemEntity) {
        inventoryDao.deleteItem(item)
    }

    // Savings Goals
    val allGoals: Flow<List<SavingsGoalEntity>> = savingsGoalDao.getAllGoals()

    suspend fun addGoal(goal: SavingsGoalEntity): Long = savingsGoalDao.insertGoal(goal)

    suspend fun updateGoalSavedAmount(id: Long, newAmount: Double) {
        savingsGoalDao.updateSavedAmount(id, newAmount)
    }

    suspend fun deleteGoal(goal: SavingsGoalEntity) {
        savingsGoalDao.deleteGoal(goal)
    }
}
