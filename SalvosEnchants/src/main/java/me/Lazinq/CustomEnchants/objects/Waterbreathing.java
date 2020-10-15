package me.Lazinq.CustomEnchants.objects;
//gives waterbreathing - helmet

import me.Lazinq.CustomEnchants.Constructors.ArmorEquipEvent;
import me.Lazinq.CustomEnchants.Constructors.EnchantMentWrapper;
import me.Lazinq.CustomEnchants.CustomEnchants;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Waterbreathing implements Listener, CommandExecutor {

    private CustomEnchants plugin;

    public Waterbreathing(CustomEnchants plugin) {
        this.plugin = plugin;
    }

    public static final Enchantment WATERBREATHING = new EnchantMentWrapper("waterbreathing", "WaterBreathing", 1);

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(WATERBREATHING);

        if (!registered) registerEnchantment(WATERBREATHING);
    }

    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e){
            registered = false;
            e.printStackTrace();
        }
        if (registered){

        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("WaterBreathing")) {
            Player player = (Player) sender;
            if (items.contains(player.getInventory().getItemInMainHand().getType())) {
                if (player.hasPermission(this.plugin.getConfig().getString("WaterBreathing.give-perm"))) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    item.addUnsafeEnchantment(WATERBREATHING, 1);
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("WaterBreathing.loreName")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onEnchanting(EnchantItemEvent e){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        final int chance = random.nextInt(100);

        if (items.contains(e.getItem().getType())) {
            if (e.getExpLevelCost() == 30) {
                if (chance >= 0 && chance <=(this.plugin.getConfig().getInt("WaterBreathing.chance"))) {
                    ItemStack item = e.getItem();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("WaterBreathing.loreName")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item.addUnsafeEnchantment(WATERBREATHING, 1);
                }
            }
        }
    }

    private static final EnumSet<Material> items = EnumSet.of(Material.TURTLE_HELMET, Material.NETHERITE_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET);

    @EventHandler
    public void onAnvil(PrepareAnvilEvent e) {
        if (e.getInventory().getStorageContents()[0] == null)return;
        if (!e.getInventory().getStorageContents()[0].hasItemMeta())return;
        if (e.getInventory().getStorageContents()[1] == null)return;
        if (!e.getInventory().getStorageContents()[1].hasItemMeta())return;
        if (e.getResult() == null)return;
        if (!e.getResult().hasItemMeta())return;

        ItemMeta firstContent = e.getInventory().getStorageContents()[0].getItemMeta();
        ItemMeta secondContent = e.getInventory().getStorageContents()[1].getItemMeta();
        ItemStack thirdContent = e.getResult();

        if (firstContent.hasEnchant(WATERBREATHING)) {
            thirdContent.addUnsafeEnchantment(WATERBREATHING, 1);

            if (secondContent.hasEnchant(Rejuvination.REJUVINATION)){
                thirdContent.addUnsafeEnchantment(Rejuvination.REJUVINATION, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("Rejuvination.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("Rejuvination.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
            if (secondContent.hasEnchant(NightVision.NIGHTVISION)) {
                thirdContent.addUnsafeEnchantment(NightVision.NIGHTVISION, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("NightVision.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("NightVision.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
            if (secondContent.hasEnchant(Regeneration.REGENERATION)){
                thirdContent.addUnsafeEnchantment(Regeneration.REGENERATION, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("Regeneration.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("Regeneration.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
            if (secondContent.hasEnchant(Life.LIFE)) {
                thirdContent.addUnsafeEnchantment(Life.LIFE, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("Life.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("Life.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
            if (secondContent.hasEnchant(WellFed.WELLFED)) {
                thirdContent.addUnsafeEnchantment(WellFed.WELLFED, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("WellFed.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("WellFed.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
            if (secondContent.hasEnchant(Guards.GUARDS)){
                thirdContent.addUnsafeEnchantment(Guards.GUARDS, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("Guards.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("Guards.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
        }
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e) {

        Player player = e.getPlayer();
        if(e.getNewArmorPiece() != null) {
            if (items.contains(e.getNewArmorPiece().getType())) {
                if (e.getNewArmorPiece().getItemMeta().hasEnchant(WATERBREATHING)) {
                    if (player.getGameMode() != GameMode.CREATIVE || player.getGameMode() != GameMode.SPECTATOR) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0));
                    }
                }
            }
        }
        if(e.getOldArmorPiece() != null) {
            if (items.contains(e.getOldArmorPiece().getType())) {
                if (e.getOldArmorPiece().getItemMeta().hasEnchant(WATERBREATHING)) {
                    player.removePotionEffect(PotionEffectType.WATER_BREATHING);
                }
            }
        }
    }

    public String applyCC(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}