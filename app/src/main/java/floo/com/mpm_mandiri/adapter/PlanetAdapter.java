/*
 * Copyright (C) 2012 jfrankie (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package floo.com.mpm_mandiri.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import floo.com.mpm_mandiri.R;

public class PlanetAdapter extends ArrayAdapter<TaskCalendar> implements Filterable {

	private List<TaskCalendar> planetList;
	private Context context;
	private Filter planetFilter;
	private List<TaskCalendar> origPlanetList;
	
	public PlanetAdapter(List<TaskCalendar> planetList, Context ctx) {
		super(ctx, R.layout.list_row_task, planetList);
		this.planetList = planetList;
		this.context = ctx;
		this.origPlanetList = planetList;
	}
	
	public int getCount() {
		return planetList.size();
	}

	public TaskCalendar getItem(int position) {
		return planetList.get(position);
	}

	public long getItemId(int position) {
		return planetList.get(position).hashCode();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		PlanetHolder holder = new PlanetHolder();
		
		// First let's verify the convertView is not null
		if (convertView == null) {
			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_row_task, null);
			// Now we can fill the layout with the right values
			holder.idTask= (TextView) v.findViewById(R.id.txt_list_task_id);
			holder.subjectTask = (TextView) v.findViewById(R.id.txt_list_task_subject);
			holder.ptTask = (TextView) v.findViewById(R.id.txt_list_task_pt);
			holder.expireTask = (TextView) v.findViewById(R.id.txt_list_task_tgl);
			holder.imageView = (ImageView) v.findViewById(R.id.img_list);
			
			v.setTag(holder);
		}
		else 
			holder = (PlanetHolder) v.getTag();

		TaskCalendar p = planetList.get(position);
		holder.idTask.setText(Integer.toString(p.getTask_id()));
		holder.subjectTask.setText(p.getTitle());
		holder.ptTask.setText(p.getCompany());
		String expire = p.getExpire();

		//Date date = new Date(expire * 1000L);
		//DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		//format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
		//String formatDate = format.format(date);
		holder.expireTask.setText(expire);


		/*long today;
		long epoch = 2592000;

		String str = dateNow();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date2 = null;
		try {
			date2 = df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		today = date2.getTime()/1000;
		Log.d("expire", Integer.toString(expire));
		Log.d("today", Long.toString(today));
		if (expire < today) {
			holder.imageView.setImageResource(R.drawable.point_red);
		}else if (expire >= (today+epoch)){
			holder.imageView.setImageResource(R.drawable.point_green);
		}else {
			holder.imageView.setImageResource(R.drawable.point_orange);
		}*/
		
		
		return v;
	}

	private String dateNow(){
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm", Locale.getDefault());
		Date date1 = new Date();
		return dateFormat.format(date1);
	}

	public void resetData() {
		planetList = origPlanetList;
	}
	
	
	/* *********************************
	 * We use the holder pattern        
	 * It makes the view faster and avoid finding the component
	 * **********************************/
	
	private static class PlanetHolder {
		private TextView idTask, subjectTask, ptTask, expireTask;
		private ImageView imageView;
	}
	

	
	/*
	 * We create our filter	
	 */
	
	@Override
	public Filter getFilter() {
		if (planetFilter == null)
			planetFilter = new PlanetFilter();
		
		return planetFilter;
	}



	private class PlanetFilter extends Filter {

		
		
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			// We implement here the filter logic
			if (constraint == null || constraint.length() == 0) {
				// No filter implemented we return all the list
				results.values = origPlanetList;
				results.count = origPlanetList.size();
			}
			else {
				// We perform filtering operation
				List<TaskCalendar> nPlanetList = new ArrayList<TaskCalendar>();

				for (TaskCalendar p : planetList) {
					if (p.getExpire().toUpperCase().startsWith(constraint.toString().toUpperCase()))
						nPlanetList.add(p);
				}
				
				results.values = nPlanetList;
				results.count = nPlanetList.size();

			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			
			// Now we have to inform the adapter about the new list filtered
			if (results.count == 0)
				notifyDataSetInvalidated();
			else {
				planetList = (List<TaskCalendar>) results.values;
				notifyDataSetChanged();
			}
			
		}
		
	}
}
