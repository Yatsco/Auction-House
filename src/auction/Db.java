/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/
package auction;

/**
 * Db handles debug messages. Can be turned on and off programmatically.
 *
 * @author Jack Trainor
 * @version CS_351_002 : 2021 - 03 - 29
 */
public class Db {
    private static boolean on = true;

    protected static void enable() {
        on = true;
    }

    protected static void disable() {
        on = false;
    }

    /**
     * Sends string to System.out
     *
     * @param s String sent to System.out
     * @return Nothing
     */
    public static void out(String s) {
        if (on) {
            System.out.println(s);
        }
    }
}
