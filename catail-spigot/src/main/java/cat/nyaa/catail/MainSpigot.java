package cat.nyaa.catail;

import cat.nyaa.catail.common.ElementData;
import cat.nyaa.catail.common.bukkit.BukkitBlock;
import cat.nyaa.catail.common.bukkit.BukkitElementDataRegistry;
import cat.nyaa.catail.common.bukkit.BukkitIdentifier;
import java.util.Objects;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class MainSpigot extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        getLogger().log(Level.INFO, event::toString);
        Block clickedBlock = event.getClickedBlock();
        BukkitElementDataRegistry instance = BukkitElementDataRegistry.getInstance();
        if (Objects.isNull(clickedBlock)) return;
        BukkitBlock bukkitBlock = BukkitBlock.get(clickedBlock);
        ElementData elementData = instance.match(bukkitBlock);
        getLogger().log(Level.INFO, Objects.toString(elementData));
        if (Objects.nonNull(elementData)) {
            getLogger().log(Level.INFO, elementData.getStateName());
            getLogger().log(Level.INFO, elementData.getAsString());
        }
        if (clickedBlock.getBlockData().getMaterial().equals(Material.LEVER)) {
            ElementData data = instance.get(
                BukkitIdentifier.get(Material.LEVER.getKey()),
                "minecraft:lever[powered=false]"
            );
            bukkitBlock.setState(data);
        }
    }
}
