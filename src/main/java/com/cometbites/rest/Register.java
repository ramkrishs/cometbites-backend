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
import com.cometbites.model.Card;
import com.cometbites.model.CometCard;
import com.cometbites.model.CreditCard;
import com.cometbites.model.Customer;
import com.cometbites.model.FoodJoint;
import com.cometbites.model.Item;
import com.cometbites.model.Order;
import com.cometbites.model.Payment;
import com.cometbites.model.Status;
import com.cometbites.model.Ticket;
import com.cometbites.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

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
	 * 
	 * @param customerID
	 * @return
	 */
	@GET
	@Path("/{customerID}")
	public String startOrder(@PathParam("customerID") String customerID, @Context HttpHeaders headers) {
		if (headers.getHeaderString("UID") != null) {
			if (!auth.isUserExist(headers.getHeaderString("UID"))) {
				auth.setCustomerID(headers.getHeaderString("UID"), customerID);
			}
		}

		return dBFacade.getFoodJoints();
	}

	/**
	 * Get Individual FoodJoint by id
	 * 
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
	 * 
	 * @param itemID
	 * @param name
	 * @param description
	 * @param price
	 * @return
	 */
	@POST
	@Path("/select/{itemID}")
	public String selectItem(@PathParam("itemID") String itemID, @FormParam("name") String name,
			@FormParam("description") String description, @FormParam("price") String price,
			@FormParam("fjID") String fjID, @Context HttpHeaders headers) {
		Order order = getOrderByUID(headers.getHeaderString("UID"));

		if (order == null) {
			order = new Order();
			order.setCustomer(new Customer(auth.getCustomerID(headers.getHeaderString("UID"))));
			order.setFoodJoint(new FoodJoint(fjID));
		}

		Item item = new Item(itemID, name, description, Double.parseDouble(price));

		order.addItem(item);

		return item.getDescription();
	}

	/**
	 * Update Quantity from user response
	 * 
	 * @param itemID
	 * @param name
	 * @param description
	 * @param price
	 * @param quantity
	 * @return
	 */
	@POST
	@Path("/addquantity/{itemID}")
	public String informQuantity(@PathParam("itemID") String itemID, @FormParam("name") String name,
			@FormParam("description") String description, @FormParam("price") String price,
			@FormParam("quantity") String quantity, @FormParam("fjID") String fjID, @Context HttpHeaders headers) {
		Order order = getOrderByUID(headers.getHeaderString("UID"));
		order.setFoodJoint(new FoodJoint(fjID));

		order.updateQuantity(new Item(itemID, name, description, Double.parseDouble(price)),
				Integer.parseInt(quantity));
		dBFacade.saveOrder(order);

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
	 * 
	 * @return
	 */

	@GET
	@Path("/checkout")
	public String checkOut(@Context HttpHeaders headers) {
		if (headers.getHeaderString("UID") != null) {
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
	public String makePayment(@FormParam("cardname") String cardName, @FormParam("cardno") String cardNo,
			@FormParam("cvv") String cvv, @FormParam("expdate") String expDate, @Context HttpHeaders headers) {
		Order order = getOrderByUID(headers.getHeaderString("UID"));
		
		Card card = null;
		if (cvv == null || "".equals(cvv)) {
			card = new CometCard(Long.parseLong(cardNo));
		} else {
			card = new CreditCard(Long.parseLong(cardNo),
					Util.parseDate(expDate, Util.EXP_DATE_FORMAT),
					Integer.parseInt(cvv));
		}

		card.setCardName(cardName);

		Payment payment = new Payment(card);
		payment.setAmount(order.getTotal());
		
		Ticket ticket = order.concludeOrder(payment, dBFacade.calculateWaitTime(order.getFoodJoint().getId()));

		dBFacade.updateOrder(order);
		ticket.setCode(order.getInvoice());

		sendSMS(headers.getHeaderString("UID"), "Thank you for using CometBites!"
				+ "\nYour payment was accepted and your order is In Preparation."
				+ "\nInvoice: "+order.getInvoice()+"."
				+ "\nWait Time: "+order.getTicket().getWaitTime()+" minutes.");
		
		return ticket.toString();
	}

	/**
	 * 
	 * @param paymentOption
	 * @return
	 */
	@POST
	@Path("/order/eticket")
	public String generateEticket(@Context HttpHeaders headers) {
		Ticket ticket = new Ticket();
		try {

			DBObject res = new BasicDBObject();
			res.put("result", 400);

			Order order = getOrderByUID(headers.getHeaderString("UID"));

			DBObject query = new BasicDBObject();
			query.put("invoice", order.getInvoice());

			DBObject update = new BasicDBObject();

			DBObject values = new BasicDBObject("status", Status.PENDING.getValue());
			values.put("date", Util.getCurrentTime());
			update.put("$set", values);

			dBFacade.updateOrderDocument(query, update);

			ticket.setCode(order.getInvoice());

			ticket.setWaitTime(Float.toString(dBFacade.calculateWaitTime(order.getFoodJoint().getId())));

			sendSMS(headers.getHeaderString("UID"), "Thank you for using CometBites!"
					+ "\nYour e-Ticket was generated. Please note your order will only be prepared after the payment."
					+ "\nInvoice: "+order.getInvoice()+"."
					+ "\nDue amount: $"+order.getTotal()+"."
					+ "\nWait Time: "+ticket.getWaitTime()+" minutes.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ticket.toString();
	}

	/**
	 * 
	 * @param customerUID
	 * @return
	 */
	private Order getOrderByUID(String customerUID) {
		
		Order order = dBFacade.getNewOrderByCustomerID(auth.getCustomerID(customerUID));
				
		if (order == null) {
			order = new Order();
			order.setCustomer(new Customer(auth.getCustomerID(customerUID)));
		}
		
		return order;
	}
	
	private void sendSMS(String UID, String msg) {
		String netid = auth.getCustomerID(UID);
		String phone = dBFacade.getPhonenumberbyNetid(netid);
		Util.SendSms(phone, msg);
	}

}