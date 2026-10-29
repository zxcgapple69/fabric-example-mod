package com.soupvisual.client;

import java.util.HashSet;
import java.util.Set;

public class FriendManager {

    private static final Set<String> friends = new HashSet<>();

    static {
        // Shu yerga do'stlaringiz Minecraft ismini yozing
        friends.add("Pa_dej");
        friends.add("xX_soup_Xx");
    }

    public static boolean isFriend(String username) {
        return friends.contains(username);
    }

    public static void addFriend(String username) {
        friends.add(username);
    }

    public static void removeFriend(String username) {
        friends.remove(username);
    }

    public static Set<String> getFriends() {
        return new HashSet<>(friends);
    }
}
