package diss;

import org.freedesktop.dbus.interfaces.DBusSigHandler;
import org.freedesktop.dbus.interfaces.Properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SignalHandler implements DBusSigHandler<Properties.PropertiesChanged> {

    private static final String LOGIN1_USER = "org.freedesktop.login1.Session";
    private static final String IDLE_HINT = "IdleHint";

    private final ProcessBuilder processBuilder = new ProcessBuilder();
    private final Containers containers;

    public SignalHandler(Containers containers) {
        this.containers = containers;
    }

    @Override
    public void handle(Properties.PropertiesChanged signal) {
        if (LOGIN1_USER.equals(signal.getInterfaceName())) {
            var changedProperty = signal.getPropertiesChanged().get(IDLE_HINT);

            if ((Boolean) changedProperty.getValue()) {
                onLock();
            } else {
                onUnlock();
            }
        }
    }

    private void onLock() {
        System.out.println("Starting containers");

        var containerNames = containers.get();
        var cmd = "docker start " + String.join(" ", containerNames);
        processBuilder.command("bash", "-c", cmd);
        startProcess();
    }

    private void onUnlock() {
        System.out.println("Stopping containers");

        var containerNames = containers.get();
        var cmd = "docker stop " + String.join(" ", containerNames);
        processBuilder.command("bash", "-c", cmd);
        startProcess();
    }

    private void startProcess() {
        try {
            var process = processBuilder.start();
            var output = new StringBuilder();

            var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println(output);
                System.out.println("Success!");
            } else {
                System.out.println("Process returned non-zero status code");
                System.exit(1);
            }

        } catch (IOException | InterruptedException ex) {
            System.out.println("Error while executing command");
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
}
