package com.example.fitnessapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.fitnessapp.models.AccountEntity

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAccount(account: AccountEntity)

    @Query("SELECT * FROM account_details WHERE id = 1 LIMIT 1")
    suspend fun getAccountDetails(): AccountEntity?

    @Query("UPDATE account_details SET profilePhotoUri = :photoUri WHERE id = 1")
    suspend fun updateProfilePhoto(photoUri: String): Int // Returns affected rows

    @Transaction
    suspend fun insertOrUpdatePhoto(photoUri: String) {
        val affectedRows = updateProfilePhoto(photoUri)
        if (affectedRows == 0) {
            insertOrUpdateAccount(AccountEntity(
                id = 1,
                name = "",
                gender = "",
                goalWeight = 0.0,
                goalDate = "",
                heightFeet = 0,
                heightInches = 0,
                maxWeight = 0.0,
                minWeight = 0.0,
                profilePhotoUri = photoUri
            ))
        }
    }
}

