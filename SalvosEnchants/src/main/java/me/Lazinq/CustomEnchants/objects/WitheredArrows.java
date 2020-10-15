package me.Lazinq.CustomEnchants.objects;
//Wither enemys - bow

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

public class WitheredArrows implements Listener, CommandExecutor {

    private CustomEnchants plugin;

    public WitheredArrows(CustomEnchants plugin) {
        this.plugin = plugin;
    }

    public static final Enchantment WITHEREDARROWS = new EnchantMentWrapper("witheredarrows", "WitheredArrows", 1);

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(WITHEREDARROWS);

        if (!registered) registerEnchantment(WITHEREDARROWS);
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
        if (label.equalsIgnoreCase("WitheredArrows")) {
            Player player = (Player) sender;
            if (player.getInventory().getItemInMainHand().getType() == Material.BOW) {
                if (player.hasPermission(this.plugin.getConfig().getString("WitheredArrows.give-perm"))) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    item.addUnsafeEnchantment(WITHEREDARROWS, 1);
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("WitheredArrows.loreName")));
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

        if (e.getItem().getType() == Material.BOW) {
            if (e.getExpLevelCost() == 30) {
                if (chance >= 0 && chance <=(this.plugin.getConfig().getInt("WitheredArrows.chance"))) {
                    ItemStack item = e.getItem();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("WitheredArrows.loreName")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item.addUnsafeEnchantment(WITHEREDARROWS, 1);
                }
            }
        }
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

        if (firstContent.hasEnchant(WITHEREDARROWS)) {
            thirdContent.addUnsafeEnchantment(WITHEREDARROWS, 1);

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
            if (secondContent.hasEnchant(FlashingArrows.FLASHINGARROWS)){
                thirdContent.addUnsafeEnchantment(FlashingArrows.FLASHINGARROWS, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("FlashingArrows.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("FlashingArrows.loreName")));
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
    public void onDamage(ProjectileHitEvent e) {
        if (!(e.getHitEntity() instanceof Player))return;
        Player ent = (Player) e.getHitEntity();

        Player player = (Player) e.getEntity().getShooter();
        if (!player.getInventory().getItemInMainHand().hasItemMeta())return;

        Player p = (Player) e.getEntity().getShooter();
        if (p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(WITHEREDARROWS)) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            final int chance = random.nextInt(100);
            if (chance <= this.plugin.getConfig().getInt("WitheredArrows.add-chance")) {
                ent.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, this.plugin.getConfig().getInt("WitheredArrows.time-seconds") * 20, 0));
            }
        }
    }


    public String applyCC(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}