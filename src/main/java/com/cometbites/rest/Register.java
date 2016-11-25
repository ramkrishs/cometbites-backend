package com.cometbites.rest;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.cometbites.db.DBFacade;
import com.cometbites.model.FoodJoint;
import com.cometbites.model.IDevice;
import com.cometbites.model.Item;
import com.cometbites.model.LineItem;
import com.cometbites.model.Order;

@Component
@Path("register")
@Produces(MediaType.APPLICATION_JSON)
public class Register {
	
	//FIXME info from client and/or login ???
	private IDevice currentCustomer;
	private Order currentOrder;
	
	@GET
	public String startOrder() {
		return DBFacade.getInstance().getFoodJoints().toString();
	}
	
	@GET
	@Path("/foodJoint/{foodJointID}")
	public String selectFoodJoint(@PathParam("foodJointID") String foodJointID) {
		
		List<Item> itemList = DBFacade.getInstance().getMenu(foodJointID);
		
		FoodJoint foodJoint = new FoodJoint(itemList);
		
		return foodJoint.getMenu().getItems().toString();
	}
	
	@PUT
	@Path("/item/{itemID}")
	public String selectItem(@PathParam("itemID") String itemID, @FormParam("price") String price, @FormParam("description") String description) {
		
		if(currentOrder == null) {
			currentOrder = new Order();
		}
		
		Item item = new Item(itemID, description, Double.parseDouble(price));
		currentOrder.addItem(item);
		
		return item.getDescription();
	}
	
	@POST
	@Path("/item/{itemID}")
	public double informQuantity(@PathParam("itemID") String itemID, @FormParam("quantity") String quantity) {
		currentOrder.updateQuantity(itemID, Integer.parseInt(quantity));
		
		return currentOrder.getTotal();
	}
	
	@GET
	@Path("/order")
	public Collection<LineItem> viewOrder() {
		return currentOrder.getOrderItems();
	}
	
	@POST
	@Path("/order")
	public List<String> checkOut() {
		return DBFacade.getInstance().getPaymentOptions(currentCustomer.getID());
	}
	
	@POST
	@Path("/order")
	public List<String> makePayment(@FormParam("quantity") String paymentOption) {
		return DBFacade.getInstance().getPaymentOptions(currentCustomer.getID());
	}
	
	
	
}
