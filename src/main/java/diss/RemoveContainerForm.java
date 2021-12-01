package diss;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RemoveContainerForm {

    private final Frame frame;

    public RemoveContainerForm(Containers containers) {
        var containerNames = containers.get();
        frame = new Frame();

        final var removeButton = new Button("Remove");
        final var removeAllButton = new Button("Remove all");

        final var pane = new ScrollPane();
        final var panel = new Panel();
        panel.setLayout(new GridLayout(containerNames.size(), 1));
        pane.add(panel);

        removeButton.setBounds(20, 80, 80, 30);
        removeAllButton.setBounds(100, 80, 80, 30);
        pane.setBounds(20, 120, 280, 30);
        pane.setSize(260, 360);

        var checkboxMap = new HashMap<String, Checkbox>();
        containerNames.forEach(container -> {
            var checkbox = new Checkbox(container);
            checkbox.setSize(250, 30);
            checkboxMap.put(container, checkbox);
            panel.add(checkbox);
        });

        frame.add(removeButton);
        frame.add(removeAllButton);
        frame.add(pane);

        frame.setSize(300,500);
        frame.setTitle("Remove container");
        frame.setLayout(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });

        removeButton.addActionListener(e -> {
            containers.remove(findSelectedCheckboxes(checkboxMap));
            frame.dispose();
        });
        removeAllButton.addActionListener(e -> {
            containers.removeAll();
            frame.dispose();
        });
    }

    private List<String> findSelectedCheckboxes(Map<String, Checkbox> map) {
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().getState())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
