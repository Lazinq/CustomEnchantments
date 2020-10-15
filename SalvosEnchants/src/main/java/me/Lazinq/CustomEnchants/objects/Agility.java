package me.Lazinq.CustomEnchants.objects;
//Add haste when holding a weapon - sword/axe/shovel/pick

import me.Lazinq.CustomEnchants.Constructors.EnchantMentWrapper;
import me.Lazinq.CustomEnchants.CustomEnchants;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Agility implements Listener, CommandExecutor {

    private CustomEnchants plugin;

    public Agility(CustomEnchants plugin) {
        this.plugin = plugin;
    }

    public static final Enchantment AGILITY = new EnchantMentWrapper("agility", "Agility", 1);

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(AGILITY);

        if (!registered) registerEnchantment(AGILITY);
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
        if (label.equalsIgnoreCase("Agility")) {
            Player player = (Player) sender;
            if (items.contains(player.getInventory().getItemInMainHand().getType())) {
                if (player.hasPermission(this.plugin.getConfig().getString("Agility.give-perm"))) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("Agility.loreName")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item.addUnsafeEnchantment(AGILITY, 1);
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onEnchanting(EnchantItemEvent e) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        final int chance = random.nextInt(100);

        if (items.contains(e.getItem().getType())) {
            if (e.getExpLevelCost() == 30) {
                if (chance >= 0 && chance <= (this.plugin.getConfig().getInt("Agility.chance"))) {
                    ItemStack item = e.getItem();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("Agility.loreName")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item.addUnsafeEnchantment(AGILITY, 1);
                }
            }
        }
    }

    private final EnumSet<Material> items = EnumSet.of(Material.WOODEN_PICKAXE, Material.WOODEN_AXE, Material.WOODEN_SWORD, Material.WOODEN_SHOVEL, Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SWORD, Material.STONE_SHOVEL, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SWORD, Material.IRON_SHOVEL, Material.GOLDEN_PICKAXE, Material.GOLDEN_AXE, Material.GOLDEN_SWORD, Material.GOLDEN_SHOVEL, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SWORD, Material.DIAMOND_SHOVEL, Material.NETHERITE_PICKAXE, Material.NETHERITE_AXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_SWORD);

    @EventHandler
    public void onHold(PlayerItemHeldEvent e) {
        if (e.getPlayer().getInventory().getItem(e.getNewSlot()) == null) {
            e.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
            return;
        }

        if (e.getPlayer().getInventory().getItem(e.getNewSlot()).getItemMeta().hasEnchant(AGILITY)) {
            if (e.getPlayer().getGameMode() != GameMode.CREATIVE || e.getPlayer().getGameMode() != GameMode.SPECTATOR) {
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0));
            }
        } else {
            e.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
        }
    }

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

        if ((firstContent.hasEnchant(Enchantment.ARROW_DAMAGE) || firstContent.hasEnchant(Enchantment.ARROW_FIRE) || firstContent.hasEnchant(Enchantment.ARROW_INFINITE) || firstContent.hasEnchant(Enchantment.ARROW_KNOCKBACK) || firstContent.hasEnchant(Enchantment.BINDING_CURSE) || firstContent.hasEnchant(Enchantment.CHANNELING) || firstContent.hasEnchant(Enchantment.DAMAGE_ALL) || firstContent.hasEnchant(Enchantment.DAMAGE_ARTHROPODS) || firstContent.hasEnchant(Enchantment.DAMAGE_UNDEAD) || firstContent.hasEnchant(Enchantment.DEPTH_STRIDER) || firstContent.hasEnchant(Enchantment.DIG_SPEED) || firstContent.hasEnchant(Enchantment.DURABILITY) || firstContent.hasEnchant(Enchantment.FIRE_ASPECT) || firstContent.hasEnchant(Enchantment.FROST_WALKER) || firstContent.hasEnchant(Enchantment.IMPALING) || firstContent.hasEnchant(Enchantment.KNOCKBACK) || firstContent.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS) || firstContent.hasEnchant(Enchantment.LOOT_BONUS_MOBS) || firstContent.hasEnchant(Enchantment.LOYALTY) || firstContent.hasEnchant(Enchantment.LUCK) || firstContent.hasEnchant(Enchantment.LURE) || firstContent.hasEnchant(Enchantment.MENDING) || firstContent.hasEnchant(Enchantment.MULTISHOT) || firstContent.hasEnchant(Enchantment.OXYGEN) || firstContent.hasEnchant(Enchantment.PIERCING) || firstContent.hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL) || firstContent.hasEnchant(Enchantment.PROTECTION_EXPLOSIONS) || firstContent.hasEnchant(Enchantment.PROTECTION_FALL) || firstContent.hasEnchant(Enchantment.PROTECTION_FIRE) || firstContent.hasEnchant(Enchantment.PROTECTION_PROJECTILE) || firstContent.hasEnchant(Enchantment.QUICK_CHARGE) || firstContent.hasEnchant(Enchantment.RIPTIDE) || firstContent.hasEnchant(Enchantment.SILK_TOUCH) || firstContent.hasEnchant(Enchantment.SOUL_SPEED) || firstContent.hasEnchant(Enchantment.SWEEPING_EDGE) || firstContent.hasEnchant(Enchantment.THORNS) || firstContent.hasEnchant(Enchantment.VANISHING_CURSE) || firstContent.hasEnchant(Enchantment.WATER_WORKER)) &&
                (secondContent.hasEnchant(Enchantment.ARROW_DAMAGE) || secondContent.hasEnchant(Enchantment.ARROW_FIRE) || secondContent.hasEnchant(Enchantment.ARROW_INFINITE) || secondContent.hasEnchant(Enchantment.ARROW_KNOCKBACK) || secondContent.hasEnchant(Enchantment.BINDING_CURSE) || secondContent.hasEnchant(Enchantment.CHANNELING) || secondContent.hasEnchant(Enchantment.DAMAGE_ALL) || secondContent.hasEnchant(Enchantment.DAMAGE_ARTHROPODS) || secondContent.hasEnchant(Enchantment.DAMAGE_UNDEAD) || secondContent.hasEnchant(Enchantment.DEPTH_STRIDER) || secondContent.hasEnchant(Enchantment.DIG_SPEED) || secondContent.hasEnchant(Enchantment.DURABILITY) || secondContent.hasEnchant(Enchantment.FIRE_ASPECT) || secondContent.hasEnchant(Enchantment.FROST_WALKER) || secondContent.hasEnchant(Enchantment.IMPALING) || secondContent.hasEnchant(Enchantment.KNOCKBACK) || secondContent.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS) || secondContent.hasEnchant(Enchantment.LOOT_BONUS_MOBS) || secondContent.hasEnchant(Enchantment.LOYALTY) || secondContent.hasEnchant(Enchantment.LUCK) || secondContent.hasEnchant(Enchantment.LURE) || secondContent.hasEnchant(Enchantment.MENDING) || secondContent.hasEnchant(Enchantment.MULTISHOT) || secondContent.hasEnchant(Enchantment.OXYGEN) || secondContent.hasEnchant(Enchantment.PIERCING) || secondContent.hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL) || secondContent.hasEnchant(Enchantment.PROTECTION_EXPLOSIONS) || secondContent.hasEnchant(Enchantment.PROTECTION_FALL) || secondContent.hasEnchant(Enchantment.PROTECTION_FIRE) || secondContent.hasEnchant(Enchantment.PROTECTION_PROJECTILE) || secondContent.hasEnchant(Enchantment.QUICK_CHARGE) || secondContent.hasEnchant(Enchantment.RIPTIDE) || secondContent.hasEnchant(Enchantment.SILK_TOUCH) || secondContent.hasEnchant(Enchantment.SOUL_SPEED) || secondContent.hasEnchant(Enchantment.SWEEPING_EDGE) || secondContent.hasEnchant(Enchantment.THORNS) || secondContent.hasEnchant(Enchantment.VANISHING_CURSE) || secondContent.hasEnchant(Enchantment.WATER_WORKER)))
            return;

        if (firstContent.hasEnchant(AGILITY)) {
            thirdContent.addUnsafeEnchantment(AGILITY, 1);

            if (secondContent.hasEnchant(AutoMelting.AUTOMELTING)) {
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
            if(secondContent.hasEnchant(Telepathy.TELEPATHY))

    {
        thirdContent.addUnsafeEnchantment(Telepathy.TELEPATHY, 1);
        ItemMeta meta = thirdContent.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (meta.hasLore()) {
            lore = meta.getLore();
        }
        if (!lore.contains(applyCC(this.plugin.getConfig().getString("Telepathy.loreName")))) {
        lore.add(applyCC(this.plugin.getConfig().getString("Telepathy.loreName")));
        meta.setLore(lore);
        thirdContent.setItemMeta(meta);
    }
}
            if (secondContent.hasEnchant(Blindness.BLINDNESS)) {
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
        thirdContent.addUnsafeEnchantment(LifeLeech.LIFELEECH,1);
        ItemMeta meta=thirdContent.getItemMeta();
        List<String> lore=new ArrayList<>();
        if(meta.hasLore()){
        lore=meta.getLore();
        }
        if(!lore.contains(applyCC(this.plugin.getConfig().getString("LifeLeech.loreName")))){
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

    public String applyCC(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
