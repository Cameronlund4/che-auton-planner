package info.cameronlund.autonplanner.actions;

import com.google.gson.JsonObject;
import info.cameronlund.autonplanner.listeners.ActionFocusListener;
import info.cameronlund.autonplanner.robot.Robot;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Name: Cameron Lund
 * Date: 1/23/2017
 * JDK: 1.8.0_101
 * Project: x
 */
public class DriveAutonAction extends AutonAction {
    private int distance = 0;
    private JTextField distField;

    public DriveAutonAction(AutonActionWrapper wrapper) {
        super(wrapper);
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        //gbc.weightx = 1;
        //gbc.fill = GridBagConstraints.NONE;

        JLabel label = new JLabel("\u2022 Distance: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(label, gbc);

        distField = new JTextField();
        distField.setText(distance + "");
        distField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        distField.setPreferredSize(new Dimension(75, 25));
        distField.setMaximumSize(new Dimension(75, 25));
        distField.addActionListener(e -> {
            try {
                distance = Integer.parseInt(distField.getText());
                wrapper.getManager().repaint();
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
                distField.setText(distance + "");
            }
        });
        distField.addFocusListener(new ActionFocusListener(distField));
        getSaveStateListener().addComponent(distField);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(distField, gbc);
        setContent(content);
    }

    @Override
    public Robot renderWithGraphics(Robot robot, Graphics g) {
        int x = (int) robot.getPosX();
        int y = (int) robot.getPosY();
        robot = renderWithoutGraphics(robot);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getColor());
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x, y, (int) robot.getPosX(), (int) robot.getPosY());
        g2.setColor(Color.BLACK);
        g2.fillOval((int) robot.getPosX() - 4, (int) robot.getPosY() - 4, 8, 8);
        return robot;
    }

    @Override
    public Robot renderWithoutGraphics(Robot robot) {
        robot.moveTicks(distance);
        return robot;
    }

    @Override
    public String renderCode(info.cameronlund.autonplanner.robot.Robot robot) {
        return String.format("odomDriveForward(%d, false, true); // " + getWrapper().getActionName(), distance);
    }

    @Override
    public void loadJson(JsonObject object) {
        if (!object.get("type").getAsString().equalsIgnoreCase("DRIVE")) {
            System.out.println("Got bad type for " + "DRIVE" + ", received " +
                    object.get("type").getAsString());
            return;
        }
        distance = object.get("distance").getAsInt();
        distField.setText(distance + "");
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "DRIVE");
        object.addProperty("name", getWrapper().getActionName());
        object.addProperty("distance", distance);
        // TODO Implement
        return object;
    }

    public void setDistance(int distance) {
        this.distance = distance;
        distField.setText(distance+"");
    }
}
