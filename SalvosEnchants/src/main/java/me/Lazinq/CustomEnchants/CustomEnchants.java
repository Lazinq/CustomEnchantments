package me.Lazinq.CustomEnchants;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.Lazinq.CustomEnchants.Constructors.ArmorListener;
import me.Lazinq.CustomEnchants.Constructors.DispenserArmorListener;
import me.Lazinq.CustomEnchants.objects.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Explosive;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CustomEnchants extends JavaPlugin implements Listener {

    private WorldGuardPlugin worldGuardPlugin;

    public void onEnable() {
        worldGuardPlugin = getWorldGuard();
        Agility.register();
        AutoJump.register();
        AutoMelting.register();
        AutoSpeed.register();
        Blindness.register();
        Excavation.register();
        ExplosiveArrows.register();
        FlashingArrows.register();
        Guards.register();
        IceAspect.register();
        Life.register();
        LifeLeech.register();
        Luck.register();
        MultiShoot.register();
        NightVision.register();
        Regeneration.register();
        Rejuvination.register();
        Telepathy.register();
        Waterbreathing.register();
        WellFed.register();
        WitheredArrows.register();
        Withering.register();
        getCommand("agility").setExecutor(new Agility(this));
        getCommand("autojump").setExecutor(new AutoJump(this));
        getCommand("automelting").setExecutor(new AutoMelting(this));
        getCommand("autospeed").setExecutor(new AutoSpeed(this));
        getCommand("blindness").setExecutor(new Blindness(this));
        getCommand("excavation").setExecutor(new Excavation(this));
        getCommand("explosivearrows").setExecutor(new ExplosiveArrows(this));
        getCommand("flashingarrows").setExecutor(new FlashingArrows(this));
        getCommand("guards").setExecutor(new Guards(this));
        getCommand("iceaspect").setExecutor(new IceAspect(this));
        getCommand("life").setExecutor(new Life(this));
        getCommand("lifeleech").setExecutor(new LifeLeech(this));
        getCommand("luck").setExecutor(new Luck(this));
        getCommand("multishoot").setExecutor(new MultiShoot(this));
        getCommand("nightvision").setExecutor(new NightVision(this));
        getCommand("regeneration").setExecutor(new Regeneration(this));
        getCommand("rejuvination").setExecutor(new Rejuvination(this));
        getCommand("telepathy").setExecutor(new Telepathy(this));
        getCommand("waterbreathing").setExecutor(new Waterbreathing(this));
        getCommand("wellfed").setExecutor(new WellFed(this));
        getCommand("witheredarrows").setExecutor(new WitheredArrows(this));
        getCommand("withering").setExecutor(new Withering(this));
        this.getServer().getPluginManager().registerEvents(new Agility(this), this);
        this.getServer().getPluginManager().registerEvents(new AutoJump(this), this);
        this.getServer().getPluginManager().registerEvents(new AutoMelting(this), this);
        this.getServer().getPluginManager().registerEvents(new AutoSpeed(this), this);
        this.getServer().getPluginManager().registerEvents(new Blindness(this), this);
        this.getServer().getPluginManager().registerEvents(new Excavation(this), this);
        this.getServer().getPluginManager().registerEvents(new ExplosiveArrows(this), this);
        this.getServer().getPluginManager().registerEvents(new FlashingArrows(this), this);
        this.getServer().getPluginManager().registerEvents(new Guards(this), this);
        this.getServer().getPluginManager().registerEvents(new IceAspect(this), this);
        this.getServer().getPluginManager().registerEvents(new Life(this), this);
        this.getServer().getPluginManager().registerEvents(new LifeLeech(this), this);
        this.getServer().getPluginManager().registerEvents(new Luck(this), this);
        this.getServer().getPluginManager().registerEvents(new MultiShoot(this), this);
        this.getServer().getPluginManager().registerEvents(new NightVision(this), this);
        this.getServer().getPluginManager().registerEvents(new Regeneration(this), this);
        this.getServer().getPluginManager().registerEvents(new Rejuvination(this), this);
        this.getServer().getPluginManager().registerEvents(new Telepathy(this), this);
        this.getServer().getPluginManager().registerEvents(new Waterbreathing(this), this);
        this.getServer().getPluginManager().registerEvents(new WellFed(this), this);
        this.getServer().getPluginManager().registerEvents(new WitheredArrows(this), this);
        this.getServer().getPluginManager().registerEvents(new Withering(this), this);

        getServer().getPluginManager().registerEvents(new ArmorListener(getConfig().getStringList("blocked")), this);
        getServer().getPluginManager().registerEvents(new DispenserArmorListener(), this);
        this.saveDefaultConfig();
    }

    public void onDisable(){

    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

}
