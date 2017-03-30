package info.cameronlund.autonplanner.robotest

import info.cameronlund.autonplanner.robot.Robot

import java.awt.*
import java.util.List

class TestRobot extends Robot {
    double ghostX
    double ghostY
    double ghostRot
    List lines
    List lineSensors = [new DPoint(-52, -52), new DPoint(-52, 52),
                        new DPoint(52, -52), new DPoint(52, 52)]

    TestRobot(List lines) {
        this.lines = lines
        //setResting(300, 515)
        setResting(300, 400)
        returnToResting()
    }

    void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g

        // Draw actual position
        g2.setColor(Color.BLACK)
        Rectangle bot = new Rectangle((posX / 4d - 55) as int, (posY / 4d - 55) as int, 110, 110)
        Rectangle posMarkerL = new Rectangle((posX / 4d - 55) as int, (posY / 4d - 55) as int, 10, 10)
        Rectangle posMarkerR = new Rectangle((posX / 4d + 45) as int, (posY / 4d - 55) as int, 10, 10)

        if (rotation != 0.0)
            g2.rotate(Math.toRadians(getRotation()), posX / 4d, posY / 4d) // Rotate on the robots center
        g2.draw(bot)
        g2.fill(posMarkerL)
        g2.fill(posMarkerR)
        // Draw the line sensors
        g2.setColor(isOnLine(0) ? Color.GREEN : Color.RED)
        g2.drawOval(posX / 4d - 54 as int, posY / 4d - 54 as int, 5, 5)
        g2.setColor(isOnLine(1) ? Color.GREEN : Color.RED)
        g2.drawOval(posX / 4d - 54 as int, posY / 4d + 49 as int, 5, 5)
        g2.setColor(isOnLine(2) ? Color.GREEN : Color.RED)
        g2.drawOval(posX / 4d + 49 as int, posY / 4d - 54 as int, 5, 5)
        g2.setColor(isOnLine(3) ? Color.GREEN : Color.RED)
        g2.drawOval(posX / 4d + 49 as int, posY / 4d + 49 as int, 5, 5)
        g2.rotate(Math.toRadians(-1 * getRotation()), posX / 4d, posY / 4d) // Rotate on the robots center

        // Draw ghost position
        g2.setColor(Color.GRAY)
        bot = new Rectangle((ghostX / 4d - 55) as int, (ghostY / 4d - 55) as int, 110, 110)
        posMarkerL = new Rectangle((ghostX / 4d - 55) as int, (ghostY / 4d - 55) as int, 10, 10)
        posMarkerR = new Rectangle((ghostX / 4d + 45) as int, (ghostY / 4d - 55) as int, 10, 10)

        if (rotation != 0.0)
            g2.rotate(Math.toRadians(getRotation()), ghostX / 4d, ghostY / 4d)
        // Rotate on the robots center
        g2.draw(bot)
        g2.fill(posMarkerL)
        g2.fill(posMarkerR)
        g2.rotate(Math.toRadians(-1 * getRotation()), ghostX / 4d, ghostY / 4d)
        // Rotate on the robots center
    }

    boolean isOnLine(int i) {
        def sensor = lineSensors.get(i) as DPoint
        boolean isOnLine = false
        for (def l : getLines()) {
            def li = l as Line
            DPoint pos = getPix();
            DPoint sPos = new DPoint(pos.x + sensor.x, pos.y + sensor.y);
            DPoint posRot = rotate(sPos, pos, rotation)
            double y = li.getPoint(posRot.x).y
            if (Math.abs(y - posRot.y) <= li.getDetectionRadius()) isOnLine = true
        }
        return isOnLine
    }

    DPoint getPos() {
        return new DPoint(posX, posY);
    }

    DPoint getPix() {
        return new DPoint(posX/4, posY/4);
    }

    public void setRotation(double rotation) {
        super.setRotation(rotation);
        ghostRot = rotation + new Random().nextInt(7) - 3;
    }

    Line getOnLine(int i) {
        def sensor = lineSensors.get(i) as DPoint
        for (def l : getLines()) {
            def li = l as Line
            DPoint pos = getPix();
            DPoint sPos = new DPoint(pos.x + sensor.x, pos.y + sensor.y);
            DPoint posRot = rotate(sPos, pos, rotation)
            double y = li.getPoint(posRot.x).y
            if (Math.abs(y - posRot.y) <= li.getDetectionRadius()) return li
        }
        return null
    }

    DPoint rotate(DPoint point, DPoint center, double degrees) {
        double radians = Math.toRadians(degrees);
        double x1 = point.x - center.x;
        double y1 = point.y - center.y;

        double x2 = x1 * Math.cos(radians) - y1 * Math.sin(radians);
        double y2 = x1 * Math.sin(radians) + y1 * Math.cos(radians);

        new DPoint(x2 + center.x, y2 + center.y)
    }

    void moveGhost(angle, distance) {
        ghostX += Math.cos(Math.toRadians(angle) - (Math.PI / 2)) * distance;
        ghostY += Math.sin(Math.toRadians(angle) - (Math.PI / 2)) * distance;
    }

    public void returnToResting() {
        super.returnToResting();
        ghostX = posX;
        ghostY = posY;
        ghostRot = rotation;
        //setRotation(rand.nextInt(44))
    }
}