package cc.co.evenprime.bukkit.nocheat.player;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MobEffectList;

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import cc.co.evenprime.bukkit.nocheat.data.DataStore;

public class NoCheatPlayerImpl implements NoCheatPlayer {

    protected Player                  player;
    protected final NoCheat           plugin;
    protected final DataStore         data;
    protected ConfigurationCacheStore config;
    protected long                    lastUsedTime;

    public NoCheatPlayerImpl(Player player, NoCheat plugin) {

        this.player = player;
        this.plugin = plugin;
        this.data = new DataStore();

        this.lastUsedTime = System.currentTimeMillis();
    }

    public void refresh(Player player) {
        this.player = player;
        this.config = plugin.getConfig(player);
    }

    public boolean isDead() {
        return this.player.getHealth() <= 0 || this.player.isDead();
    }

    public boolean hasPermission(String permission) {
        if(permission == null) {
            System.out.println("NoCheat: Warning, asked for null permission");
            return false;
        }
        return player.hasPermission(permission);
    }

    public DataStore getDataStore() {
        return data;
    }

    public ConfigurationCacheStore getConfigurationStore() {
        return config;
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return player.getName();
    }

    public int getTicksLived() {
        return player.getTicksLived();
    }

    public float getSpeedAmplifier() {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        if(ep.hasEffect(MobEffectList.FASTER_MOVEMENT)) {
            // Taken directly from Minecraft code, should work
            return 1.0F + 0.2F * (float) (ep.getEffect(MobEffectList.FASTER_MOVEMENT).getAmplifier() + 1);
        } else {
            return 1.0F;
        }
    }

    public boolean isSprinting() {
        return player.isSprinting();
    }

    public void setLastUsedTime(long currentTimeInMilliseconds) {
        this.lastUsedTime = currentTimeInMilliseconds;
    }

    public boolean shouldBeRemoved(long currentTimeInMilliseconds) {
        if(lastUsedTime > currentTimeInMilliseconds) {
            // Should never happen, but if it does, fix it somewhat
            lastUsedTime = currentTimeInMilliseconds;
        }
        return lastUsedTime + 60000L < currentTimeInMilliseconds;
    }

    public boolean isCreative() {
        return player.getGameMode() == GameMode.CREATIVE;
    }

    public void closeInventory() {
        ((CraftPlayer) this.player).getHandle().closeInventory();
    }
}