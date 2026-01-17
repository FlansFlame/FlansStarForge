package net.flansflame.flans_star_forge.screens.helper;

public class MouseUtil {
    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y) {
        return isMouseOver(mouseX, mouseY, x, y, 16);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int size) {
        return isMouseOver(mouseX, mouseY, x, y, size, size);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
        return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int offsetX, int offsetY, int sizeX, int sizeY) {
        return isMouseOver(mouseX, mouseY, x + offsetX, y + offsetY, sizeX, sizeY);
    }
}