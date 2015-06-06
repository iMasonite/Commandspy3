package net.korikisulda.commandspy3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import net.korikisulda.commandspy3.util.CommandHandler;
import net.korikisulda.commandspy3.util.CommandManager;

public class DatabaseManager extends CommandManager{
    private Main main;
    private Connection connection;
    
    public DatabaseManager(Main main){
        this.main = main;
    }
    
    
    @CommandHandler(maximumArgsLength=0)
    public void status (CommandSender sender, String args) throws SQLException{
        if(!main.getConfigManager().databaseEnabled){
            sender.sendMessage(ChatColor.RED + "Database connection is not enabled.");
        }else if(connection == null){
            sender.sendMessage(ChatColor.RED + "The connection is null.");
        }else if(connection.isClosed()){
            sender.sendMessage(ChatColor.RED + "The connection is closed.");
        }else{
            sender.sendMessage(ChatColor.GREEN + "There seems to be a connection.");
        }
    }
    
    public void init() {
        try {
            Class.forName(main.getConfigManager().databaseDriver);
            connection = DriverManager.getConnection(main.getConfigManager().databaseAddress, main.getConfigManager().databaseUsername, main.getConfigManager().databasePassword);
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() {
        try {
            if (connection == null) {
                init();
            } else if (connection.isClosed()) {
                init();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public void processPlayerCommand (PlayerCommandPreprocessEvent event) {
        if(!main.getConfigManager().databaseEnabled) return;
        if(!main.getConfigManager().databaseCommands) return;
        try{
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("INSERT INTO `player`(`command`,`user_uuid`,`user_username`,`user_displayname`,`user_address`) VALUES (?,?,?,?,?)");
            ps.setString(1, event.getMessage());
            ps.setString(2, event.getPlayer().getUniqueId().toString());
            ps.setString(3, event.getPlayer().getName());
            ps.setString(4, event.getPlayer().getDisplayName());
            ps.setString(5, event.getPlayer().getAddress().toString());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public void processServerCommand (ServerCommandEvent event) {
        if(!main.getConfigManager().databaseEnabled) return;
        if(!main.getConfigManager().databaseServerCommands) return;
        try{
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("INSERT INTO `server`(`command`) VALUES (?)");
            ps.setString(1, event.getCommand());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public void processSignChange (SignChangeEvent event) {
        if(!main.getConfigManager().databaseEnabled) return;
        if(!main.getConfigManager().databaseSigns) return;
        try{
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("INSERT INTO `signs`(`user_uuid`, `user_username`, `user_displayname`, `user_address`, `sign_line1`, `sign_line2`, `sign_line3`, `sign_line4`, `sign_x`, `siyn_y`, `sign z`, `sign_world`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, event.getPlayer().getUniqueId().toString());
            ps.setString(2, event.getPlayer().getName());
            ps.setString(3, event.getPlayer().getDisplayName());
            ps.setString(4, event.getPlayer().getAddress().toString());
            ps.setString(5, event.getLine(0));
            ps.setString(6, event.getLine(1));
            ps.setString(7, event.getLine(2));
            ps.setString(8, event.getLine(3));
            ps.setInt(9, event.getBlock().getX());
            ps.setInt(10, event.getBlock().getY());
            ps.setInt(11, event.getBlock().getZ());
            ps.setString(12, event.getBlock().getWorld().getName());
            
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public void createTables() throws SQLException{
        Connection c = getConnection();
        c.createStatement().execute(
            " CREATE TABLE IF NOT EXISTS `player` (" +
            " `id` int(11) NOT NULL AUTO_INCREMENT," +
            " `occurred` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            " `command` varchar(256) NOT NULL," +
            " `user_uuid` char(36) NOT NULL," +
            " `user_username` char(16) NOT NULL," +
            " `user_displayname` char(64) NOT NULL," +
            " `user_address` varchar(256) NOT NULL," +
            " PRIMARY KEY (`id`)" +
            ") DEFAULT CHARSET=utf8"
        );
        c.createStatement().execute(
            " CREATE TABLE IF NOT EXISTS `server` (" +
            " `id` int(11) NOT NULL AUTO_INCREMENT," +
            " `occurred` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            " `command` varchar(256) NOT NULL," +
            " PRIMARY KEY (`id`)" +
            ") DEFAULT CHARSET=utf8"
        );
        c.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS `signs` (" +
            " `id` int(11) NOT NULL AUTO_INCREMENT," +
            " `occurred` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            " `user_uuid` char(36) NOT NULL," +
            " `user_username` char(16) NOT NULL," +
            " `user_displayname` char(64) NOT NULL," +
            " `user_address` varchar(256) NOT NULL," +
            " `sign_line1` varchar(64) NOT NULL," +
            " `sign_line2` varchar(64) NOT NULL," +
            " `sign_line3` varchar(64) NOT NULL," +
            " `sign_line4` varchar(64) NOT NULL," +
            " `sign_x` int(11) NOT NULL," +
            " `siyn_y` int(11) NOT NULL," +
            " `sign z` int(11) NOT NULL," +
            " `sign_world` char(64) NOT NULL," +
            " PRIMARY KEY (`id`)" +
            ") DEFAULT CHARSET=utf8"
        );
    }
    
}
