package me.Lazinq.CustomEnchants.objects;
//Gives Jump Boost - Boots

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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.accessibility.AccessibleComponent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class AutoJump implements Listener, CommandExecutor {

    private CustomEnchants plugin;

    public AutoJump(CustomEnchants plugin) {
        this.plugin = plugin;
    }

    public static final Enchantment AUTOJUMP = new EnchantMentWrapper("autojump", "AutoJump", 1);

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(AUTOJUMP);

        if (!registered) registerEnchantment(AUTOJUMP);
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
        if (label.equalsIgnoreCase("AutoJump")) {
            Player player = (Player) sender;
            if (items.contains(player.getInventory().getItemInMainHand().getType())) {
                if (player.hasPermission(this.plugin.getConfig().getString("AutoJump.give-perm"))) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    item.addUnsafeEnchantment(AUTOJUMP, 1);
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("AutoJump.loreName")));
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
                if (chance >= 0 && chance <=(this.plugin.getConfig().getInt("AutoJump.chance"))) {
                    ItemStack item = e.getItem();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("AutoJump.loreName")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item.addUnsafeEnchantment(AUTOJUMP, 1);
                }
            }
        }
    }

    private static final EnumSet<Material> items = EnumSet.of(Material.DIAMOND_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.NETHERITE_BOOTS);

    @EventHandler
    public void onAnvil(PrepareAnvilEvent e) {
        if (e.getInventory().getStorageContents()[0] == null) return;
        if (!e.getInventory().getStorageContents()[0].hasItemMeta()) return;
        if (e.getInventory().getStorageContents()[1] == null) return;
        if (!e.getInventory().getStorageContents()[1].hasItemMeta()) return;
        if (e.getResult() == null) return;
        if (!e.getResult().hasItemMeta()) return;

        ItemMeta firstContent = e.getInventory().getStorageContents()[0].getItemMeta();
        ItemMeta secondContent = e.getInventory().getStorageContents()[1].getItemMeta();
        ItemStack thirdContent = e.getResult();

        if (firstContent.hasEnchant(AUTOJUMP)) {
            thirdContent.addUnsafeEnchantment(AUTOJUMP, 1);

            if (secondContent.hasEnchant(Rejuvination.REJUVINATION)) {
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
            if (secondContent.hasEnchant(Regeneration.REGENERATION)) {
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
            if (secondContent.hasEnchant(Guards.GUARDS)) {
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
            if (secondContent.hasEnchant(AutoSpeed.AUTOSPEED)) {
                thirdContent.addUnsafeEnchantment(AutoSpeed.AUTOSPEED, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("AutoSpeed.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("AutoSpeed.loreName")));
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
                if (e.getNewArmorPiece().getItemMeta().hasEnchant(AUTOJUMP)) {
                    if (player.getGameMode() != GameMode.CREATIVE || player.getGameMode() != GameMode.SPECTATOR) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 0));
                    }
                }
            }
        }
        if(e.getOldArmorPiece() != null) {
            if (items.contains(e.getOldArmorPiece().getType())) {
                if (e.getOldArmorPiece().getItemMeta().hasEnchant(AUTOJUMP)) {
                    player.removePotionEffect(PotionEffectType.JUMP);
                    }
                }
            }
}

    public String applyCC(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
