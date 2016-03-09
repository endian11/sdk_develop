package com.travelrely.app.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.travelrely.app.view.CallLayout;
import com.travelrely.app.view.NavigationBar;
import com.travelrely.app.view.CallLayout.OnAddHaveContactListener;
import com.travelrely.app.view.NavigationBar.OnNavigationBarClick;
import com.travelrely.sdk.R;

public class DialPadFragment extends Fragment implements OnNavigationBarClick
{
    private LinearLayout layout;
    private NavigationBar navigationBar;
    private OnAddHaveContactListener mon;
    CallLayout dialView;
    String currNum;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        layout = (LinearLayout) inflater.inflate(R.layout.dial_fragment,
                container, false);
         dialView = (CallLayout) layout.findViewById(R.id.dial_paddd);
        intNavigationBar();
        init();
        return layout;
    }
    
    
    

	private void init() {
		dialView.setmOnL(new OnAddHaveContactListener() {
			
			@Override
			public void OnAddHaveContactk(String callNum) {
				init2(callNum);
			}
		});
	}

	@Override
    public void onResume()
    {
        super.onResume();
    }
	
	
	@Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    
    private void intNavigationBar()
    {
        navigationBar = (NavigationBar) layout
                .findViewById(R.id.navigation_bar);
        navigationBar.setOnNavigationBarClick(this);

        navigationBar.setTitleText(R.string.call);
        navigationBar.hideLeftText();
        navigationBar.getLeftImg().setImageResource(R.drawable.home_icon_bg);
    }

    @Override
    public void onLeftClick()
    {
        getActivity().finish();
    }

    @Override
    public void onTitleClick()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onRightClick()
    {
        // TODO Auto-generated method stub
        
    }
    
    
    

	
	public void init2(String callNum) {
		 Intent intent = new Intent();
         intent.setAction(Intent.ACTION_PICK);
         intent.setData(ContactsContract.Contacts.CONTENT_URI);
         currNum = callNum;
         startActivityForResult(intent, 0);
	}
	  @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 0:
				   if (data == null) {
				       return;
				   }
				   Uri result = data.getData();
				   String contactId = result.getLastPathSegment();
				   System.out.println("contactId" + contactId);
			   
				   
				   Intent intent = new Intent(Intent.ACTION_EDIT,
							Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, 
									String.valueOf(contactId)));
			    	 intent.putExtra(Contacts.Intents.Insert.PHONE,currNum);
			        startActivity(intent);
			}

	    }


}
