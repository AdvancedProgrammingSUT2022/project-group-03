package com.example.demo.view;

import java.util.List;
import java.util.Random;

public enum UserIcon {
    ALEXANDER("assets/icons/1.png"),
    AMANITORE("assets/icons/2.png"),
    AMBIORIX("assets/icons/3.png"),
    BASILL_II("assets/icons/4.png"),
    BATRIOWEBP("assets/icons/5.png"),
    CATHERINE_DE_MEDICI("assets/icons/6.png"),
    CHANDERGUPTA("assets/icons/7.png"),
    CLEOPATRA("assets/icons/8.png"),
    CYRUS("assets/icons/9.png"),
    DIDO("assets/icons/10.png"),
    ELEANOR_OF_AQUITAINE("assets/icons/11.png"),
    FREDERICK_BARBAROSSA("assets/icons/12.png"),
    GANDHI("assets/icons/13.png"),
    GENGHIS_KHAN("assets/icons/14.png"),
    GILGAMESH("assets/icons/15.png"),
    GITARJA("assets/icons/16.png"),
    GORGO("assets/icons/17.png"),
    HAMMURABI("assets/icons/18.png"),
    HARALD_HARDRADA("assets/icons/19.png"),
    HOJO_TOKIMUNE("assets/icons/20.png"),
    JADWIGA("assets/icons/21.png"),
    JAYAVARMAN_VII("assets/icons/22.png"),
    JOAO_III("assets/icons/23.png"),
    JOHN_CURTIN("assets/icons/24.png"),
    KRISTINA("assets/icons/25.png"),
    KUBLAI_KHAN("assets/icons/26.png"),
    KUPE("assets/icons/27.png"),
    LADY_SIX_SKY("assets/icons/28.png"),
    LAUTARO("assets/icons/29.png"),
    MANSA_MUSA("assets/icons/30.png"),
    MATTHIAS_CORVINUS("assets/icons/31.png"),
    MENELIK_II("assets/icons/32.png"),
    MONTEZUMA("assets/icons/33.png"),
    MVEMBA_A_NZINGA("assets/icons/34.png"),
    PACHACUTI("assets/icons/35.png"),
    PEDRO_II("assets/icons/36.png"),
    PERICLES("assets/icons/37.png"),
    PETER("assets/icons/38.png"),
    PHILIP_II("assets/icons/39.png"),
    POUNDMAKER("assets/icons/40.png"),
    QUN_SHI_HUANG("assets/icons/41.png"),
    ROBERT_DE_BRUCE("assets/icons/42.png"),
    SALADIN("assets/icons/43.png"),
    SEONDOEK("assets/icons/44.png"),
    SHAKA("assets/icons/45.png"),
    SIMON_BOLIVAR("assets/icons/46.png"),
    SULEIMAN("assets/icons/47.png"),
    TAMAR("assets/icons/48.png"),
    TEDDY_ROOSEVELT("assets/icons/49.png"),
    TOMYRIS("assets/icons/50.png"),
    TRAJAN("assets/icons/51.png"),
    VICTORIA("assets/icons/52.png"),
    WILFRID_LAURIER("assets/icons/53.png"),
    WILHELMINA("assets/icons/54.png"),
    CUSTOM("");
    private final String image;
    final static int ICON_NUMBER = 54;
    private static final List<UserIcon> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public static UserIcon randomIcon() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
    UserIcon(String image){
        this.image = image;
    }
    public static List<UserIcon> getVALUES() {
        return VALUES;
    }

    public String getImage() {
        return image;
    }
}
