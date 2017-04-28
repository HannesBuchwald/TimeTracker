package org.hdm.app.timetracker.screens;



import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.listener.MainListener;


public class FragmentContainer extends Fragment  implements
		FragmentManager.OnBackStackChangedListener,
		MainListener {


	private FragmentActivity activityFragment;
	private View view;
	private boolean mShowingBack = false;
	private FragmentCalender dayViewFragment;
	private FragmentSettings settingsFragment;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_main, container, false);


		initFragments();
		loadFragment(savedInstanceState);
		getFragmentManager().addOnBackStackChangedListener(this);

		return view;
	}





	private void initFragments() {
		dayViewFragment = new FragmentCalender();
		activityFragment = new FragmentActivity();
		settingsFragment = new FragmentSettings();

		dayViewFragment.setContext(this);
		activityFragment.setContext(this);
		settingsFragment.setContext(this);
	}




	private void loadFragment(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, activityFragment).commit();
		} else {
			mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
		}
	}








	private void flipCard() {

		if (mShowingBack) {
			getFragmentManager().popBackStack();
			return;
		}

		// Flip to the back.
		mShowingBack = true;





		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {

			// below lillipop
			getFragmentManager().beginTransaction()
					.replace(R.id.container, dayViewFragment)
					.addToBackStack(null)
					.commit();
			// working for lower questions as per another answer posted here

		} else {
			// lollipop and above
			getFragmentManager().beginTransaction()
					.setCustomAnimations(
							R.animator.card_flip_right_in, R.animator.card_flip_right_out,
							R.animator.card_flip_left_in, R.animator.card_flip_left_out)
					.replace(R.id.container, dayViewFragment)
					.addToBackStack(null)
					.commit();
			// known to be working for lollipop as per your question
		}
	}




	@Override
	public void onBackStackChanged() {
		mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
	}






	@Override
	public void flip() {
		flipCard();
	}



	@Override
	public void displaySettingsFragment() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {

			// below lillipop
			getFragmentManager().beginTransaction()
					.replace(R.id.container, settingsFragment)
					.addToBackStack(null)
					.commit();
			// working for lower questions as per another answer posted here

		} else {
			// lollipop and above
			getFragmentManager().beginTransaction()
					.setCustomAnimations(
							R.animator.card_flip_right_in, R.animator.card_flip_right_out,
							R.animator.card_flip_left_in, R.animator.card_flip_left_out)
					.replace(R.id.container, settingsFragment)
					.addToBackStack(null)
					.commit();
			// known to be working for lollipop as per your question
		}
	}


}







