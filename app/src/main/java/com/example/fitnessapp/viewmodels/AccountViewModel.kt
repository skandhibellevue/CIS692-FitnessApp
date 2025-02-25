package com.example.fitnessapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.FitnessApp
import com.example.fitnessapp.models.AccountEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val database = FitnessApp.database
    private val accountDao = database.accountDao()

    private var _accountDetails = MutableStateFlow<AccountEntity?>(null)
    var accountDetails: StateFlow<AccountEntity?> = _accountDetails


    init {
        fetchAccountDetails()
    }

    private fun fetchAccountDetails() {
        viewModelScope.launch {
            val account = accountDao.getAccountDetails()
            _accountDetails.value = account
        }
    }

    fun saveAccountDetails(account: AccountEntity) {
        viewModelScope.launch {
            accountDao.insertOrUpdateAccount(account)
            _accountDetails.value = account
        }
    }
}
