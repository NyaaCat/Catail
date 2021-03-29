package cat.nyaa.catail;

import java.util.Objects;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class MainSpigot extends JavaPlugin implements Listener {

    private static BlockData blockData;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        blockData = Bukkit.getServer().createBlockData("minecraft:lever[powered=true]");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        getLogger().log(Level.INFO, event.toString());
        Block clickedBlock = event.getClickedBlock();
        if (Objects.isNull(clickedBlock)) return;
        getLogger().log(Level.INFO, clickedBlock.getBlockData().getAsString(true));
        getLogger().log(Level.INFO, clickedBlock.getBlockData().getAsString(false));
        getLogger().log(Level.INFO, clickedBlock.getBlockData().getAsString());
        if (clickedBlock.getBlockData().getMaterial().equals(Material.LEVER)) {
            clickedBlock.setBlockData(clickedBlock.getBlockData().merge(blockData));
        }
        event.setCancelled(true);
    }
}
