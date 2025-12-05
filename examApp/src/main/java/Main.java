import ui.SplashScreen;

/**
 * Главный класс приложения.
 * Содержит точку входа в программу.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class Main {

    /**
     * Точка входа в приложение.
     * Запускает заставку при старте.
     *
     * @param args - аргументы командной строки
     */
    public static void main(String[] args) {
        // Запускаем splash screen
        SplashScreen.showSplash();
    }
}