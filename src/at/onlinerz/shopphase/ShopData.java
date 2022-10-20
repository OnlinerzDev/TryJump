package at.onlinerz.shopphase;

import org.bukkit.Material;

public class ShopData {
	
	private String displayname;
	private Material material;
	private int slot;
	private int cost;
	
	public ShopData(String displayname, Material material, int slot, int cost) {
		this.displayname = displayname;
		this.material = material;
		this.slot = slot;
		this.cost = cost;
		
		
	}
	
	public String getDisplayname() {
		return displayname;
	}
	
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public void setSlot(int slot) {
		this.slot = slot;
	}

	public int getCost() {
		return cost;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
}
