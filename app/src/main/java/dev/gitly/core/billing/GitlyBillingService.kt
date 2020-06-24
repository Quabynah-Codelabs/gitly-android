package dev.gitly.core.billing

import android.app.Activity
import com.android.billingclient.api.*
import dev.gitly.debugPrint
import dev.gitly.debugger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GitlyBillingService constructor(private val context: Activity) {

    private val skuType = BillingClient.SkuType.INAPP
    private var skuDetails = mutableListOf<SkuDetails>()
    private var purchaseHistoryRecordList = mutableListOf<PurchaseHistoryRecord>()
    private val purchaseUpdateListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
                debugger("Cancelled by user")
            } else {
                // Handle any other error codes.
                debugger("An error occurred")
            }
        }

    private fun handlePurchase(purchase: Purchase?) {
        debugger("Purchase handling -> ${purchase?.originalJson}")
    }

    private val billingClient = BillingClient.newBuilder(context)
        .setListener(purchaseUpdateListener)
        .enablePendingPurchases()
        .build()

    private suspend fun queryPurchaseHistory() {
        if (!billingClient.isReady) return

        val queryPurchaseHistory =
            withContext(Dispatchers.IO) { billingClient.queryPurchaseHistory(skuType) }

        queryPurchaseHistory.billingResult.debugMessage.debugPrint()

        // Process the result.
        if (queryPurchaseHistory.billingResult.responseCode == BillingClient.BillingResponseCode.OK)
            purchaseHistoryRecordList =
                queryPurchaseHistory.purchaseHistoryRecordList?.toMutableList() ?: mutableListOf()
    }

    private suspend fun querySkuDetails() {
        if (!billingClient.isReady) return

        val skuList = ArrayList<String>()
        skuList.add("premium_upgrade")
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(skuList)
            .setType(skuType)

        val skuDetailsResult = withContext(Dispatchers.IO) {
            billingClient.querySkuDetails(params.build())
        }
        skuDetailsResult.billingResult.debugMessage.debugPrint()

        // Process the result.
        if (skuDetailsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK)
            skuDetails = skuDetailsResult.skuDetailsList?.toMutableList() ?: mutableListOf()
    }

    init {

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    GlobalScope.launch(Dispatchers.IO) {
                        querySkuDetails()
                        queryPurchaseHistory()
                    }
                    debugger("Query purchases here -> $skuDetails")
                    debugger("History purchases here -> $purchaseHistoryRecordList")
                    if (skuDetails.isEmpty()) return
                    val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetails.first())
                        .build()
                    val responseCode = billingClient.launchBillingFlow(context, flowParams)
                    responseCode.debugMessage.debugPrint()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

}