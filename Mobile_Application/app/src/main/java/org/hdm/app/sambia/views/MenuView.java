package org.hdm.app.sambia.views;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.hdm.app.sambia.R;
import org.hdm.app.sambia.listener.MenuListener;


public class MenuView extends RelativeLayout implements View.OnClickListener{

	private final String TAG = "MenuFragment";
	private Context context;


	private View view;

	private MenuListener listener;

	private TextView menu_tv;
	private String title = "";
	private RelativeLayout menu_rl;
	private ImageView menu_btn_flip;




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
	}




	private void initListener() {
		menu_rl.setOnClickListener(this);
//		menu_btn_flip.setOnClickListener(this);
//		menu_tv.setOnClickListener(this);
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




	@Override
	public void onClick(View v) {
		Log.d(TAG, "click" + v);
		listener.mClickInteraction(view);
	}
}







