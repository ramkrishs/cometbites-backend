package com.cometbites.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
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
import com.cometbites.util.Auth;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

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
	
	@Autowired
	private Auth auth;
	
	@GET
	@Path("/test/{customerID}")
	public String testOrder(@PathParam("customerID") String customerID,@Context HttpHeaders headers) {
		
		auth.isUserExist(headers.getHeaderString("UID"));
		Map<String, String> map = new HashMap<>();
		DBObject obj = new BasicDBObject();
		obj.put("result", "hi sorry");
		
		if (headers.getHeaderString("UID") != null ){
			
		}
		else{
			obj.put("result", "hi sorry");
		}
		
		return obj.toString();
		
		
	}
	
	
	/**
	 * Get All FoodJoints to the customer 
	 * @param customerID
	 * @return
	 */
	@GET
	@Path("/{customerID}")
	public String startOrder(@PathParam("customerID") String customerID,@Context HttpHeaders headers) {
		
		if (headers.getHeaderString("UID") != null ){
			if(!auth.isUserExist(headers.getHeaderString("UID"))){
				auth.setCustomerID(headers.getHeaderString("UID"), customerID);
			}
		}
		
		
		
		return dBFacade.getFoodJoints();
	}
	
	
	
	/**
	 * Get Individual FoodJoint by id 
	 * @param foodJointID
	 * @return
	 */
	@GET
	@Path("/foodJoint/{foodJointID}")
	public String selectFoodJoint(@PathParam("foodJointID") String foodJointID) {
		
		List<Item> itemList = dBFacade.getMenu(foodJointID);
		
		FoodJoint foodJoint = new FoodJoint(itemList);
		
		return foodJoint.getMenu().toString();
	}
	
	
	/**
	 * Create a Order for user and send him item Description
	 * @param itemID
	 * @param name
	 * @param description
	 * @param price
	 * @return
	 */
	@POST
	@Path("/select/{itemID}")
	public String selectItem(@PathParam("itemID") String itemID,  @FormParam("name") String name, @FormParam("description") String description,  @FormParam("price") String price) {
		
		if(currentOrder == null) {
			currentOrder = new Order();
			currentOrder.setCustomer(currentCustomer);
		}
		
		Item item = new Item(itemID, name, description, Double.parseDouble(price));
		currentOrder.addItem(item);
		
		return item.getDescription();
	}
	
	/**
	 * Update Quantity from user response
	 * @param itemID
	 * @param name
	 * @param description
	 * @param price
	 * @param quantity
	 * @return
	 */
	@POST
	@Path("/addquantity/{itemID}")
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
	
	
	/**
	 * View current order
	 * 
	 * @return
	 */
	@GET
	@Path("/order")
	public String viewOrder() {
		return currentOrder.getOrderItems().toString();
	}
	
	/**
	 * Get Payment Information of the user
	 * @return
	 */
	
	@GET
	@Path("/checkout")
	public String checkOut(@Context HttpHeaders headers) {
		if(headers.getHeaderString("UID")!=null){
			return dBFacade.getPaymentOptions(headers.getHeaderString("UID"));
		}
		return "{\"result\":\"500\"}";
	}
	
	/**
	 * 
	 * @param paymentOption
	 * @return
	 */
	@POST
	@Path("/order/payment")
	public String makePayment(@FormParam("option") String paymentOption) {
		
		Ticket ticket = currentOrder.concludeOrder(paymentOption);
		
		String code = dBFacade.saveOrder(currentOrder);
		
		currentOrder.setId(code);
		ticket.setCode(code);
		
		return ticket.toString();
	}
	
	
	
}