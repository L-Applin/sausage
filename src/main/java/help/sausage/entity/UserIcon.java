package help.sausage.entity;


import java.security.SecureRandom;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public enum UserIcon {
    BUNNY,
    CAT,
    DEER,
    DOG_1,
    DOG_2,
    DOG_3,
    FOX,
    HIPPO,
    LION,
    OCTOPUS,
    OWL,
    PIG,
    PIG_FULL,
    RHINO,
    SHEEP,
    SNAKE,
    TEDDY_BEAR,
    TURTLE;

    public static final String location = "images/user-icons/";
    private static final String DEFAULT_FILE_EXTENSION = ".png";

    public static UserIcon from(String icon) {
        return Arrays.stream(values()).filter(str ->
            str.name().replace('_', '-').toUpperCase().equals(icon))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(icon));
    }

    public String asUrl() {
        return location + this.name().replace('_', '-').toLowerCase() + DEFAULT_FILE_EXTENSION;
    }

   public static UserIcon random() {
        UserIcon[] icons = UserIcon.values();
        int size = icons.length;
        int randIdx = new Random().nextInt(size);
        return icons[randIdx];
   }

}
