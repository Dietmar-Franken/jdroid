package com.jdroid.android.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;

public abstract class ReadOnlyContentProvider extends ContentProvider {
	
	/**
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		return true;
	}
	
	/**
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(@NonNull Uri paramUri, ContentValues paramContentValues) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(@NonNull Uri paramUri, String paramString, String[] paramArrayOfString) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String,
	 *      java.lang.String[])
	 */
	@Override
	public int update(@NonNull Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(@NonNull Uri paramUri) {
		return null;
	}
}
