package com.rau.cafecampus

import android.app.Application
import com.rau.cafecampus.data.local.CafeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CafeApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { CafeDatabase.getDatabase(this, applicationScope) }
}
