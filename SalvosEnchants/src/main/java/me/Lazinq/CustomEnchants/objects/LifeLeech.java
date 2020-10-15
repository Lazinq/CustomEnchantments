package me.Lazinq.CustomEnchants.objects;

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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
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

//Extra damage when hitting - sword/axe

public class LifeLeech implements Listener, CommandExecutor {

    private CustomEnchants plugin;

    public LifeLeech(CustomEnchants plugin) {
        this.plugin = plugin;
    }

    public static final Enchantment LIFELEECH = new EnchantMentWrapper("lifeleech", "LifeLeech", 1);

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(LIFELEECH);

        if (!registered) registerEnchantment(LIFELEECH);
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
        if (label.equalsIgnoreCase("LifeLeech")) {
            Player player = (Player) sender;
            if (items.contains(player.getInventory().getItemInMainHand().getType())) {
                if (player.hasPermission(this.plugin.getConfig().getString("LifeLeech.give-perm"))) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    item.addUnsafeEnchantment(LIFELEECH, 1);
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("LifeLeech.loreName")));
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

        if (firstContent.hasEnchant(LIFELEECH)) {
            thirdContent.addUnsafeEnchantment(LIFELEECH, 1);

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
            if (secondContent.hasEnchant(Telepathy.TELEPATHY)) {
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
            if (secondContent.hasEnchant(Luck.LUCK)) {
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
    public void onEnchanting(EnchantItemEvent e){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        final int chance = random.nextInt(100);

        if (items.contains(e.getItem().getType())) {
            if (e.getExpLevelCost() == 30) {
                if (chance >= 0 && chance <=(this.plugin.getConfig().getInt("Agility.chance"))) {
                    ItemStack item = e.getItem();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("Agility.loreName")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item.addUnsafeEnchantment(LIFELEECH, 1);
                }
            }
        }
    }

    private final EnumSet<Material> items = EnumSet.of(Material.WOODEN_PICKAXE, Material.WOODEN_AXE, Material.WOODEN_SWORD, Material.WOODEN_SHOVEL, Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SWORD, Material.STONE_SHOVEL, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SWORD, Material.IRON_SHOVEL, Material.GOLDEN_PICKAXE, Material.GOLDEN_AXE, Material.GOLDEN_SWORD, Material.GOLDEN_SHOVEL, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SWORD, Material.DIAMOND_SHOVEL, Material.NETHERITE_PICKAXE, Material.NETHERITE_AXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_SWORD);

    @EventHandler
    public void onHold(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player))return;
        if (!(e.getEntity() instanceof Player))return;
        Player ent = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();
        if (damager.getInventory().getItemInMainHand() == null)return;
        if (!damager.getInventory().getItemInMainHand().hasItemMeta())return;


        if (damager.getInventory().getItemInMainHand().getItemMeta().hasEnchant(LIFELEECH)) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            final int chance = random.nextInt(100);
            if (chance <= this.plugin.getConfig().getInt("LifeLeech.add-chance")) {
                final boolean oneOrTwo = random.nextBoolean();
                if (oneOrTwo) {
                    ent.setHealth(ent.getHealth()-this.plugin.getConfig().getInt("LifeLeech.remove-amount-half-hearts-chance1"));
                } else {
                    ent.setHealth(ent.getHealth()-this.plugin.getConfig().getInt("LifeLeech.remove-amount-half-hearts-chance2"));
                }
            }
        }
    }

    public String applyCC(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
