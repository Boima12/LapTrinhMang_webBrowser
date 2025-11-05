package shared;

public class HtmlAnalyzer {
    public static void analyze(String html) {
        int pCount = html.split("<p").length - 1;
        int divCount = html.split("<div").length - 1;
        int spanCount = html.split("<span").length - 1;
        int imgCount = html.split("<img").length - 1;

        System.out.println("=== HTML Analysis ===");
        System.out.println("<p>: " + pCount);
        System.out.println("<div>: " + divCount);
        System.out.println("<span>: " + spanCount);
        System.out.println("<img>: " + imgCount);
        System.out.println("Length: " + html.length() + " characters");
    }
}
