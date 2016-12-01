package com.cometbites.rest;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cometbites.db.Auth;
import com.cometbites.db.DBFacade;
import com.cometbites.model.Customer;
import com.cometbites.model.FoodJoint;
import com.cometbites.model.Item;
import com.cometbites.model.Order;
import com.cometbites.model.Ticket;

@Component
@Path("register")
@Produces(MediaType.APPLICATION_JSON)
public class Register {
	
	@Autowired
	private DBFacade dBFacade;
	
	@Autowired
	private Auth auth;
	
	/**
	 * Get All FoodJoints to the customer 
	 * @param customerID
	 * @return
	 */
	@GET
	@Path("/{customerID}")
	public String startOrder(@PathParam("customerID") String customerID, @Context HttpHeaders headers) {
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
	@Path("/foodjoint/{foodJointID}")
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
	public String selectItem(@PathParam("itemID") String itemID,  @FormParam("name") String name, @FormParam("description") String description,  @FormParam("price") String price, @FormParam("fjID") String fjID, @Context HttpHeaders headers) {
		Order order = getOrderByUID(headers.getHeaderString("UID"));
		
		if(order == null) {
			order = new Order();
			order.setCustomer(new Customer(auth.getCustomerID(headers.getHeaderString("UID"))));
			order.setFoodJoint(new FoodJoint(fjID));
		}
		
		Item item = new Item(itemID, name, description, Double.parseDouble(price));
		
		order.addItem(item);
		dBFacade.saveOrder(order);

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
			@FormParam("description") String description, @FormParam("price") String price, @FormParam("quantity") String quantity, @Context HttpHeaders headers) {
		Order order = getOrderByUID(headers.getHeaderString("UID"));
		
		order.updateQuantity(new Item(itemID, name, description, Double.parseDouble(price)), Integer.parseInt(quantity));
		dBFacade.updateOrder(order);
		
		return Double.toString(order.getTotal());
	}
	
	/**
	 * View current order
	 * 
	 * @return
	 */
	@GET
	@Path("/order")
	public String viewOrder(@Context HttpHeaders headers) {
		Order order = getOrderByUID(headers.getHeaderString("UID"));
		
		return order.getOrderItems().toString();
	}
	
	/**
	 * Get Payment Information of the user
	 * @return
	 */
	
	@GET
	@Path("/checkout")
	public String checkOut(@Context HttpHeaders headers) {
		if(headers.getHeaderString("UID")!=null){
			return dBFacade.getPaymentOptions(auth.getCustomerID(headers.getHeaderString("UID")));
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
	public String makePayment(@FormParam("option") String paymentOption, @Context HttpHeaders headers) {
		Order order = getOrderByUID(headers.getHeaderString("UID"));
		
		Ticket ticket = order.concludeOrder(paymentOption);
		
		dBFacade.updateOrder(order);
		
		ticket.setCode(order.getInvoice());
		
		return ticket.toString();
	}
	
	private Order getOrderByUID(String customerUID) {
		return dBFacade.getNewOrderByCustomerID(auth.getCustomerID(customerUID));
	}
	
}