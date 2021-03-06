package com.github.offjaao.warps.utils;

import com.github.offjaao.warps.WarpsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.WeakHashMap;
import java.util.function.BiConsumer;

public class ChatAsker {
    private final static WeakHashMap<Player, ChatAsker> askers = new WeakHashMap<>();

    static {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onChat(AsyncPlayerChatEvent e) {
                ChatAsker asker = askers.get(e.getPlayer());
                if (asker != null) {
                    e.setCancelled(true);
                    BiConsumer<Player, String> consumer = asker.getOnComplete();
                    String message = e.getMessage();
                    Player player = e.getPlayer();
                    askers.remove(player);
                    consumer.accept(player, message);
                }
            }

            @EventHandler
            public void onQuit(PlayerQuitEvent e) {
                askers.remove(e.getPlayer());
            }
        }, WarpsPlugin.getInstance());
    }

    private String[] messages;
    private BiConsumer<Player, String> onComplete;

    private ChatAsker(Builder builder) {
        messages = builder.messages;
        onComplete = builder.onComplete;
    }

    public void addPlayer(Player player) {
        askers.put(player, this);
        player.sendMessage(messages);
    }

    public BiConsumer<Player, String> getOnComplete() {
        return onComplete;
    }

    public String[] getMessages() {
        return messages;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String[] messages;
        private BiConsumer<Player, String> onComplete;

        private Builder() {
        }

        public Builder messages(String... messages) {
            this.messages = messages;
            return this;
        }

        public Builder onComplete(BiConsumer<Player, String> onComplete) {
            this.onComplete = onComplete;
            return this;
        }

        public ChatAsker build() {
            return new ChatAsker(this);
        }
    }
}