package com.travelrely.app.view;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.IAction;
import com.travelrely.core.util.LOGManager;
import com.travelrely.sdk.R;
import com.travelrely.v2.model.Cart;
import com.travelrely.v2.model.Commodity;

public class SystemDialog extends Activity implements OnClickListener
{
    private TextView tvOperate;
    private TextView tvDescription;
    private Button okBtn, cancelBtn;

    private String strPreActivity = null;

    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_dialog);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            strPreActivity = bundle.getString("PreActivity");
        }

        if (strPreActivity == null)
        {
            finish();
            return;
        }

        init();
    }

    private void init()
    {
        tvOperate = (TextView) findViewById(R.id.tvOperate);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        okBtn = (Button) findViewById(R.id.okBtn);
        okBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(this);

        if (strPreActivity.equals("ShoppingCartActivity"))
        {
            tvOperate.setText("删除商品");
            tvDescription.setText("确认从购物车中删除选中商品？");
        }
        else if (strPreActivity.equals("MealOneActivity"))
        {
            tvOperate.setText("添加成功");
            tvDescription.setText("商品已经成功添加到购物车！");
            okBtn.setText("去购物车结算");
            cancelBtn.setText("继续逛逛");
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
		if (id == R.id.cancelBtn) {
			if (strPreActivity.equals("MealOneActivity"))
			{
			    finish();
			    
			    // 发广播关闭商店首页下一级的界面
			    Intent i = new Intent(IAction.finish);
			    sendBroadcast(i);
			}
			else
			{
			    finish();
			}
		} else if (id == R.id.okBtn) {
			if (strPreActivity.equals("ShoppingCartActivity"))
			{
			    delSelectedCommodity();
			}
			else if (strPreActivity.equals("MealOneActivity"))
			{
//                    Intent intent = new Intent(SystemDialog.this,
//                            ShoppingCartActivity.class);
//                    startActivity(intent);
				// TODO cwj
			}
			finish();
		} else {
		}
    }

    private void getCartData()
    {
        // 读取购物车数据
        try
        {
            String strObject = Engine.getInstance().getCart();
            if (strObject.equals("") == false)
            {
                cart = Cart.deSerialization(strObject);
                LOGManager.d("read cart=" + cart);
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void saveCartData()
    {
        // 读取购物车数据
        try
        {
            if (cart != null)
            {
                String strObject = Cart.serialize(cart);
                Engine.getInstance().setCart(strObject);
                LOGManager.d("set cart=" + cart);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void delSelectedCommodity()
    {
        int iCommodityNum = 0;
        double orderTtlPrice = 0;

        getCartData();

        List<Commodity> commodityItems = cart.getCommodityItems();
        if (commodityItems == null || commodityItems.size() == 0)
        {
            LOGManager.e("commodityItems == null");
            cart.setSelectedNum(0);
            cart.setSelectedOrderPrice(0);
            cart.setTtlNumber(0);
            cart.setTtlOrderPrice(0);
            cart.setTotalMoney(0);
            return;
        }

        int i = 0;
        while (commodityItems != null && commodityItems.size() != 0)
        {
            if (i >= commodityItems.size())
            {
                break;
            }

            Commodity c = commodityItems.get(i);
            if (c.getSelected())
            {
                commodityItems.remove(c);

                iCommodityNum = cart.getTtlNumber();
                iCommodityNum--;
                cart.setTtlNumber(iCommodityNum);
                orderTtlPrice = cart.getTtlOrderPrice() - c.getTotalPrice();
                cart.setTtlOrderPrice(orderTtlPrice);
            }
            else
            {
                i++;
            }
        }

        cart.setSelectedNum(0);
        cart.setSelectedOrderPrice(0);

        saveCartData();
    }
}
