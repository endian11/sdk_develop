package com.travelrely.v2.model;

import java.io.Serializable;

//The model of Commodity
public class Commodity implements Serializable
{
	private static final long	serialVersionUID	= 4318838659250781721L;

	// name of commodity
	private String				name;

	// 是否选中
	private boolean				selected;
	
	// 是否需要快递
    private boolean             needExp;

	// Type of commodity
	private int					type;										// 1-手机卡,
																			// 2-套餐

	// number of commodity
	private int					numItems;

	// price of one commodity
//	private double				price;

	// price of all commodity
	private double				totalPrice;

	// SIM卡商品
	private SimCard				simCard;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean getSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
	
    public boolean getNeedExp()
    {
        return needExp;
	}

    public void setNeedExp(boolean selected)
    {
        this.needExp = selected;
    }

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getNumItems()
	{
		return numItems;
	}

	public void setNumItems(int numItems)
	{
		this.numItems = numItems;
	}

//	public double getPrice()
//	{
//		return price;
//	}
//
//	public void setPrice(double price)
//	{
//		this.price = price;
//	}

	public double getTotalPrice()
	{
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice)
	{
		this.totalPrice = totalPrice;
	}

	public SimCard getSimCard()
	{
		return simCard;
	}

	public void setSimCard(SimCard simCard)
	{
		this.simCard = simCard;
	}

	@Override
	public String toString()
	{
		return "Commodity [name=" + name + ", selected=" + selected + ", type="
				+ type + ", numItems=" + numItems
				+ ", totalPrice=" + totalPrice + ", simCard=" + simCard + "]";
	}

}
