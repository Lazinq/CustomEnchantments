package me.Lazinq.CustomEnchants.objects;
//Slowly regains durability - tools/armor/weapons

import me.Lazinq.CustomEnchants.Constructors.ArmorEquipEvent;
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
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Rejuvination  implements Listener, CommandExecutor {

    private CustomEnchants plugin;

    public Rejuvination(CustomEnchants plugin) {
        this.plugin = plugin;
    }

    public static final Enchantment REJUVINATION = new EnchantMentWrapper("rejuvination", "Rejuvination", 1);

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(REJUVINATION);

        if (!registered) registerEnchantment(REJUVINATION);
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
        if (label.equalsIgnoreCase("rejuvination")) {
            Player player = (Player) sender;
            if (armor.contains(player.getInventory().getItemInMainHand().getType()) || tools.contains(player.getInventory().getItemInMainHand().getType())) {
                if (player.hasPermission(this.plugin.getConfig().getString("Rejuvination.give-perm"))) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    item.addUnsafeEnchantment(REJUVINATION, 1);
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("Rejuvination.loreName")));
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

        if (tools.contains(e.getItem().getType())) {
            if (e.getExpLevelCost() == 30) {
                if (chance >= 0 && chance <=(this.plugin.getConfig().getInt("Rejuviation.chance"))) {
                    ItemStack item = e.getItem();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("Rejuviation.loreName")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item.addUnsafeEnchantment(REJUVINATION, 1);
                }
            }
        }
    }

    private final EnumSet<Material> tools = EnumSet.of(Material.WOODEN_PICKAXE, Material.WOODEN_AXE, Material.WOODEN_SWORD, Material.WOODEN_SHOVEL, Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SWORD, Material.STONE_SHOVEL, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SWORD, Material.IRON_SHOVEL, Material.GOLDEN_PICKAXE, Material.GOLDEN_AXE, Material.GOLDEN_SWORD, Material.GOLDEN_SHOVEL, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SWORD, Material.DIAMOND_SHOVEL, Material.NETHERITE_PICKAXE, Material.NETHERITE_AXE, Material.NETHERITE_SHOVEL);
    private static final EnumSet<Material> armor = EnumSet.of(Material.TURTLE_HELMET, Material.NETHERITE_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET, Material.NETHERITE_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.LEATHER_LEGGINGS, Material.NETHERITE_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS);

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

        if (firstContent.hasEnchant(REJUVINATION)) {
            thirdContent.addUnsafeEnchantment(REJUVINATION, 1);

            if (secondContent.hasEnchant(Telepathy.TELEPATHY)){
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
            if (secondContent.hasEnchant(Agility.AGILITY)){
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
            if (secondContent.hasEnchant(Excavation.EXCAVATION)){
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
            if (secondContent.hasEnchant(Withering.WITHERING)){
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
                if (secondContent.hasEnchant(Life.LIFE)){
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
                if (secondContent.hasEnchant(AutoJump.AUTOJUMP)){
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
                if (secondContent.hasEnchant(AutoSpeed.AUTOSPEED)){
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
                if (secondContent.hasEnchant(Waterbreathing.WATERBREATHING)){
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
            if (secondContent.hasEnchant(WellFed.WELLFED)){
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
            if (secondContent.hasEnchant(ExplosiveArrows.EXPLOSIVEARROWS)){
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
            if (secondContent.hasEnchant(WitheredArrows.WITHEREDARROWS)){
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
            if (secondContent.hasEnchant(MultiShoot.MULTISHOOT)) {
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
    public void onHold(PlayerItemHeldEvent e) {

        if (e.getPlayer().getInventory().getItem(e.getNewSlot()) == null)return;
        if (tools.contains(e.getPlayer().getInventory().getItem(e.getNewSlot()).getType())) {
            if (e.getPlayer().getInventory().getItem(e.getNewSlot()).getItemMeta().hasEnchant(REJUVINATION)) {
                if (e.getPlayer().getGameMode() != GameMode.CREATIVE || e.getPlayer().getGameMode() != GameMode.SPECTATOR) {
                    if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta() instanceof Damageable) {

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (e.getPlayer().getInventory().getItemInMainHand() == null)return;
                                if (!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta())return;
                                if (!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(REJUVINATION))return;

                                ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
                                ((Damageable) meta).setDamage(((Damageable) e.getPlayer().getInventory().getItemInMainHand().getItemMeta()).getDamage() -1);
                                e.getPlayer().getInventory().getItemInMainHand().setItemMeta(meta);
                                }
                        }.runTaskTimer(this.plugin, 0L, 20L);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onWear(ArmorEquipEvent e) {
        Player player = e.getPlayer();
        if(e.getNewArmorPiece() != null) {
            if (armor.contains(e.getNewArmorPiece().getType())) {
                if (e.getNewArmorPiece().getItemMeta().hasEnchant(REJUVINATION)) {
                    if (player.getGameMode() != GameMode.CREATIVE || player.getGameMode() != GameMode.SPECTATOR) {

                        ItemMeta meta = e.getNewArmorPiece().getItemMeta();
                        ((Damageable) meta).setDamage(((Damageable) e.getNewArmorPiece().getItemMeta()).getDamage() -1);
                        e.getNewArmorPiece().setItemMeta(meta);
                    }
                }
            }
        }
    }

    public String applyCC(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}