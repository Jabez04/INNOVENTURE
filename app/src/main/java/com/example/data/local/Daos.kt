package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.InventoryItemEntity
import com.example.data.model.KhataPartyEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE timestamp >= :startOfDay AND timestamp <= :endOfDay ORDER BY timestamp DESC")
    fun getTransactionsForDay(startOfDay: Long, endOfDay: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE paymentMode = 'UPI' ORDER BY timestamp DESC")
    fun getUpiTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("UPDATE transactions SET isUpiReconciled = :reconciled WHERE id = :id")
    suspend fun setUpiReconciled(id: Long, reconciled: Boolean)

    @Query("UPDATE transactions SET isUpiReconciled = 1 WHERE paymentMode = 'UPI' AND timestamp >= :startOfDay AND timestamp <= :endOfDay")
    suspend fun reconcileAllUpiForDay(startOfDay: Long, endOfDay: Long)
}

@Dao
interface KhataDao {
    @Query("SELECT * FROM khata_parties ORDER BY lastUpdated DESC")
    fun getAllParties(): Flow<List<KhataPartyEntity>>

    @Query("SELECT * FROM khata_parties WHERE partyType = :partyType ORDER BY lastUpdated DESC")
    fun getPartiesByType(partyType: String): Flow<List<KhataPartyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParty(party: KhataPartyEntity): Long

    @Update
    suspend fun updateParty(party: KhataPartyEntity)

    @Delete
    suspend fun deleteParty(party: KhataPartyEntity)

    @Query("UPDATE khata_parties SET balanceDue = :newBalance, lastUpdated = :timestamp WHERE id = :id")
    suspend fun updateBalance(id: Long, newBalance: Double, timestamp: Long = System.currentTimeMillis())
}

@Dao
interface InventoryDao {
    @Query("SELECT * FROM inventory_items ORDER BY id ASC")
    fun getAllInventory(): Flow<List<InventoryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: InventoryItemEntity): Long

    @Update
    suspend fun updateItem(item: InventoryItemEntity)

    @Delete
    suspend fun deleteItem(item: InventoryItemEntity)

    @Query("UPDATE inventory_items SET quantity = :newQty WHERE id = :id")
    suspend fun updateQuantity(id: Long, newQty: Double)
}

@Dao
interface SavingsGoalDao {
    @Query("SELECT * FROM savings_goals ORDER BY id ASC")
    fun getAllGoals(): Flow<List<SavingsGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: SavingsGoalEntity): Long

    @Update
    suspend fun updateGoal(goal: SavingsGoalEntity)

    @Delete
    suspend fun deleteGoal(goal: SavingsGoalEntity)

    @Query("UPDATE savings_goals SET savedAmount = :newAmount WHERE id = :id")
    suspend fun updateSavedAmount(id: Long, newAmount: Double)
}
