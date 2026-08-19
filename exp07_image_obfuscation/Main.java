package exp07_image_obfuscation;

public class Main {
    public static void main(String[] args) {
        String text = "sensitive-image";
        System.out.println("Image Obfuscation Simulation");
        System.out.println("=============================" );
        System.out.println("Original: " + text);
        String obfuscated = text.replace('e', '*').replace('i', '!');
        System.out.println("Obfuscated: " + obfuscated);
    }
}
