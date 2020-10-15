package me.Lazinq.CustomEnchants.objects;
//Blindness when hitting enemy - bows

import me.Lazinq.CustomEnchants.Constructors.EnchantMentWrapper;
import me.Lazinq.CustomEnchants.CustomEnchants;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
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


public class FlashingArrows implements Listener, CommandExecutor {

    private CustomEnchants plugin;

    public FlashingArrows(CustomEnchants plugin) {
        this.plugin = plugin;
    }

    public static final Enchantment FLASHINGARROWS = new EnchantMentWrapper("flashingarrows", "FlashingArrows", 1);

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(FLASHINGARROWS);

        if (!registered) registerEnchantment(FLASHINGARROWS);
    }

    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if (registered) {

        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("FlashingArrows")) {
            Player player = (Player) sender;
            if (player.getInventory().getItemInMainHand().getType() == Material.BOW) {
                if (player.hasPermission(this.plugin.getConfig().getString("FlashingArrows.give-perm"))) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    item.addUnsafeEnchantment(FLASHINGARROWS, 1);
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("FlashingArrows.loreName")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    return true;
                }
            }
        }
        return false;
    }

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

        if (firstContent.hasEnchant(FLASHINGARROWS)) {
            thirdContent.addUnsafeEnchantment(FLASHINGARROWS, 1);

            if (secondContent.hasEnchant(ExplosiveArrows.EXPLOSIVEARROWS)) {
                thirdContent.addUnsafeEnchantment(ExplosiveArrows.EXPLOSIVEARROWS, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("ExplosiveArrows.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("ExplosiveArrows.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
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
            if (secondContent.hasEnchant(WitheredArrows.WITHEREDARROWS)) {
                thirdContent.addUnsafeEnchantment(WitheredArrows.WITHEREDARROWS, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("WitheredArrows.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("WitheredArrows.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
            if (secondContent.hasEnchant(MultiShoot.MULTISHOOT)){
                thirdContent.addUnsafeEnchantment(MultiShoot.MULTISHOOT, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("MultiShoot.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("MultiShoot.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
        }
    }

    @EventHandler
    public void onEnchanting(EnchantItemEvent e) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        final int chance = random.nextInt(100);

        if (e.getItem().getType() == Material.BOW) {
            if (e.getExpLevelCost() == 30) {
                if (chance >= 0 && chance <= (this.plugin.getConfig().getInt("FlashingArrows.chance"))) {
                    ItemStack item = e.getItem();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("FlashingArrows.lorename")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item.addUnsafeEnchantment(FLASHINGARROWS, 1);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(ProjectileHitEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player))return;
        if (!(e.getHitEntity() instanceof Player))return;
        Player player = (Player) e.getEntity().getShooter();

        if (player.getInventory().getItemInMainHand() == null)return;
        if (!player.getInventory().getItemInMainHand().hasItemMeta())return;

        Player ent = (Player) e.getHitEntity();
        Player p = (Player) e.getEntity().getShooter();
        if (p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(FLASHINGARROWS)) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            final int chance = random.nextInt(100);
            if (chance <= this.plugin.getConfig().getInt("FlashingArrows.add-chance"))
            ent.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, this.plugin.getConfig().getInt("FlashingArrows.time-seconds") * 20, 0));
        }
    }


    public String applyCC(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}