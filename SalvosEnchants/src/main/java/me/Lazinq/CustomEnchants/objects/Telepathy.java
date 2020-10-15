package me.Lazinq.CustomEnchants.objects;
//Mined blocks get into inv - pick/axe/shovel

import me.Lazinq.CustomEnchants.Constructors.EnchantMentWrapper;
import me.Lazinq.CustomEnchants.CustomEnchants;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Telepathy implements Listener, CommandExecutor {

    private CustomEnchants plugin;
    public Telepathy(CustomEnchants plugin) {
        this.plugin = plugin;
    }

    public static final Enchantment TELEPATHY = new EnchantMentWrapper("telepathy", "Telepathy", 1);

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(TELEPATHY);

        if (!registered) registerEnchantment(TELEPATHY);
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
        if (label.equalsIgnoreCase("Telepathy")) {
            Player player = (Player) sender;
            if (items.contains(player.getInventory().getItemInMainHand().getType())) {
                if (player.hasPermission(this.plugin.getConfig().getString("Telepathy.give-perm"))) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    item.addUnsafeEnchantment(TELEPATHY, 1);
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("Telepathy.loreName")));
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
        Player player = e.getEnchanter();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        final int chance = random.nextInt(100);

        if (items.contains(e.getItem().getType())) {
            if (e.getExpLevelCost() == 30) {
                if (chance >= 0 && chance <=(this.plugin.getConfig().getInt("Telepathy.chance"))) {
                    ItemStack item = e.getItem();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("Telepathy.loreName")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item.addUnsafeEnchantment(TELEPATHY, 1);
                }
            }
        }
    }

    private static final EnumSet<Material> items = EnumSet.of(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_PICKAXE, Material.NETHERITE_AXE, Material.NETHERITE_SHOVEL);

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

        if (firstContent.hasEnchant(TELEPATHY)) {
            thirdContent.addUnsafeEnchantment(TELEPATHY, 1);

            if (secondContent.hasEnchant(Agility.AGILITY)) {
                thirdContent.addUnsafeEnchantment(Agility.AGILITY, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("Agility.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("Agility.loreName")));
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
            if (secondContent.hasEnchant(AutoMelting.AUTOMELTING)){
                thirdContent.addUnsafeEnchantment(AutoMelting.AUTOMELTING, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("AutoMelting.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("AutoMelting.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
            if (secondContent.hasEnchant(Excavation.EXCAVATION)) {
                thirdContent.addUnsafeEnchantment(Excavation.EXCAVATION, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("Excavation.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("Excavation.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
            if (secondContent.hasEnchant(Blindness.BLINDNESS)){
                thirdContent.addUnsafeEnchantment(Blindness.BLINDNESS, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("Blindness.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("Blindness.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
            if (secondContent.hasEnchant(Withering.WITHERING)) {
                thirdContent.addUnsafeEnchantment(Withering.WITHERING, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("Withering.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("Withering.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
            if (secondContent.hasEnchant(LifeLeech.LIFELEECH)){
                thirdContent.addUnsafeEnchantment(LifeLeech.LIFELEECH, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("LifeLeech.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("LifeLeech.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
            if (secondContent.hasEnchant(IceAspect.ICEASPECT)){
                thirdContent.addUnsafeEnchantment(IceAspect.ICEASPECT, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("IceAspect.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("IceAspect.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
            if (secondContent.hasEnchant(Luck.LUCK)){
                thirdContent.addUnsafeEnchantment(Luck.LUCK, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("Luck.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("Luck.loreName")));
                    meta.setLore(lore);
                    thirdContent.setItemMeta(meta);
                }
            }
        }
    }

    @EventHandler
    public void onB(BlockBreakEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand() == null)return;
        if (!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta())return;
        if (!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(Telepathy.TELEPATHY))return;
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE || e.getPlayer().getGameMode() == GameMode.SPECTATOR)return;
        if (e.getPlayer().getInventory().firstEmpty() == -1)return;
        if (e.getBlock().getState() instanceof Container)return;
        e.setDropItems(false);
        Collection<ItemStack> drops = e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand());
        if (drops.isEmpty())return;
        e.getPlayer().getInventory().addItem(drops.iterator().next());
    }

    public String applyCC(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}
