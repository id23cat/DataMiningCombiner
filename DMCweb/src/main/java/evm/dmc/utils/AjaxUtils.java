package evm.dmc.utils;

public class AjaxUtils {

    private AjaxUtils() {
    }

    public static boolean isAjaxRequest(String requestedWith) {
        return "XMLHttpRequest".equals(requestedWith);
    }
}
