package org.hdm.app.timetracker.views;



import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.listener.MenuListener;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;


public class MenuView extends RelativeLayout implements View.OnClickListener{

	private final String TAG = "MenuFragment";
	private Context context;


	private View view;

	private MenuListener listener;

	private TextView menu_tv;
	private String title = "";
	private RelativeLayout menu_rl;
	private ImageView menu_btn_flip;
	public ImageView menu_btn_currentDate;
	private int settingsCounter = Variables.getInstance().settingsCounter;


	public MenuView(Context context) {
		super(context);
	}

	public MenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initLayout();
		initListener();
	}

	public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}





	private void initLayout() {
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		view = inflater.inflate(R.layout.view_menu, this, true);
		menu_rl = (RelativeLayout) view.findViewById(R.id.menu_rl);
		menu_btn_flip = (ImageView) view.findViewById(R.id.menu_btn_flip);
		menu_tv = (TextView) view.findViewById(R.id.menu_tv);
		menu_btn_currentDate = (ImageView) view.findViewById(R.id.menu_btn_currentDate);
		menu_btn_currentDate.setVisibility(INVISIBLE);
	}




	private void initListener() {
//		menu_rl.setOnClickListener(this);
		menu_btn_flip.setOnClickListener(this);
		menu_tv.setOnClickListener(this);
		menu_btn_currentDate.setOnClickListener(this);
//		view.setOnClickListener(this);

	}




	public void setListener(MenuListener listener) {
		this.listener = listener;
	}



	public void setTitle(String title) {
		menu_tv.setText(title);
	}


	public void setBackground(int id){
		menu_rl.setBackgroundColor(getResources().getColor(id));
	}



	public void setImage(int imageId) {
		menu_btn_flip.setImageDrawable(getResources().getDrawable(imageId));
	}



	private void handleSettingsClick() {


			settingsCounter--;

			if(settingsCounter<=3) {
				String displayedText = settingsCounter + " Clicks to unlock Settings";

				if(settingsCounter==0) {
//					displayedText = "Setting is unlocked";
					settingsCounter = Variables.getInstance().settingsCounter;
					listener.onClickSettingsButton();
				}

				final Toast toast = Toast.makeText(context, displayedText, Toast.LENGTH_SHORT);
				toast.show();
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						toast.cancel();
					}
				}, 500);
			}

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				settingsCounter = Variables.getInstance().settingsCounter;
			}
		}, 10000);
	}



	@Override
	public void onClick(View v) {

		if(v.equals(menu_btn_flip)) {
			listener.mClickInteraction(view);
		}else if(v.equals(menu_tv)) {
			handleSettingsClick();
		}else if(v.equals(menu_btn_currentDate)){
			listener.scrollToCurrentTime(view);
		}
	}



}







