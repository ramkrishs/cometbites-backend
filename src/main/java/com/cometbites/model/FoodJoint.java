package com.cometbites.model;

import java.util.List;

import org.springframework.data.annotation.Id;


public class FoodJoint {

    @Id
    public String id;

    private String name;
    private Menu menu;

    public FoodJoint() {}

    public FoodJoint(String name) {
        this.name = name;
    }
    
    public FoodJoint(List<Item> itemList) {
    	Menu menu = new Menu();
    	menu.setItems(itemList);
    	
    	this.menu = menu;
    }
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	@Override
    public String toString() {
        return String.format(
                "FoodJoint[id=%s, name='%s']",
                id, name);
    }

}