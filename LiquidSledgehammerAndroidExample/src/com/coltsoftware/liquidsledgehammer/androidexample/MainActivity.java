package com.coltsoftware.liquidsledgehammer.androidexample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.coltsoftware.liquidsledgehammer.androidexample.truedata.FinancialFillTagDrawer;
import com.coltsoftware.liquidsledgehammer.androidexample.truedata.FinancialTagDrawer;
import com.coltsoftware.liquidsledgehammer.androidexample.truedata.GraphDataSourceAdaptor;
import com.coltsoftware.liquidsledgehammer.androidexample.truedata.PathSourceWalker;
import com.coltsoftware.liquidsledgehammer.sources.FinancialTransactionSource;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private long backTimer;

	@Override
	public void onBackPressed() {
		RectDisplay display = (RectDisplay) findViewById(R.id.rectDisplay1);
		if (!display.back()) {
			long currentTimeMillis = System.currentTimeMillis();
			if (currentTimeMillis - backTimer < 5000)
				super.onBackPressed();
			else {
				backTimer = currentTimeMillis;
				Toast.makeText(this, "Press back again to exit",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private GraphDataSourceAdaptor dataSource;

		public PlaceholderFragment() {
			setRetainInstance(true);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			if (dataSource == null)
				loadData(rootView);

			applyDataSource(rootView, dataSource);
			return rootView;
		}

		private void loadData(View rootView) {
			try {
				File finances = new File(
						Environment.getExternalStorageDirectory(), "Fin");
				File path = new File(finances, "Transactions");
				Log.d(TAG, path.toString());
				if (path.exists()) {
					ArrayList<FinancialTransactionSource> sources = PathSourceWalker
							.loadAllSourcesBelowPath(path);

					dataSource = new GraphDataSourceAdaptor(getActivity(),
							sources, finances);

				} else {
					Log.w(TAG, "Path does not exist");
					for (File child : Environment.getExternalStorageDirectory()
							.listFiles())
						Log.w(TAG, child.toString());
				}
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
		}

		private void applyDataSource(View rootView, GraphDataSource dataSource) {
			RectDisplay rectDisplay = (RectDisplay) (rootView
					.findViewById(R.id.rectDisplay1));
			rectDisplay.setDataSource(dataSource);
			rectDisplay.setTagDrawer(new FinancialTagDrawer());
			rectDisplay.setTagFillDrawer(new FinancialFillTagDrawer());
		}
	}

}
