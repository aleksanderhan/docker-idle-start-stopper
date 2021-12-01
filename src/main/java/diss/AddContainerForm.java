package diss;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddContainerForm {

    private final Frame frame;
    private final TextField textField;

    public AddContainerForm(Containers containers) {
        frame = new Frame();
        textField = new TextField();
        final var label = new Label("Container name:");
        final var addButton = new Button("Add");

        label.setBounds(20, 70, 160, 30);
        textField.setBounds(20, 100, 240, 25);
        addButton.setBounds(20, 130, 80, 30);

        frame.add(textField);
        frame.add(label);
        frame.add(addButton);

        frame.setSize(300,200);
        frame.setTitle("Add container");
        frame.setLayout(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });

        addButton.addActionListener(e -> {
            if (textField.getText() != null || textField.getText().length() > 0) {
                containers.add(textField.getText());
                frame.dispose();
            }
        });

    }
}
