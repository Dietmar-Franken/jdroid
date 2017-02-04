package com.jdroid.android.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final Logger LOGGER = LoggerUtils.getLogger(RecyclerViewAdapter.class);

	private Map<Integer, RecyclerViewType> recyclerViewTypeMap = Maps.newHashMap();

	private HeaderRecyclerViewType.HeaderItem headerItem;
	private List<Object> items;
	private FooterRecyclerViewType.FooterItem footerItem;

	private int selectedItemPosition = RecyclerView.NO_POSITION;
	private RecyclerViewType.RecyclerViewTypeListener recyclerViewTypeListener = new RecyclerViewType.RecyclerViewTypeListener() {
		@Override
		public void onItemSelected(int position) {
			setSelectedItem(position);
		}
	};

	public RecyclerViewAdapter(RecyclerViewType recyclerViewType) {
		this(Lists.newArrayList(recyclerViewType), Lists.newArrayList());
	}

	public RecyclerViewAdapter(RecyclerViewType recyclerViewType, List<? extends Object> items) {
		this(Lists.newArrayList(recyclerViewType), items);
	}

	public RecyclerViewAdapter(List<RecyclerViewType> recyclerViewTypes) {
		this(recyclerViewTypes, Lists.newArrayList());
	}

	public RecyclerViewAdapter(List<RecyclerViewType> recyclerViewTypes, List<? extends Object> items) {
		this.items = (List<Object>)items;
		for (RecyclerViewType each : recyclerViewTypes) {
			addRecyclerViewType(each);
		}
	}

	public void addHeader(final @LayoutRes int headerResId) {
		addHeader(new HeaderRecyclerViewType() {
			@Override
			protected Integer getLayoutResourceId() {
				return headerResId;
			}

			@Override
			public AbstractRecyclerFragment getAbstractRecyclerFragment() {
				return null;
			}
		});
	}

	public void addHeader(HeaderRecyclerViewType headerRecyclerViewType) {
		Boolean add = headerItem == null;
		removeHeader();
		addRecyclerViewType(headerRecyclerViewType);
		headerItem = new HeaderRecyclerViewType.HeaderItem();
		if (add) {
			notifyItemInserted(0);
		} else {
			notifyItemChanged(0);
		}
	}

	public void removeHeader() {
		for (Map.Entry<Integer, RecyclerViewType> entry : recyclerViewTypeMap.entrySet()) {
			if (entry.getValue() instanceof HeaderRecyclerViewType) {
				recyclerViewTypeMap.put(entry.getKey(), null);
				break;
			}
		}
		if (headerItem != null) {
			headerItem = null;
			notifyItemRemoved(0);
		}
	}

	public void addFooter(final @LayoutRes int footerResId) {
		addFooter(new FooterRecyclerViewType() {
			@Override
			protected Integer getLayoutResourceId() {
				return footerResId;
			}

			@Override
			public AbstractRecyclerFragment getAbstractRecyclerFragment() {
				return null;
			}
		});
	}

	public void addFooter(FooterRecyclerViewType footerRecyclerViewType) {
		Boolean add = footerItem == null;
		removeFooter();
		addRecyclerViewType(footerRecyclerViewType);
		footerItem = new FooterRecyclerViewType.FooterItem();
		if (add) {
			notifyItemInserted(getItemCount() - 1);
		} else {
			notifyItemChanged(getItemCount() - 1);
		}
	}

	public void removeFooter() {
		for (Map.Entry<Integer, RecyclerViewType> entry : recyclerViewTypeMap.entrySet()) {
			if (entry.getValue() instanceof FooterRecyclerViewType) {
				recyclerViewTypeMap.put(entry.getKey(), null);
				break;
			}
		}
		if (footerItem != null) {
			footerItem = null;
			notifyItemRemoved(getItemCount() - 1);
		}
	}

	public int addRecyclerViewType(RecyclerViewType recyclerViewType) {
		recyclerViewType.setRecyclerViewTypeListener(recyclerViewTypeListener);
		int viewType = recyclerViewTypeMap.size() + 1;
		recyclerViewTypeMap.put(viewType, recyclerViewType);
		return viewType;
	}

	@Override
	public int getItemViewType(int position) {
		Object item = getItem(position);
		for (Map.Entry<Integer, RecyclerViewType> entry : recyclerViewTypeMap.entrySet()) {
			if (entry.getValue() != null && entry.getValue().matchViewType(item)) {
				return entry.getKey();
			}
		}
		LOGGER.warn("ViewType not found for item " + item);
		return -1;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		RecyclerViewType recyclerViewType = recyclerViewTypeMap.get(viewType);
		View view = recyclerViewType.inflateView(inflater, parent);
		return recyclerViewType.createViewHolderFromView(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		Integer itemViewType = holder.getItemViewType();
		Object item = getItem(position);
		RecyclerViewType recyclerViewType = recyclerViewTypeMap.get(itemViewType);

		if (recyclerViewType.isClickable()) {
			holder.itemView.setOnClickListener(recyclerViewType);
		} else {
			holder.itemView.setOnClickListener(null);
		}

		if (recyclerViewType.isSelectable()){
			holder.itemView.setSelected(holder.getLayoutPosition() == selectedItemPosition);
		}

		recyclerViewType.fillHolderFromItem(item, holder);
	}

	@Override
	public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
		super.onViewDetachedFromWindow(holder);

		// The view could be reused while an animation is been happening.
		// In order to avoid that is recommendable to clear the animation when is detached.
		holder.itemView.clearAnimation();
	}

	@Override
	public int getItemCount() {
		return items.size() + (headerItem != null ? 1 : 0) + (footerItem != null ? 1 : 0);
	}

	public <T> void addItem(T item) {
		items.add(item);
		notifyItemInserted(getItemCount() - 1);
	}

	public <T> void addItems(List<T> newItems) {
		items.addAll(newItems);
		notifyItemRangeInserted(getItemCount() - newItems.size(), newItems.size());
	}

	public <T> void removeItem(T item) {
		if (item.equals(headerItem)) {
			removeHeader();
		} else if (item.equals(footerItem)) {
			removeFooter();
		} else {
			int pos = getPosition(item);
			items.remove(item);
			notifyItemRemoved(pos);
		}
	}

	public void removeItemByPosition(int position) {
		Object item = getItem(position);
		if (item.equals(headerItem)) {
			removeHeader();
		} else if (item.equals(footerItem)) {
			removeFooter();
		} else {
			int pos = items.indexOf(item);
			pos = headerItem != null ? pos + 1 : pos;
			items.remove(item);
			notifyItemRemoved(pos);
		}
	}

	public void clear() {
		int size = getItemCount();
		headerItem = null;
		footerItem = null;
		items.clear();
		notifyItemRangeRemoved(0, size);
	}

	public List<Object> getItems() {
		return items;
	}

	public Object getItem(Integer position) {
		Object item;
		if (headerItem != null && position == 0) {
			item = headerItem;
		} else if (footerItem != null && position == getItemCount() - 1) {
			item = footerItem;
		} else {
			if (headerItem != null && position != 0) {
				position = position - 1;
			}
			item = items.get(position);
		}
		return item;
	}

	public <T> int getPosition(T item) {
		if (item.equals(headerItem)) {
			return 0;
		} else if (item.equals(footerItem)) {
			return getItemCount() - 1;
		} else {
			int pos = items.indexOf(item);
			return headerItem != null ? pos + 1 : pos;
		}
	}

	public void setSelectedItem(int position) {
		int itemViewType = getItemViewType(position);
		RecyclerViewType recyclerViewType = recyclerViewTypeMap.get(itemViewType);
		if (recyclerViewType.isSelectable()) {
			int previousSelectedItemPosition = selectedItemPosition;
			selectedItemPosition = position;
			notifyItemChanged(previousSelectedItemPosition);
			notifyItemChanged(selectedItemPosition);
		}
	}

	public int getSelectedItemPosition() {
		return selectedItemPosition;
	}
}
