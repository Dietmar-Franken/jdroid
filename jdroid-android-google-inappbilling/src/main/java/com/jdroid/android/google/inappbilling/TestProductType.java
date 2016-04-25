package com.jdroid.android.google.inappbilling;

import com.jdroid.android.google.inappbilling.Product.ItemType;

public enum TestProductType implements ProductType {
	
	// When you make an in-app billing request with this product ID, the Google Play app responds as though you
	// successfully purchased an item. The response includes a JSON string, which contains fake purchase information
	// (for example, a fake order ID). In some cases, the JSON string is signed and the response includes the signature
	// so you can test your signature verification implementation using these responses.
	PURCHASED("android.test.purchased"),
	
	// When you make an in-app billing request with this product ID the Google Play app responds as though the purchase
	// was canceled. This can occur when an error is encountered in the order process, such as an invalid credit card,
	// or when you cancel a user's order before it is charged.
	CANCELED("android.test.canceled"),
	
	// When you make an in-app billing request with this product ID, the Google Play app responds as though the purchase
	// was refunded. Refunds cannot be initiated through the Google Play app's in-app billing service. Refunds must be
	// initiated by you (the merchant). After you process a refund request through your Google Checkout account, a
	// refund message is sent to your application by the Google Play app. This occurs only when the Google Play app gets
	// notification from Google Checkout that a refund has been made. For more information about refunds, see Handling
	// IN_APP_NOTIFY messages and In-app Billing Pricing.
	REFUNDED("android.test.refunded"),
	
	// When you make an in-app billing request with this product ID, the Google Play app responds as though the item
	// being purchased was not listed in your application's product list.
	UNAVAILABLE("android.test.item_unavailable");
	
	private String productId;
	
	TestProductType(String productId) {
		this.productId = productId;
	}
	
	/**
	 * @see com.jdroid.android.google.inappbilling.ProductType#getProductId()
	 */
	@Override
	public String getProductId() {
		return productId;
	}
	
	/**
	 * @see com.jdroid.android.google.inappbilling.ProductType#isConsumable()
	 */
	@Override
	public Boolean isConsumable() {
		return false;
	}
	
	/**
	 * @see com.jdroid.android.google.inappbilling.ProductType#getTitleId()
	 */
	@Override
	public Integer getTitleId() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.google.inappbilling.ProductType#getDescriptionId()
	 */
	@Override
	public Integer getDescriptionId() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.google.inappbilling.ProductType#getItemType()
	 */
	@Override
	public ItemType getItemType() {
		return ItemType.MANAGED;
	}
}
