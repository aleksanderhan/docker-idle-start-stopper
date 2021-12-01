package diss;

import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.interfaces.Properties;

import java.awt.*;

public class Main {

    private Containers containers;

    public static void main(String[] args) {
        try {
            var app = new Main();
            app.init();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void init() throws Exception {
        containers = new Containers();
        addTrayIcon();

        try (DBusConnection conn = DBusConnection.getConnection(DBusConnection.DBusBusType.SYSTEM)) {
            conn.addSigHandler(Properties.PropertiesChanged.class, new SignalHandler(containers));
        }
    }

    private void addTrayIcon() throws Exception {
        if (!SystemTray.isSupported()) {
            throw new Exception("SystemTray is not supported");
        }
        var image = Toolkit.getDefaultToolkit()
                .createImage(Main.class.getClassLoader().getResource("tray_icon.png"));

        // TODO: image doesn't work

        final var popup = new PopupMenu();
        final var trayIcon = new TrayIcon(image, "idle-cmd-trigger", popup);
        final var tray = SystemTray.getSystemTray();

        // Exit button
        var exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(1));
        popup.add(exitItem);

        // Add container frame
        var addContainerItem = new MenuItem("Add container");
        addContainerItem.addActionListener(e -> new AddContainerForm(containers));
        popup.add(addContainerItem);

        // Remove container frame
        var removeContainerItem = new MenuItem("Remove container");
        removeContainerItem.addActionListener(e -> new RemoveContainerForm(containers));
        popup.add(removeContainerItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added");
            System.exit(1);
        }
    }

}
