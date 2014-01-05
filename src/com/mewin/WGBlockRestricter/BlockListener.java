/*
 * Copyright (C) 2012 mewin <mewin001@hotmail.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mewin.WGBlockRestricter;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author mewin <mewin001@hotmail.de>
 */
public class BlockListener implements Listener {
    private WGBlockRestricterPlugin plugin;
    private WorldGuardPlugin wgPlugin;
    
    public BlockListener(WGBlockRestricterPlugin plugin, WorldGuardPlugin wgPlugin)
    {
        this.plugin = plugin;
        this.wgPlugin = wgPlugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK
                && e.hasItem()
                && Utils.isBlockMaterial(e.getMaterial())
                && !e.getPlayer().hasPermission("wgblockrestricter.ignore")
                && !Utils.blockAllowedAtLocation(wgPlugin, e.getMaterial(), e.getClickedBlock().getRelative(e.getBlockFace()).getLocation())) {
            String message = plugin.getConfig().getString("messages.deny-block-place", "&cYou are not allowed to place {block} here.");
            if (!"".equals(message))
            {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message
                        .replaceAll("\\{block\\}", e.getMaterial().name())));
            }
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        if (!e.getPlayer().hasPermission("wgblockrestricter.ignore")
                && !Utils.blockAllowedAtLocation(wgPlugin, e.getBlockPlaced().getType(), e.getBlockPlaced().getLocation())) {
            String message = plugin.getConfig().getString("messages.deny-block-place", "&cYou are not allowed to place {block} here.");
            if (!"".equals(message))
            {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message
                        .replaceAll("\\{block\\}", e.getBlock().getType().name())));
            }
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (!e.getPlayer().hasPermission("wgblockrestricter.ignore")
                && !Utils.blockAllowedAtLocation(wgPlugin, e.getBlock().getType(), e.getBlock().getLocation())) {
            String message = plugin.getConfig().getString("messages.deny-block-break", "&cYou are not allowed to break {block} here.");
            if (!"".equals(message))
            {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message
                        .replaceAll("\\{block\\}", e.getBlock().getType().name())));
            }
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onHangingPlace(HangingPlaceEvent e)
    {
        Material mat = Material.PAINTING;
        if (e.getEntity() instanceof ItemFrame)
        {
            mat = Material.ITEM_FRAME;
        }
        if (!e.getPlayer().hasPermission("wgblockrestricter.ignore")
                && !Utils.blockAllowedAtLocation(wgPlugin, mat, e.getBlock().getRelative(e.getBlockFace()).getLocation()))
        {
            String message = plugin.getConfig().getString("messages.deny-hanging-place", "&cYou are not allowed to place {block} here.");
            if (!"".equals(message))
            {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message
                        .replaceAll("\\{block\\}", mat.name())));
            }
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent e)
    {
        if (e.getRemover() instanceof Player)
        {
            Player player = (Player) e.getRemover();
            Material mat = Material.PAINTING;
            if (e.getEntity() instanceof ItemFrame)
            {
                mat = Material.ITEM_FRAME;
            }
            if (!player.hasPermission("wgblockrestricter.ignore")
                    && !Utils.blockAllowedAtLocation(wgPlugin, mat, e.getEntity().getLocation()))
            {
                String message = plugin.getConfig().getString("messages.deny-hanging-break", "&cYou are not allowed to break {block} here.");
                if (!"".equals(message))
                {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message
                            .replaceAll("\\{block\\}", mat.name())));
                }
                e.setCancelled(true);
            }
        }
    }
}
