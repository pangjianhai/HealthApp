package cn.com.hzzc.health.pro.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomeFrameAdapter extends FragmentPagerAdapter {

	List<Fragment> list;

	public HomeFrameAdapter(FragmentManager fragmentManager, List<Fragment> lst) {
		super(fragmentManager);
		this.list = lst;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

}