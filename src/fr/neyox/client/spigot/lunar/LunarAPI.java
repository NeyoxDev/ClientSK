package fr.neyox.client.spigot.lunar;

import java.io.IOException;

import javax.management.Notification;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class LunarAPI {

    private static LunarAPI apiInstance;

    public LunarAPI() {
        apiInstance = this;
    }

    public static LunarAPI INSTANCE() {
        return apiInstance;
    }

    /**
     * Send a cooldown packet to the player.
     *
     * @param player the player instance.
     * @param name the name of the instance.
     * @param material the material instance.
     * @param seconds the seconds the cooldown goes up for.
     * @throws IOException the exception called when something fucks up.
     */
    public abstract void sendCooldown(Player player, String name, Material material, int seconds) throws IOException;

    /**
     * Send a cooldown packet to the player.
     *
     * @param player the player instance.
     * @param name the name of the instance.
     * @param material the material instance.
     * @param mill the seconds the cooldown goes up for.
     * @throws IOException the exception called when something fucks up.
     */
    public abstract void sendCooldown(Player player, String name, Material material, long mill) throws IOException;

    /**
     * send a title packet to the player.
     *
     * @param player the player to send packet to.
     * @param message the message on the title.
     * @param size the size of the title.
     * @param duration the duration of the title.
     * @param fadeIn the fadeIn duration of the title.
     * @param fadeOut the fadeOut duration of the title.
     * @throws IOException the exception called if something goes wrong.
     */
    public abstract void sendTitle(Player player, boolean subTitle, String message, float size, int duration, int fadeIn, int fadeOut) throws IOException;

    /**
     * Make the player perform an emote.
     *
     * @param player the player which will be performing the emote.
     * @param type the emote
     * @param everyone if the packet should be sent to everyone.
     * @throws IOException exception.
     */
    public abstract void performEmote(Player player, int type, boolean everyone) throws IOException;


    /**
     * updates the players discord rich presence server name
     *
     * @param player the player to affect.
     * @param name the server name.
     * @throws IOException the exception called if something goes wrong.
     */
    public abstract void updateServerName(Player player, String name) throws IOException;

    /**
     * update the name tag of a player.
     *
     * @param player the player to send the update to.
     * @param target the player to change name tag.
     * @param tags the tags to add for the player.
     * @throws IOException the exception called if something goes wrong.
     */
    public abstract void updateNameTag(Player player, Player target, String... tags) throws IOException;

    /**
     * resets the name tag of a player to be handled by the native scoreboard system.
     *
     * @param player the player to send the update to.
     * @param target the player to change name tag.
     * @throws IOException the exception called if something goes wrong.
     */
    public abstract void resetNameTag(Player player, Player target) throws IOException;

    /**
     * send the teammate packet to the two players.
     *
     * @param player the player that is gonna see his team mates.
     * @param targets the targets the player should be teamed with.
     * @throws IOException the exception called if something goes wrong.
     */
    public abstract void sendTeamMate(Player player, Player... targets) throws IOException;

    /**
     * resets the teammates that a player can see.
     *
     * @param player the player that is gonna see his team mates.
     * @throws IOException the exception called if something goes wrong.
     */
    public abstract void resetTeamMates(Player player) throws IOException;

    /**
     * Check if a player is playing on Lunar Client.
     *
     * @param player the player.
     * @return if they are on Lunar Client.
     */
    public abstract boolean isAuthenticated(Player player);

    public abstract UserManager getUserManager();

}
