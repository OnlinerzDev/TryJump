package at.onlinerz.shopphase;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import at.onlinerz.jumpphase.JumpListener;
import at.onlinerz.tryjump.TryJump;
import net.minecraft.server.v1_8_R3.Enchantment;
import net.minecraft.server.v1_8_R3.ItemStack;

public class EnchanterListener implements Listener {
	
	public String prefix = TryJump.prefix;
	
	@EventHandler
	public void onBlockclick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
				if(TryJump.phase.equalsIgnoreCase("shopping")) {
				e.setCancelled(true);
				if(p.getItemInHand().getType() != Material.AIR) {
					net.minecraft.server.v1_8_R3.ItemStack nmsitem = org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack.asNMSCopy(p.getItemInHand());
					String enchantCateg = getItemType(nmsitem);
					
					if(enchantCateg != null) {
						int tokenbalance = JumpListener.tokenbalance.get(p.getUniqueId());
							
							if(enchantCateg == "sword") {
								int enchlevel = p.getItemInHand().getEnchantmentLevel(org.bukkit.enchantments.Enchantment.DAMAGE_ALL);
								int ench2level = p.getItemInHand().getEnchantmentLevel(org.bukkit.enchantments.Enchantment.KNOCKBACK);
								
								if(enchlevel < 5) {
									if((tokenbalance >= 400 && nmsitem.hasEnchantments()) || tokenbalance >= 250 && !nmsitem.hasEnchantments()) {
										
										if(nmsitem.hasEnchantments()) {
											JumpListener.tokenbalance.replace(p.getUniqueId(), (tokenbalance-400));
											ShopBoard.changeBalance(p, tokenbalance-400);
										} else {
											JumpListener.tokenbalance.replace(p.getUniqueId(), (tokenbalance-250));
											ShopBoard.changeBalance(p, tokenbalance-250);
										}
										
											if(ench2level < 2) {
												p.getItemInHand().addEnchantment(org.bukkit.enchantments.Enchantment.KNOCKBACK, ench2level+1);
											}
										
											p.getItemInHand().addEnchantment(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, enchlevel+1);
											p.playSound(p.getLocation(), Sound.LEVEL_UP, 6, 6);
									
									} else {
										p.sendMessage(prefix+"§cDu hast nicht genügend Tokens, um dieses Item zu verzaubern!");
										
									}
									
									} else {
										p.sendMessage(prefix+ "§cDieses Item kann nicht mehr verzaubert werden!");
								}
								
							} else {
								if(enchantCateg == "bow") {
									int enchlevel = p.getItemInHand().getEnchantmentLevel(org.bukkit.enchantments.Enchantment.ARROW_DAMAGE);
									int ench2level = p.getItemInHand().getEnchantmentLevel(org.bukkit.enchantments.Enchantment.ARROW_KNOCKBACK);
									
									if(enchlevel < 5) {
										if((tokenbalance >= 400 && nmsitem.hasEnchantments()) || tokenbalance >= 250 && !nmsitem.hasEnchantments()) {
											
											if(nmsitem.hasEnchantments()) {
												JumpListener.tokenbalance.replace(p.getUniqueId(), (tokenbalance-400));
												ShopBoard.changeBalance(p, tokenbalance-400);
											} else {
												JumpListener.tokenbalance.replace(p.getUniqueId(), (tokenbalance-250));
												ShopBoard.changeBalance(p, tokenbalance-250);
											}
											
												if(ench2level < 2) {
													p.getItemInHand().addEnchantment(org.bukkit.enchantments.Enchantment.ARROW_KNOCKBACK, ench2level+1);
												}
											
												p.getItemInHand().addEnchantment(org.bukkit.enchantments.Enchantment.ARROW_DAMAGE, enchlevel+1);
												p.playSound(p.getLocation(), Sound.LEVEL_UP, 6, 6);
										
										} else {
											p.sendMessage(prefix+"§cDu hast nicht genügend Tokens, um dieses Item zu verzaubern!");
											
										}
										
										} else {
											p.sendMessage(prefix+ "§cDieses Item kann nicht mehr verzaubert werden!");
									}
										
									
								} else {
									if(enchantCateg == "armor") {
										int enchlevel = p.getItemInHand().getEnchantmentLevel(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL);
										
										if(enchlevel < 4) {
											if((tokenbalance >= 400 && nmsitem.hasEnchantments()) || tokenbalance >= 250 && !nmsitem.hasEnchantments()) {
												
												if(nmsitem.hasEnchantments()) {
													JumpListener.tokenbalance.replace(p.getUniqueId(), (tokenbalance-400));
													ShopBoard.changeBalance(p, tokenbalance-400);
												} else {
													JumpListener.tokenbalance.replace(p.getUniqueId(), (tokenbalance-250));
													ShopBoard.changeBalance(p, tokenbalance-250);
												}
												
												p.getItemInHand().addEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, enchlevel+1);
												p.playSound(p.getLocation(), Sound.LEVEL_UP, 6, 6);
												
											} else {
												p.sendMessage(prefix+"§cDu hast nicht genügend Tokens, um dieses Item zu verzaubern!");
												
											}
										} else {
											p.sendMessage(prefix+ "§cDieses Item kann nicht mehr verzaubert werden!");
										}
										}
									}
							}
							
						}	
					
				}
				
				}
				
			}

		}
	}
	
	public String getItemType(ItemStack i) {

		Enchantment swordench = Enchantment.FIRE_ASPECT;
		Enchantment bowench = Enchantment.ARROW_FIRE;
		Enchantment armorench = Enchantment.PROTECTION_ENVIRONMENTAL;

		if(bowench.canEnchant(i)) {
			return "bow";
		} else {
			if(swordench.canEnchant(i)) {
				return "sword";
				
				} else {
					if(armorench.canEnchant(i)) {
					return "armor";
					
					} else {
					return null;
					
					}
			}
		}
	
	}

}
