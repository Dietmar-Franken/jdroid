package com.jdroid.android.google.inappbilling;

import com.jdroid.java.exception.ErrorCodeException;

public interface InAppBillingClientListener {
	
	/**
	 * Called to notify that setup is complete.
	 */
	public void onSetupFinished();
	
	/**
	 * Called to notify that setup failed.
	 * 
	 * @param errorCodeException The result of the setup process.
	 */
	public void onSetupFailed(ErrorCodeException errorCodeException);
	
	/**
	 * Called to notify that an inventory query operation completed.
	 * 
	 * @param inventory The inventory.
	 */
	public void onQueryInventoryFinished(Inventory inventory);
	
	/**
	 * Called to notify that an inventory query operation failed.
	 * 
	 * @param errorCodeException The result of the operation.
	 */
	public void onQueryInventoryFailed(ErrorCodeException errorCodeException);
	
	/**
	 * Called to notify that an in-app purchase finished.
	 * 
	 * @param product The {@link Product} purchased
	 */
	public void onPurchaseFinished(Product product);
	
	/**
	 * Called to notify that an in-app purchase failed.
	 * 
	 * @param errorCodeException The result of the purchase.
	 */
	public void onPurchaseFailed(ErrorCodeException errorCodeException);
	
	/**
	 * Called to notify that a consumption has finished.
	 * 
	 * @param product The {@link Product} that was (or was to be) consumed.
	 */
	public void onConsumeFinished(Product product);
	
	/**
	 * Called to notify that a consumption has failed.
	 * 
	 * @param errorCodeException The result of the consumption operation.
	 */
	public void onConsumeFailed(ErrorCodeException errorCodeException);
	
}
