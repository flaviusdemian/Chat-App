package com.example.demoflavius.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.demoflavius.R;
import com.example.demoflavius.data.ConversationMessage;
import com.example.demoflavius.util.NotificationService;
import com.example.demoflavius.viewholders.BubbleChatViewHolder;


public class BubbleChatAdapter extends ArrayAdapter<ConversationMessage> 
{

	private ArrayList<ConversationMessage> dataSource;
	private int row;
	private LinearLayout wrapper;

	public BubbleChatAdapter(Context context, int resource, ArrayList<ConversationMessage> dataSource) {
		super(context, resource, dataSource);
		this.row = resource;
		this.dataSource = dataSource;
	}

	public ConversationMessage getItem(int index) {
		return this.dataSource.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		try
		{
			BubbleChatViewHolder holder;
			if (view == null) 
			{
				LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(row, parent, false);
//				view = inflater.inflate(row, null);
				
				holder = new BubbleChatViewHolder();
				view.setTag(holder);
			} 
			else 
			{
				holder = (BubbleChatViewHolder) view.getTag();
			}
		
			wrapper = (LinearLayout) view.findViewById(R.id.wrapper);

			if (dataSource == null || (position + 1) > dataSource.size())
				return view;
			
			ConversationMessage item = dataSource.get(position);
			
			//holder.iv_user = (ImageView) view.findViewById(R.id.tv_activity_chat_sender);
			holder.tv_content = (TextView) view.findViewById(R.id.tv_activity_chat_content);
			holder.tv_content.setText(item.getContent());
			holder.tv_content.setBackgroundResource(item.isLeft() ? R.drawable.linkedtranslators_bubbleyellow : R.drawable.linkedtranslators_bubblegreen);
			
			wrapper.setGravity(item.isLeft() ? Gravity.LEFT : Gravity.RIGHT);

		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("Failed at bubble chat adapter");
			ex.printStackTrace();
		}
		return view;
	}

	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}
	
	@Override
	public int getCount()
	{
		return dataSource.size();
	}

	public void setDataSource(ArrayList<ConversationMessage> dataSource) {
		this.dataSource = dataSource;
		notifyDataSetChanged();
	}
	
	public ArrayList<ConversationMessage> getDataSource()
	{
		return dataSource;
	}
}
