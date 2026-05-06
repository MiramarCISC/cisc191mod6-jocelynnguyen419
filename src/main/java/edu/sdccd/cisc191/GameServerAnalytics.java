package edu.sdccd.cisc191;

import java.util.*;
import java.util.stream.Collectors;

public class GameServerAnalytics {

    // PR: Clean, readable implementation. Reads like a sentence!
    public static List<String> findTopNUsernamesByRating(Collection<PlayerAccount> players, int n) {
        // TODO: use a stream pipeline
        return players.stream()
                // PR: Sort alphabetically in the end in order to get consistent results
                .sorted(Comparator.comparingInt(PlayerAccount::rating).reversed().thenComparing(PlayerAccount::username))
                .limit(n)
                .map(PlayerAccount::username)
                .toList();
    }

    // PR: Simple and readable Stream pipeline.
    public static Map<String, Double> averageRatingByRegion(Collection<PlayerAccount> players) {
        // TODO: use groupingBy + averagingInt
        return players.stream()
                .collect(Collectors.groupingBy(
                        PlayerAccount::region,
                        Collectors.averagingInt(PlayerAccount::rating)
                ));
    }

    public static Set<String> findDuplicateUsernames(Collection<PlayerAccount> players) {
        // PR: This method is supposed to be implemented using collections and/or streams
        // (The following is an implementation that matches the assignment requirements)
        return players.stream() // Stream players
            .collect(Collectors.groupingBy(PlayerAccount::username, Collectors.counting())) // Create entries of form: (username, # of instances)
            .entrySet().stream() // Stream entries
            .filter(e -> e.getValue() > 1) // Get only usernames with 2+ mentions
            .map(Map.Entry::getKey) // Get usernames in question
            .collect(Collectors.toSet()); // Convert to set
    }

    // PR: Implementation is clean and logical.
    public static Map<String, List<String>> groupUsernamesByTier(Collection<PlayerAccount> players) {
        // TODO: use groupingBy and mapping
        return players.stream()
                .collect(Collectors.groupingBy(
                        GameServerAnalytics::tierFor,
                        Collectors.mapping(
                                PlayerAccount::username,
                                Collectors.toList()
                        )
                ));
    }

    public static Map<String, List<String>> buildRecentMatchSummariesByPlayer(Collection<MatchRecord> matches) {
        // TODO: use a Map + collection logic or a stream-based approach
        Map<String, List<String>> recentMatches = new HashMap<>();

        // PR: Implementation works OK, but assignment asks for collection logic or stream-based approach.
        for (MatchRecord match : matches) {
            String p1 = match.playerOne().username();
            String p2 = match.playerTwo().username();
            String summary =  match.summary();

            recentMatches.computeIfAbsent(p1, k -> new ArrayList<>()).add(summary);
            recentMatches.computeIfAbsent(p2, k -> new ArrayList<>()).add(summary);
        }
        return recentMatches;
    }

    // PR: Method is correct
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
