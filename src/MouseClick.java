import java.awt.*;

import java.awt.event.InputEvent;


public class MouseClick {

    public static final int STEP_LEN_PIXEL = 25;
    public static final int STEP_INTERVAL_MS = 2;


    public static void main(String[] args) {
        String argCnt = args[0], argTimeInterval = args[1];
        int cnt = Integer.parseInt(argCnt);
        int timeInterval = Integer.parseInt(argTimeInterval);
        Point point = null;
        while (cnt > 0) {
            point = MouseClick.click(point);
            System.out.println(cnt + " " + argTimeInterval + " milli-second(s) left: " + point);
            --cnt;
            sleepMilliSecond(timeInterval);
        }
    }

    public static Point click(Point point) {
        try {
            Robot bot = new Robot();
            Point clickPoint = point;
            if (point == null) {
                bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                clickPoint = MouseInfo.getPointerInfo().getLocation();
            } else {
                Point currPoint = MouseInfo.getPointerInfo().getLocation();
                mouseMoveWithTrail(bot, currPoint, clickPoint);
                bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                mouseMoveWithTrail(bot, clickPoint, currPoint);
            }
            return clickPoint;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void mouseMoveWithTrail(Robot bot, Point fromPoint, Point toPoint) {
//                bot.mouseMove(point.x, point.y);
        double xDist = (double) Math.abs(toPoint.x - fromPoint.x);
        double yDist = (double) Math.abs(toPoint.y - fromPoint.y);
        double dist = Math.max(xDist, yDist);
        int numOfSteps = (int) dist / STEP_LEN_PIXEL;
        double xStepLen = xDist / numOfSteps;
        double yStepLen = yDist / numOfSteps;
        int xDirectionFactor = (toPoint.x >= fromPoint.x) ? 1 : -1;
        int yDirectionFactor = (toPoint.y >= fromPoint.y) ? 1 : -1;
        double xDistMoved = 0, yDistMoved = 0;
        while (xDistMoved + xStepLen <= xDist || yDistMoved + yStepLen <= yDist) {
            if (xDistMoved + xStepLen <= xDist) {
                xDistMoved += xStepLen;
            }
            if (yDistMoved + yStepLen <= yDist) {
                yDistMoved += yStepLen;
            }
//            System.out.println("(" + (fromPoint.x + (int)xDistMoved) + ", " +  (fromPoint.y + (int)yDistMoved));
            bot.mouseMove(fromPoint.x + (int) xDistMoved * xDirectionFactor, fromPoint.y + (int) yDistMoved * yDirectionFactor);
            sleepMilliSecond(STEP_INTERVAL_MS);
        }
        bot.mouseMove(toPoint.x, toPoint.y);
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public static void sleepMilliSecond(Integer milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
