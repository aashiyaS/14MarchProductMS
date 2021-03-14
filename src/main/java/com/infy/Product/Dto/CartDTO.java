package com.infy.Product.Dto;

public class CartDTO {
	Integer buyerid;
	Integer prodid;
	Integer quantity;

	public CartDTO()
	{
		super();
	}
	public CartDTO(Integer buyerid, Integer prodid, Integer quantity)
	{
		this();
		this.buyerid=buyerid;
		this.prodid=prodid;
		this.quantity=quantity;
	}
	public void setBuyerId(Integer buyerid)
	{
	this.buyerid=buyerid;	
	}
	public Integer getBuyerId()
	{
	return buyerid;	
	}
	public void setProdId(Integer prodid)
	{
	this.prodid=prodid;	
	}
	public Integer getProdId()
	{
	return prodid;
	}
	public void setQuantity(Integer quantity)
	{
	 this.quantity=quantity;	
	}
	public Integer getQuantity()
	{
	return quantity;	
	}
}