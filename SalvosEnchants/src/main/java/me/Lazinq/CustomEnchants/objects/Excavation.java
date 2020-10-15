package me.Lazinq.CustomEnchants.objects;
//Digs 3x3 area - pickaxe/shovel/axe

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.cause.Cause;
import com.sk89q.worldguard.bukkit.event.block.BreakBlockEvent;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.Lazinq.CustomEnchants.Constructors.EnchantMentWrapper;
import me.Lazinq.CustomEnchants.CustomEnchants;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R1.ChunkConverterPalette;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

public class Excavation implements Listener, CommandExecutor {

    private CustomEnchants plugin;

    public Excavation(CustomEnchants plugin) {
        this.plugin = plugin;
    }

    public static final Enchantment EXCAVATION = new EnchantMentWrapper("excavation", "Excavation", 1);

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(EXCAVATION);

        if (!registered) registerEnchantment(EXCAVATION);
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

    private final EnumSet<Material> items = EnumSet.of(Material.WOODEN_PICKAXE, Material.WOODEN_AXE, Material.WOODEN_SHOVEL, Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SHOVEL, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SHOVEL, Material.GOLDEN_PICKAXE, Material.GOLDEN_AXE, Material.GOLDEN_SHOVEL, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SHOVEL, Material.NETHERITE_PICKAXE, Material.NETHERITE_AXE, Material.NETHERITE_SHOVEL);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("Excavation")) {
            Player player = (Player) sender;
            if (items.contains(player.getInventory().getItemInMainHand().getType())) {
                if (player.hasPermission(this.plugin.getConfig().getString("Excavation.give-perm"))) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("Excavation.loreName")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item.addUnsafeEnchantment(EXCAVATION, 1);
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
                if (chance >= 0 && chance <=(this.plugin.getConfig().getInt("Excavation.chance"))) {
                    ItemStack item = e.getItem();
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    if (meta.hasLore()) {
                        lore = meta.getLore();
                    }
                    lore.add(applyCC(this.plugin.getConfig().getString("Excavation.loreName")));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    item.addUnsafeEnchantment(EXCAVATION, 1);
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

        if (firstContent.hasEnchant(EXCAVATION)) {
            thirdContent.addUnsafeEnchantment(EXCAVATION, 1);

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
            if (secondContent.hasEnchant(LifeLeech.LIFELEECH)) {
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
            if (secondContent.hasEnchant(IceAspect.ICEASPECT)) {
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
    public void onHold(BlockBreakEvent e) {
        if (!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta())return;
        Block block = e.getBlock();
        Location loc = block.getLocation();
        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(EXCAVATION)) {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regions = container.get(new BukkitWorld(e.getPlayer().getWorld()));
            ApplicableRegionSet regionset = regions.getApplicableRegions(BukkitAdapter.asBlockVector(e.getBlock().getLocation()));
            ApplicableRegionSet regionset1 = regions.getApplicableRegions(BukkitAdapter.asBlockVector(e.getBlock().getLocation().add(1, 0, 0)));
            ApplicableRegionSet regionset2 = regions.getApplicableRegions(BukkitAdapter.asBlockVector(e.getBlock().getLocation().add(0, 0, 1)));
            ApplicableRegionSet regionset3 = regions.getApplicableRegions(BukkitAdapter.asBlockVector(e.getBlock().getLocation().subtract(1, 0, 0)));
            ApplicableRegionSet regionset4 = regions.getApplicableRegions(BukkitAdapter.asBlockVector(e.getBlock().getLocation().subtract(0, 0, 1)));
            ApplicableRegionSet regionset5 = regions.getApplicableRegions(BukkitAdapter.asBlockVector(e.getBlock().getLocation().add(1, 0, 1)));
            ApplicableRegionSet regionset6 = regions.getApplicableRegions(BukkitAdapter.asBlockVector(e.getBlock().getLocation().subtract(1, 0, 1)));
            if (regionset.size() > 0 || regionset1.size() > 0 || regionset2.size() > 0 || regionset3.size() > 0 || regionset4.size() > 0 || regionset5.size() > 0 || regionset6.size() > 0) {
                e.setCancelled(true);
                return;
            }

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    final Block locBlock = loc.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
                    if (locBlock.getType() == Material.BEDROCK || locBlock.getType() == Material.WATER || locBlock.getType() == Material.LAVA || locBlock.getType() == Material.END_PORTAL_FRAME) {
                        continue;
                    }
                    locBlock.breakNaturally();
                    }
                }
            }
        }
    }

    public String applyCC(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}

