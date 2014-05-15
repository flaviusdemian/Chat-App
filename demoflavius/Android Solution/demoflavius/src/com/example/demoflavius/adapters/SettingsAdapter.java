package com.example.demoflavius.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demoflavius.R;
import com.example.demoflavius.data.Section;
import com.example.demoflavius.data.SectionItem;
import com.example.demoflavius.util.NotificationService;


public class SettingsAdapter extends BaseExpandableListAdapter 
{
	private Context context;
	private ArrayList<Section> dataSource;
	private Section currentItem;
	private int row;
	private LayoutInflater inflater;

	public SettingsAdapter(Context context, int resource, ArrayList<Section> arrayList) 
	{
		this.context = context;
		this.row = resource;
		this.dataSource = arrayList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return dataSource.get(groupPosition).getSectionItems().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return dataSource.get(groupPosition).getSectionItems().get(childPosition).getId();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return dataSource.get(groupPosition).getSectionItems().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.dataSource.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.dataSource.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		try
		{
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.activity_settings_section_view, parent, false);
			}

			TextView textView = (TextView) convertView.findViewById(R.id.settings_section_title);
			if( textView != null)
			{
				textView.setText(((Section) getGroup(groupPosition)).getTitle());
			}
			return convertView;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			NotificationService.showDebugCrouton("failed at getGroupView at Settings adapter");
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_settings_section_item, parent, false);
		}
		try
		{
			SectionItem oSectionItem = this.dataSource.get(groupPosition).getSectionItems().get(childPosition);
			
			TextView textView = (TextView) convertView.findViewById(R.id.settings_sectionitem_label);
			textView.setText(oSectionItem.getTitle());

			final ImageView itemIcon = (ImageView) convertView.findViewById(R.id.settings_sectionitem_icon);
			itemIcon.setImageDrawable(getDrawableByName(oSectionItem.getIcon(), context));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			NotificationService.showDebugCrouton("failed at getChildView at Settings adapter");
		}
		return convertView;
	}

	public static Drawable getDrawableByName( String name, Context context ) {
		try
		{
			int drawableResource = context.getResources().getIdentifier(name,"drawable", context.getPackageName());
			if ( drawableResource == 0 ) {
				throw new RuntimeException("Can't find drawable with name: " + name );
			}
			return context.getResources().getDrawable(drawableResource);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			NotificationService.showDebugCrouton("failed at getDrawableByName at Settings adapter");
		}
		return null;
	}
}
