package com.jdroid.android.view;

import java.util.Date;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import com.jdroid.android.fragment.DatePickerDialogFragment;
import com.jdroid.android.utils.AndroidDateUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class DateEditText extends EditText {
	
	/**
	 * @param context
	 * @param attrs
	 */
	public DateEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void init(final Fragment fragment, final Date defaultDate) {
		setDate(defaultDate);
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatePickerDialogFragment.show(fragment, defaultDate);
			}
		});
		setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					DatePickerDialogFragment.show(fragment, defaultDate);
				}
			}
		});
		setLongClickable(false);
	}
	
	public void setDate(Date date) {
		setText(AndroidDateUtils.formatDate(date));
	}
}
