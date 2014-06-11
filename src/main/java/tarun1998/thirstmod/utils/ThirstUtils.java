package tarun1998.thirstmod.utils;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import tarun1998.thirstmod.*;
import tarun1998.thirstmod.blocks.JMRecipes;
import tarun1998.thirstmod.blocks.RCRecipes;
import tarun1998.thirstmod.packets.PacketPlayerPos;

import java.io.File;
import java.io.FilenameFilter;

public class ThirstUtils {
    public static final String NAME = "Thirst mod";
    public static final String ID = "ThirstMod";
    public static final String VERSION = "1.2.0";

    private PlayerStatistics stats = new PlayerStatistics();
    public PlayerStatisticsMP statsMp = new PlayerStatisticsMP();

    /**
     * Gets the mod version.
     *
     * @return the mod version.
     */
    public static String getModVersion() {
        return VERSION;
    }

    /**
     * Gets the PlayerStatistics.class instance.
     *
     * @return the PlayerStatistics.class instance.
     */
    public PlayerStatistics getStats() {
        return stats;
    }


    public static ThirstUtils getUtilsFor(String playerUser) {
        return (ThirstUtils) PacketHandler.playerInstance.get(playerUser);
    }

    public static String getPlayerName() {
        return ClientProxy.getPlayer().username;
    }

    public static void addNewPlayer(String username, ThirstUtils utils) {
        if (!PacketHandler.playerInstance.containsKey(username)) {
            PacketHandler.playerInstance.put(username, utils);
        }
    }

    /**
     * Gets the players current speed in blocks per tick.
     *
     * @param entityplayer instance.
     * @return players current speed.
     */
    public static int getMovementStat(EntityPlayer entityplayer) {
        double s = entityplayer.posX;
        double s1 = entityplayer.posY;
        double s2 = entityplayer.posZ;
        double d = s - entityplayer.prevPosX;
        double d1 = s1 - entityplayer.prevPosY;
        double d2 = s2 - entityplayer.prevPosZ;
        return Math.round(MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2) * 100F);
    }

    /**
     * Gets the current biome the player is in.
     *
     * @param entityplayer
     * @return the current biome of the player.
     */
    public static String getCurrentBiome(EntityPlayer entityplayer) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            if (PacketHandler.isRemote == true) {
                return PacketPlayerPos.currentBiome;
            } else {
                return entityplayer.worldObj.getWorldChunkManager().getBiomeGenAt((int) entityplayer.posX, (int) entityplayer.posZ).biomeName;
            }
        } else {
            return entityplayer.worldObj.getWorldChunkManager().getBiomeGenAt((int) entityplayer.posX, (int) entityplayer.posZ).biomeName;
        }
    }

    /**
     * Sets all the data in PlayerStatistics to their original values.
     */
    public void setDefaults() {
        getStats().level = 20;
        getStats().exhaustion = 0f;
        getStats().saturation = 5f;
        getStats().healhurtTimer = 0;
        getStats().drinkTimer = 0;
        getStats().getPoison().setPoisonedTo(false);
        getStats().getPoison().setPoisonTime(0);
    }

    public static void setModUnloaded() {
        ThirstMod.proxy.loadedMod = false;
        PacketHandler.isRemote = false;
    }

    /**
     * Gets the Thirst Level from PlayerStatistics.class
     *
     * @return the level.
     */
    public int getLevel() {
        return getStats().level;
    }

    /**
     * Gets the Saturation Level from PlayerStatistics.class
     *
     * @return the saturation value.
     */
    public float getSaturation() {
        return getStats().saturation;
    }

    /**
     * Gets the Exhaustion Level from PlayerStatistics.class
     *
     * @return the exhaustion level.
     */
    public float getExhaustion() {
        return getStats().exhaustion;
    }

    /**
     * Adds exhaustion to the player.
     *
     * @param f amount of exhaustion to add.
     */
    public void addExhaustion(float f) {
        getStats().addExhaustion(f);
    }

    public static boolean isClientHost() {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            String name = ClientProxy.getPlayer().username;
            if (PacketHandler.isRemote && PacketHandler.typeOfServer == 1) {
                MinecraftServer server = FMLClientHandler.instance().getServer();
                if (server.getServerOwner().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Deletes files beginning with a specific extension. Used to avoid
     * conflicts in the ContentLoader.class
     *
     * @param directory Directory of the file.
     * @param extension Files beginning with this extension to delete.
     */
    public static void deleteFiles(String directory, String extension) {
        ExtensionFilter filter = new ExtensionFilter(extension);
        File dir = new File(directory);

        String[] list = dir.list(filter);
        File file;
        if (list.length == 0) {
            return;
        }

        for (int i = 0; i < list.length; i++) {
            file = new File(directory + list[i]);
            boolean isdeleted = file.delete();
        }
    }

    /**
     * Adds a recipe to the Drinks Brewer.
     *
     * @param id   Item that is placed in the top Drinks Brewer Slot.
     * @param item The Item that is returned after the item (int id) is brewed.
     */
    public static void addJMRecipe(int id, ItemStack item) {
        JMRecipes.solidifying().addSolidifying(id, item);
    }

    /**
     * Adds a recipe to the Drinks Brewer.
     *
     * @param id   Item that is placed in the top Drinks Brewer Slot.
     * @param j    Metadata for (int i) if needed.
     * @param item The Item that is returned after the item (int id, int
     *             metadata) is brewed.
     */
    public static void addJMRecipe(int id, int metadata, ItemStack item) {
        JMRecipes.solidifying().addSolidifying(id, metadata, item);
    }

    /**
     * Adds a recipe to the Rain Collector
     *
     * @param id         The id of the item to fill.
     * @param timeToFill Amount of time taken to fill the item. For reference Glass Bottle = 200, Bucket = 600;
     * @param return1    The filled item.
     */
    public static void addRCRecipe(int id, int timeToFill, ItemStack return1) {
        RCRecipes.fill().addRecipe(id, timeToFill, return1);
    }

    public static String getDir() {
        File s = ObfuscationReflectionHelper.getPrivateValue(Loader.class, Loader.instance(), "minecraftDir");
        return s.getAbsolutePath();
    }

    /**
     * Prints to either the console or the minecraft server window depending on
     * which side we are current at.
     *
     * @param obj something to print.
     */
    public static void print(Object obj) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            System.out.println("[ThirstMod] " + obj);
        } else {
            FMLCommonHandler.instance().getMinecraftServerInstance().logInfoMessage("[ThirstMod] " + obj.toString());
        }
    }
}

/**
 * Class that deletes files beginning with a specific extension.
 */
class ExtensionFilter implements FilenameFilter {
    private String extension;

    public ExtensionFilter(String extension) {
        this.extension = extension;
    }

    public boolean accept(File dir, String name) {
        return (name.endsWith(extension));
    }
}
