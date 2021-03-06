package info.cameronlund.autonplanner.actions;

import com.google.gson.JsonObject;
import info.cameronlund.autonplanner.listeners.SaveStateListener;
import info.cameronlund.autonplanner.robot.Robot;

import javax.swing.*;
import java.awt.*;

/**
 * Name: Cameron Lund
 * Date: 1/23/2017
 * JDK: 1.8.0_101
 * Project: x
 */
public abstract class AutonAction {
    private AutonActionWrapper wrapper;
    private JPanel content;
    private Color color = Color.RED;
    private SaveStateListener saveStateListener = new SaveStateListener();

    public AutonAction(AutonActionWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public AutonAction(AutonActionWrapper wrapper, JsonObject data) {
        this(wrapper);
        loadJson(data);
    }

    public AutonActionWrapper getWrapper() {
        return wrapper;
    }

    public JPanel getContent() {
        return content;
    }

    protected void setContent(JPanel content) {
        this.content = content;
    }

    public abstract Robot renderWithGraphics(Robot robot, Graphics g);

    public abstract Robot renderWithoutGraphics(Robot robot);

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public abstract String renderCode(Robot robot);

    public abstract void loadJson(JsonObject object);

    public abstract JsonObject toJson();

    public void saveState() {
        saveStateListener.callActions();
    }

    protected SaveStateListener getSaveStateListener() {
        return saveStateListener;
    }
}
