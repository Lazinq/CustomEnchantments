package me.Lazinq.CustomEnchants.objects;
//Chance to spawn iron golem when attacked - armor

import me.Lazinq.CustomEnchants.Constructors.EnchantMentWrapper;
import me.Lazinq.CustomEnchants.CustomEnchants;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Guards implements Listener, CommandExecutor {

    private CustomEnchants plugin;
    public Guards(CustomEnchants plugin) {
        this.plugin = plugin;
    }

    public static final Enchantment GUARDS = new EnchantMentWrapper("guards", "Guards", 1);

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(GUARDS);

        if (!registered) registerEnchantment(GUARDS);
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
        if (label.equalsIgnoreCase("Guards")) {
            Player player = (Player) sender;
            if (armor.contains(player.getInventory().getItemInMainHand().getType())) {
                if (player.hasPermission(this.plugin.getConfig().getString("Guards.give-perm"))) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    item.addUnsafeEnchantment(GUARDS, 1);
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("Guards.loreName")));
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

        if (firstContent.hasEnchant(GUARDS)) {
            thirdContent.addUnsafeEnchantment(GUARDS, 1);

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
            if (secondContent.hasEnchant(AutoJump.AUTOJUMP)) {
                thirdContent.addUnsafeEnchantment(AutoJump.AUTOJUMP, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("AutoJump.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("AutoJump.loreName")));
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
            if (secondContent.hasEnchant(Waterbreathing.WATERBREATHING)) {
                thirdContent.addUnsafeEnchantment(Waterbreathing.WATERBREATHING, 1);
                ItemMeta meta = thirdContent.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (!lore.contains(applyCC(this.plugin.getConfig().getString("WaterBreathing.loreName")))) {
                    lore.add(applyCC(this.plugin.getConfig().getString("WaterBreathing.loreName")));
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
        }
    }

    @EventHandler
    public void onEnchanting(EnchantItemEvent e){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        final int chance = random.nextInt(100);

        if (armor.contains(e.getItem().getType())) {
            if (e.getExpLevelCost() == 30) {
                if (chance >= 0 && chance <=(this.plugin.getConfig().getInt("Guards.chance"))) {
                    ItemStack item = e.getItem();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("Guards.loreName")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item.addUnsafeEnchantment(GUARDS, 1);
                }
            }
        }
    }

    private static final EnumSet<Material> armor = EnumSet.of(Material.TURTLE_HELMET, Material.NETHERITE_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET, Material.NETHERITE_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.LEATHER_LEGGINGS, Material.NETHERITE_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS);

    @EventHandler
    public void onHold(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player))return;
        if (!(e.getEntity() instanceof Player))return;
        Player ent = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();
        if (ent.getInventory().getHelmet() == null)return;
        if (ent.getInventory().getChestplate() == null)return;
        if (ent.getInventory().getLeggings() == null)return;
        if (ent.getInventory().getBoots() == null)return;
        if (!ent.getInventory().getHelmet().hasItemMeta())return;
        if (!ent.getInventory().getChestplate().hasItemMeta())return;
        if (!ent.getInventory().getLeggings().hasItemMeta())return;
        if (!ent.getInventory().getBoots().hasItemMeta())return;

        if (ent.getInventory().getHelmet().getItemMeta().hasEnchant(GUARDS) || ent.getInventory().getChestplate().getItemMeta().hasEnchant(GUARDS) || ent.getInventory().getLeggings().getItemMeta().hasEnchant(GUARDS) || ent.getInventory().getBoots().getItemMeta().hasEnchant(GUARDS)) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            final int chance = random.nextInt(100);
            if (chance <= 7) {
                IronGolem ent1 = (IronGolem) ent.getWorld().spawnEntity(ent.getLocation(), EntityType.IRON_GOLEM);
                ent1.setTarget(damager);
                ent1.setHealth(ent1.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 2);
                ent1.setCustomName(applyCC(this.plugin.getConfig().getString("Guards.nameTag")));
                ent1.setCustomNameVisible(this.plugin.getConfig().getBoolean("Guards.nameTagEnabled"));

                Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
                    public void run() {
                        ent1.remove();
                    }
                }, this.plugin.getConfig().getInt("Guards.RemoveAfterSeconds") * 20);
            }
        }
    }

    public String applyCC(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
