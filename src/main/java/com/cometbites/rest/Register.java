package com.cometbites.rest;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cometbites.db.DBFacade;
import com.cometbites.model.Customer;
import com.cometbites.model.FoodJoint;
import com.cometbites.model.Item;
import com.cometbites.model.LineItem;
import com.cometbites.model.Order;
import com.cometbites.model.Ticket;

@Component
@Path("register")
@Produces(MediaType.APPLICATION_JSON)
public class Register {
	
	//FIXME info from client and/or login ???
//	private IDevice currentCustomer;
	private Customer currentCustomer;
	private Order currentOrder;
	
	@Autowired
	private DBFacade dBFacade;
	
	@GET
	@Path("/{customerID}")
	public String startOrder(@PathParam("customerID") String customerID) {
		currentCustomer = new Customer(customerID);
		
		return dBFacade.getFoodJoints();
	}
	
	@GET
	@Path("/foodJoint/{foodJointID}")
	public String selectFoodJoint(@PathParam("foodJointID") String foodJointID) {
		
		List<Item> itemList = dBFacade.getMenu(foodJointID);
		
		FoodJoint foodJoint = new FoodJoint(itemList);
		
		return foodJoint.getMenu().toString();
	}
	
	@PUT
	@Path("/item/{itemID}")
	public String selectItem(@PathParam("itemID") String itemID,  @FormParam("name") String name, @FormParam("description") String description,  @FormParam("price") String price) {
		
		if(currentOrder == null) {
			currentOrder = new Order();
			currentOrder.setCustomer(currentCustomer);
		}
		
		Item item = new Item(itemID, name, description, Double.parseDouble(price));
		currentOrder.addItem(item);
		
		return item.getDescription();
	}
	
	@POST
	@Path("/item/{itemID}")
	public String informQuantity(@PathParam("itemID") String itemID,  @FormParam("name") String name, 
			@FormParam("description") String description, @FormParam("price") String price, @FormParam("quantity") String quantity) {
		
		currentOrder.updateQuantity(new Item(itemID, name, description, Double.parseDouble(price)), Integer.parseInt(quantity));
		
		System.out.println("QUANTITY INFORMED: "+quantity);
		
		System.out.println(currentCustomer.getId());
		
		for (LineItem item : currentOrder.getOrderItems()) {
			System.out.println("LINE ITEM: "+item.getItem().getName()+" x"+item.getQuantity());
		}
		System.out.println();
		
		return Double.toString(currentOrder.getTotal());
	}
	
	@GET
	@Path("/order")
	public String viewOrder() {
		return currentOrder.getOrderItems().toString();
	}
	
	@GET
	@Path("/checkout")
	public String checkOut() {
		return dBFacade.getPaymentOptions(currentCustomer.getId());
	}
	
	@POST
	@Path("/order/payment")
	public String makePayment(@FormParam("option") String paymentOption) {
		
		Ticket ticket = currentOrder.concludeOrder(paymentOption);
		
		String code = dBFacade.saveOrder(currentOrder);
		
		currentOrder.setId(code);
		ticket.setCode(code);
		
		return ticket.toString();
	}
	
	//FIXME TESTS
	@GET
	@Path("/date")
	public void getDate() {
		dBFacade.createOrderStatus();
	}
	
}