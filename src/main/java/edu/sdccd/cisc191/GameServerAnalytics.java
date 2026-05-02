package edu.sdccd.cisc191;

import java.util.*;
import java.util.stream.Stream;

public class GameServerAnalytics {

    public static List<String> findTopNUsernamesByRating(Collection<PlayerAccount> players, int n) {
        // TODO: use a stream pipeline
        return players.stream()
                .sorted(Comparator.comparingInt(PlayerAccount::rating).reversed())
                .limit(n)
                .map(PlayerAccount::username)
                .toList();
    }

    public static Map<String, Double> averageRatingByRegion(Collection<PlayerAccount> players) {
        // TODO: use groupingBy + averagingInt
        return players.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        PlayerAccount::region,
                        java.util.stream.Collectors.averagingInt(PlayerAccount::rating)
                ));
    }

    public static Set<String> findDuplicateUsernames(Collection<PlayerAccount> players) {
        // TODO: use collections and/or streams
        Set<String> seen = new java.util.HashSet<>();
        Set <String> duplicates = new java.util.HashSet<>();

        for (PlayerAccount player : players) {
            if (!duplicates.contains(player.username())) {
                duplicates.add(player.username());
            }
        }
        return duplicates;
    }

    public static Map<String, List<String>> groupUsernamesByTier(Collection<PlayerAccount> players) {
        // TODO: use groupingBy and mapping
        return players.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        GameServerAnalytics::tierFor,
                        java.util.stream.Collectors.mapping(
                                PlayerAccount::username,
                                java.util.stream.Collectors.toList()
                        )
                ));
    }

    public static Map<String, List<String>> buildRecentMatchSummariesByPlayer(Collection<MatchRecord> matches) {
        // TODO: use a Map + collection logic or a stream-based approach
        Map<String, List<String>> recentMatches = new HashMap<>();

        for (MatchRecord match : matches) {
            String p1 = match.playerOne().username();
            String p2 = match.playerTwo().username();

            String summary = p1 + " vs. " + p2;

            recentMatches.computeIfAbsent(p1, k -> new java.util.ArrayList<>()).add(summary);
            recentMatches.computeIfAbsent(p2, k -> new java.util.ArrayList<>()).add(summary);
        }
        return recentMatches;
    }

    public static <T> T pickHigherRated(T first, T second, Comparator<T> comparator) {
        // TODO: implement using the comparator
        return comparator.compare(first, second) >= 0 ? first : second;
    }

    public static String tierFor(PlayerAccount player) {
        if (player.rating() < 1000) return "Bronze";
        if (player.rating() < 1400) return "Silver";
        return "Gold";
    }
}
