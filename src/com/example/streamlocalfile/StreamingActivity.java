package com.example.streamlocalfile;

import java.io.File;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class StreamingActivity extends ActionBarActivity {

	private static final String TAG = "StreamingActivity";
	LocalFileStreamingServer mServer;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_streaming);
		/*
		 * Make sure you have correct file structure. For demo copy the jellies
		 * file onto ROOT of SD card. You can have your own file pointed instead
		 */
		mServer = new LocalFileStreamingServer(new File(
				Environment.getExternalStorageDirectory() + "/jellies.mp4"));
		WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		String deviceIp = Formatter.formatIpAddress(wm.getConnectionInfo()
				.getIpAddress());
		mServer.init(deviceIp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.streaming, menu);
		return true;
	}

	public void stopStreaming(View v) {
		if (null != mServer)
			mServer.stop();
		((TextView) findViewById(R.id.status)).setText("");
	}

	public void startStreaming(View v) {
		if (null != mServer && !mServer.isRunning())
			mServer.start();
		((TextView) findViewById(R.id.status))
				.setText("use the below URL on your browser or any media player to stream this video:\n"
						+ mServer.getFileUrl());
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

	@Override
	protected void onDestroy() {
		if (null != mServer && mServer.isRunning()) {
			mServer.stop();
			mServer = null;
		}
		super.onDestroy();
	}
}
