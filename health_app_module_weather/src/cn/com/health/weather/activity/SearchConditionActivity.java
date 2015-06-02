package cn.com.health.weather.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import cn.com.health.weather.db.LocationCodeDB;

public class SearchConditionActivity extends Activity {
	Spinner SPprovince, SPcity, SParea;
	Button btnOK, btnSearch;
	EditText etSearch;
	TextView tv;
	List<String> provinceidList, provincenameList;
	List<String> cityidList, citynameList;
	List<String> areaidList, areanameList;
	String province;
	String city;
	String area;
	String citycode;
	String citycode_name;
	LocationCodeDB citycodedb = null;
	/**
	 * 数据库DB
	 */
	SQLiteDatabase db = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_condition_activity);

		init();
		initProvinceSpinner(db);

		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SearchConditionActivity.this,
						WeatherResultActivity.class);
				intent.putExtra("citycode", citycode);
				intent.putExtra("citycode_name", citycode_name);
				startActivity(intent);
			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String cityname = etSearch.getText().toString();
				citycode_name = cityname;
				citycode = searchCityCodeByName(db, cityname);
				if (citycode != null) {
					Intent intent = new Intent(SearchConditionActivity.this,
							WeatherResultActivity.class);
					intent.putExtra("citycode", citycode);
					intent.putExtra("citycode_name", citycode_name);
					startActivity(intent);
				} else {
					tv.setText("您查找的城市或地区不存在!");
				}
			}
		});
	}

	// 执行初始化操作
	void init() {
		SPprovince = (Spinner) findViewById(R.id.province);
		SPcity = (Spinner) findViewById(R.id.city);
		SParea = (Spinner) findViewById(R.id.area);
		btnOK = (Button) findViewById(R.id.btnOK);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		etSearch = (EditText) findViewById(R.id.search);
		tv = (TextView) findViewById(R.id.citycode);
		provinceidList = new ArrayList<String>();
		provincenameList = new ArrayList<String>();
		cityidList = new ArrayList<String>();
		citynameList = new ArrayList<String>();
		areaidList = new ArrayList<String>();
		areanameList = new ArrayList<String>();

		citycodedb = new LocationCodeDB(SearchConditionActivity.this);
		/**
		 * 根据DB的文件名字获取数据库
		 */
		db = citycodedb.getDatabase("data.db");
	}

	/**
	 * 
	 * @param database
	 * @user:pang
	 * @data:2015年6月1日
	 * @todo:初始化省直辖市Spinner
	 * @return:void
	 */
	public void initProvinceSpinner(SQLiteDatabase database) {
		Cursor provincecursor = citycodedb.getAllProvince(database);

		if (provincecursor != null) {
			provinceidList.clear();
			provincenameList.clear();
			if (provincecursor.moveToFirst()) {
				do {
					String province_id = provincecursor
							.getString(provincecursor.getColumnIndex("id"));
					String province_name = provincecursor
							.getString(provincecursor.getColumnIndex("name"));
					provinceidList.add(province_id);
					provincenameList.add(province_name);
				} while (provincecursor.moveToNext());
			}
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				SearchConditionActivity.this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				provincenameList);
		SPprovince.setAdapter(adapter);

		/**
		 * 事件-根据生选择市
		 */
		OnItemSelectedListener listener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int position, long id) {
				initCitySpinner(db, provinceidList.get(position).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};

		SPprovince.setOnItemSelectedListener(listener);
	}

	/**
	 * 
	 * @param database
	 * @param provinceid
	 * @user:pang
	 * @data:2015年6月1日
	 * @todo:初始化市Spinner
	 * @return:void
	 */
	public void initCitySpinner(SQLiteDatabase database, String provinceid) {
		Cursor citycursor = citycodedb.getCity(database, provinceid);
		if (citycursor != null) {
			cityidList.clear();
			citynameList.clear();
			if (citycursor.moveToFirst()) {
				do {
					String city_id = citycursor.getString(citycursor
							.getColumnIndex("id"));
					String city_name = citycursor.getString(citycursor
							.getColumnIndex("name"));
					String province = citycursor.getString(citycursor
							.getColumnIndex("p_id"));
					cityidList.add(city_id);
					citynameList.add(city_name);
				} while (citycursor.moveToNext());
			}
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				SearchConditionActivity.this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				citynameList);
		SPcity.setAdapter(adapter);

		OnItemSelectedListener listener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int position, long id) {
				initAreaSpinner(db, cityidList.get(position).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		};
		SPcity.setOnItemSelectedListener(listener);
	}

	/**
	 * 
	 * @param database
	 * @param cityid
	 * @user:pang
	 * @data:2015年6月1日
	 * @todo: 初始化地区Spinner,同时获取城市码
	 * @return:void
	 */
	public void initAreaSpinner(SQLiteDatabase database, String cityid) {
		Cursor areacursor = citycodedb.getArea(db, cityid);
		if (areacursor != null) {
			areaidList.clear();
			areanameList.clear();
			if (areacursor.moveToFirst()) {
				do {
					String area_id = areacursor.getString(areacursor
							.getColumnIndex("id"));
					String area_name = areacursor.getString(areacursor
							.getColumnIndex("name"));
					String city = areacursor.getString(areacursor
							.getColumnIndex("c_id"));
					areaidList.add(area_id);
					areanameList.add(area_name);
				} while (areacursor.moveToNext());
			}
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				SearchConditionActivity.this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				areanameList);
		SParea.setAdapter(adapter);

		OnItemSelectedListener listener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				citycode_name = areanameList.get(position).toString();
				citycode = citycodedb.getCityCode(db, areaidList.get(position)
						.toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};
		SParea.setOnItemSelectedListener(listener);
	}

	/**
	 * 
	 * @param database
	 * @param cityname
	 * @return
	 * @user:pang
	 * @data:2015年6月1日
	 * @todo:通过城市名查找城市码
	 * @return:String
	 */
	public String searchCityCodeByName(SQLiteDatabase database, String cityname) {
		String citycode = null;
		citycode = citycodedb.getCityCodeByName(database, cityname);
		return citycode;
	}
}
