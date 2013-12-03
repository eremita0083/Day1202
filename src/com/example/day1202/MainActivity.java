package com.example.day1202;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	private Button stopBtn;
	private ImageButton playBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final ListView lv = (ListView) findViewById(R.id.lv);
		playBtn = (ImageButton) findViewById(R.id.playBtn);
		stopBtn = (Button) findViewById(R.id.stopBtn);
		final ToggleButton shuffleBtn = (ToggleButton) findViewById(R.id.shuffleBtn);

		// Iｄ
		final int[] rawIds = { R.raw.bgm008, R.raw.bgm026, R.raw.harunoyouki,
				R.raw.orgel, R.raw.otenba, R.raw.septet, R.raw.snowfield,
				R.raw.startingjapan, R.raw.teatime };
		final String[] title = { "bgm008", "btm026", "春の陽気", "オルゴール", "おてんば",
				"セプテット", "雪原", "starting Japan", "お茶の時間" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				MainActivity.this,
				android.R.layout.simple_list_item_single_choice, title);
		lv.setAdapter(adapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setItemChecked(0, true); //ListViewのCheckBoxにチェックを入れる

		// 再生処理
		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(lv.getCheckedItemCount() == 0 && !shuffleBtn.isChecked()){
					Toast.makeText(MainActivity.this, "再生する曲を選んでください", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(getApplicationContext(),
						MyService.class);
				if (shuffleBtn.isChecked()) {
					intent.setAction("shufflePlay");
				} else {
					intent.setAction("play");
					intent.putExtra("position", lv.getCheckedItemPosition());
				}
				intent.putExtra("musicId", rawIds);
				startService(intent);
			}
		});
		//stop
		stopBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MyService.class);
				intent.setAction("stop");
				startService(intent);
			}
		});
		
		//shuffle
		shuffleBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					lv.setChoiceMode(ListView.CHOICE_MODE_NONE);
				}else{
					lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
					lv.setItemChecked(0, true);
				}
			}
		});
		
	}
	
}
