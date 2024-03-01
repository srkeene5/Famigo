package com.famigo.website.service;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.famigo.website.utilities.Role;
import com.famigo.website.utilities.Visibility;
import com.famigo.website.WebsiteApplication;
import com.famigo.website.model.Review;
import com.famigo.website.model.User;
import com.famigo.website.repositories.ReviewRepository;

public class ReviewSorter {

    /*
     * README
     * 
     * This function is a stable sorting algorithm for Review objects
     * 
     * It has overloaded method calls to allow simplified implementation
     * 
     * Usage: ReviewSortBy(ArrayList to be sorted, ".get function to be sorted by",
     * if you want ascending or descending,
     * (optional: 1) primary array of specific users to seperate (ex: friend list),
     * (optional: 1) move primary users to top,
     * (optional: 2) secondary array of specific users to seperate (ex: follower
     * list), (optional: 2) move secondary users to top);
     * 
     * Currently can Handle:
     * "stars"
     * "timeStamp"
     * "review"
     * 
     * 
     * 
     * Add handling in ReviewMergeBy
     * 1. Copy between the "copy comments" in ReviewMergeBy, and paste after the
     * lower comment
     * 2. Changing the four ".getLikes()" statements a new variable to sort by
     * 3. Change the if statement val.equals("likes") to the new variable name
     * 4. Update the "Currently can Handle" section in the description
     * 
     */
    public void ReviewSortBy(ArrayList<Review> revs, String val, boolean ascending) {
        ReviewsubSortBy(revs, 0, revs.size() - 1, val, ascending, null, false, null, false);
    }

    public void ReviewSortBy(ArrayList<Review> revs, String val, boolean ascending, ArrayList<User> sortPrime,
            boolean primeAscend) {
        ReviewsubSortBy(revs, 0, revs.size() - 1, val, ascending, null, false, null, false);
        ReviewsubSortBy(revs, 0, revs.size() - 1, "sortPrimeSec", ascending, sortPrime, primeAscend, null, false);
    }

    public void ReviewSortBy(ArrayList<Review> revs, String val, boolean ascending, ArrayList<User> sortPrime,
            boolean primeAscend, ArrayList<User> sortSec, boolean secAscend) {
        ReviewsubSortBy(revs, 0, revs.size() - 1, val, ascending, null, false, null, false);
        ReviewsubSortBy(revs, 0, revs.size() - 1, "sortPrimeSec", ascending, sortPrime, primeAscend, sortSec,
                secAscend);
    }

    /* _____Private Classes_____ */
    /*
     * This is the merge sort function that recursively splits
     * and recombines the subArrayList
     */
    private void ReviewsubSortBy(ArrayList<Review> revs, int s, int f, String val, boolean ascending,
            ArrayList<User> sortPrime, boolean primeAscend, ArrayList<User> sortSec, boolean secAscend) {
        if (s >= f) {
            return;
        }
        int m = s + (f - s) / 2;
        ReviewsubSortBy(revs, s, m, val, ascending, sortPrime, primeAscend, sortSec, secAscend);
        ReviewsubSortBy(revs, m + 1, f, val, ascending, sortPrime, primeAscend, sortSec, secAscend);
        ReviewMergeBy(revs, s, m, f, val, ascending, sortPrime, primeAscend, sortSec, secAscend);
    }

    /*
     * _____CHANGE HANDLING HERE_____
     * 
     * This is the Merge function, using an inplace merge
     * Comments are included to show where handling can be added
     */
    private void ReviewMergeBy(ArrayList<Review> revs, int s, int m, int f, String val, boolean ascending,
            ArrayList<User> sortPrime, boolean primeAscend, ArrayList<User> sortSec, boolean secAscend) {
        int i = s;
        int j = m + 1;
        while (i <= m && j <= f) {
            // copy for any new var to sort
            if (val.equals("stars")) {
                if ((ascending && revs.get(i).getStars() > revs.get(j).getStars())
                        || (!ascending && revs.get(i).getStars() < revs.get(j).getStars())) {
                    Review hold = revs.get(j);
                    int index = j;
                    while (index != i) {
                        revs.set(index, revs.get(index - 1));
                        index--;
                    }
                    revs.set(i, hold);
                    i++;
                    m++;
                    j++;
                } else {
                    i++;
                }
            }
            // copy for any new var to sort
            // paste after:

            if (val.equals("timeStamp")) {
                if ((ascending && revs.get(i).getTimeStamp().isAfter(revs.get(j).getTimeStamp()))
                        || (!ascending && revs.get(i).getTimeStamp().isBefore(revs.get(j).getTimeStamp()))) {
                    Review hold = revs.get(j);
                    int index = j;
                    while (index != i) {
                        revs.set(index, revs.get(index - 1));
                        index--;
                    }
                    revs.set(i, hold);
                    i++;
                    m++;
                    j++;
                } else {
                    i++;
                }
            } else if (val.equals("review")) {
                if ((ascending && revs.get(i).getReview().compareTo(revs.get(j).getReview()) > 0)
                        || (!ascending && revs.get(i).getReview().compareTo(revs.get(j).getReview()) < 0)) {
                    Review hold = revs.get(j);
                    int index = j;
                    while (index != i) {
                        revs.set(index, revs.get(index - 1));
                        index--;
                    }
                    revs.set(i, hold);
                    i++;
                    m++;
                    j++;
                } else {
                    i++;
                }
            } else if (val.equals("sortPrimeSec") && sortPrime != null) {
                int UPi = 0;
                int UPj = 0;
                if (sortSec != null) {
                    for (User user : sortSec) {
                        if (revs.get(i).getUserId().equals(user.getID())) {
                            if (secAscend) {
                                UPi = 1;
                            } else {
                                UPi = -1;
                            }
                        }
                        if (revs.get(j).getUserId().equals(user.getID())) {
                            if (secAscend) {
                                UPj = 1;
                            } else {
                                UPj = -1;
                            }
                        }
                    }
                }
                for (User user : sortPrime) {
                    if (revs.get(i).getUserId().equals(user.getID())) {
                        if (primeAscend) {
                            UPi = 2;
                        } else {
                            UPi = -2;
                        }
                    }
                    if (revs.get(j).getUserId().equals(user.getID())) {
                        if (primeAscend) {
                            UPj = 2;
                        } else {
                            UPj = -2;
                        }
                    }
                }
                if (UPi < UPj) {
                    Review hold = revs.get(j);
                    int index = j;
                    while (index != i) {
                        revs.set(index, revs.get(index - 1));
                        index--;
                    }
                    revs.set(i, hold);
                    i++;
                    m++;
                    j++;
                } else {
                    i++;
                }
            } else if (val.equals("sortPrimeSec")) {
                // TODO: handle exception
            }
        }
    }

    /* _____Main for Debugging_____ */
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WebsiteApplication.class, args);
        ReviewRepository rr = context.getBean(ReviewRepository.class);

        boolean testing = true;

        boolean testingTimeStamp = true;
        boolean testingStars = true;
        boolean testingReview = true;
        boolean testingPrime = true;
        boolean testingSec = true;

        int addruns = 10;
        int revLen = 0;
        String getBy = "place";
        String[] userIds = { "u1", "u2", "u3", "u4", "u5", "u6", "u7", "u8" };

        ArrayList<User> friends = new ArrayList<User>();
        ArrayList<User> following = new ArrayList<User>();
        friends.add(new User("u1", "user1", "testEmail1", "testPass1", "testName1", "testDescription1", Visibility.ALL,
                Role.USER));
        friends.add(new User("u2", "user2", "testEmail2", "testPass2", "testName2", "testDescription2", Visibility.ALL,
                Role.USER));
        following
                .add(new User("u3", "user3", "testEmail3", "testPass3", "testName3", "testDescription3", Visibility.ALL,
                        Role.USER));
        following
                .add(new User("u4", "user4", "testEmail4", "testPass4", "testName4", "testDescription4", Visibility.ALL,
                        Role.USER));

        ArrayList<Review> revs = new ArrayList<Review>();
        Random rand = new Random();
        if (addruns > 0) {
            System.out.println("\nMay take a moment\nWaits included to test timeStamp");
        }
        for (String userId : userIds) {
            for (int i = 0; i < addruns; i++) {
                StringBuilder buffer = new StringBuilder(revLen);
                for (int j = 0; j < revLen; j++) {
                    buffer.append((char) rand.nextInt(97, 123));
                }
                rr.addReview(new Review(userId, "place", buffer.toString(), rand.nextInt(1, 6)));
                try {
                    TimeUnit.SECONDS.sleep(rand.nextInt(1, 3));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
        if (getBy.equals("user")) {
            revs = rr.getReviewsByUser("user");
        } else {
            revs = rr.getReviewsByPlace("place");
        }

        if (revs == null) {
            testing = false;
            System.out.println("No Reviews");
        }
        ReviewSorter rs = new ReviewSorter();
        if (testing && testingTimeStamp && revs != null) {
            System.out.println("_____Unsorted_____");
            for (Review rev : revs) {
                System.out.println(rev.getTimeStamp());
            }
            rs.ReviewSortBy(revs, "timeStamp", false);
            System.out.println("_____Sorted_Desc_____");
            for (Review rev : revs) {
                System.out.println(rev.getTimeStamp());
            }
            rs.ReviewSortBy(revs, "timeStamp", true);
            System.out.println("_____Sorted_Asc_____");
            for (Review rev : revs) {
                System.out.println(rev.getTimeStamp());
            }
        }
        if (testing && testingStars && revs != null) {
            System.out.println("_____Unsorted_____");
            for (Review rev : revs) {
                System.out.println(rev.getStars());
            }
            rs.ReviewSortBy(revs, "stars", false);
            System.out.println("_____Sorted_Desc_____");
            for (Review rev : revs) {
                System.out.println(rev.getStars());
            }
            rs.ReviewSortBy(revs, "stars", true);
            System.out.println("_____Sorted_Asc_____");
            for (Review rev : revs) {
                System.out.println(rev.getStars());
            }
        }
        if (testing && testingReview && revs != null) {
            System.out.println("_____Unsorted_____");
            for (Review rev : revs) {
                System.out.println(rev.getReview());
            }
            rs.ReviewSortBy(revs, "review", false);
            System.out.println("_____Sorted_Desc_____");
            for (Review rev : revs) {
                System.out.println(rev.getReview());
            }
            rs.ReviewSortBy(revs, "review", true);
            System.out.println("_____Sorted_Asc_____");
            for (Review rev : revs) {
                System.out.println(rev.getReview());
            }
        }
        if (testing && testingPrime && revs != null) {
            System.out.println("_____Unsorted_____");
            for (Review rev : revs) {
                System.out.println("User: " + rev.getUserId() + " \t| Stars: " + rev.getStars() + " \t| TimeStamp: "
                        + rev.getTimeStamp());
            }
            rs.ReviewSortBy(revs, "timeStamp", false);
            rs.ReviewSortBy(revs, "stars", false, friends, true);
            System.out.println("_____Sorted_Desc_____");
            for (Review rev : revs) {
                System.out.println("User: " + rev.getUserId() + " \t| Stars: " + rev.getStars() + " \t| TimeStamp: "
                        + rev.getTimeStamp());
            }
            rs.ReviewSortBy(revs, "timeStamp", false);
            rs.ReviewSortBy(revs, "stars", true, friends, true);
            System.out.println("_____Sorted_Asc_____");
            for (Review rev : revs) {
                System.out.println("User: " + rev.getUserId() + " \t| Stars: " + rev.getStars() + " \t| TimeStamp: "
                        + rev.getTimeStamp());
            }
        }
        if (testing && testingSec && revs != null) {
            System.out.println("_____Unsorted_____");
            for (Review rev : revs) {
                System.out.println("User: " + rev.getUserId() + " \t| Stars: " + rev.getStars() + " \t| TimeStamp: "
                        + rev.getTimeStamp());
            }
            rs.ReviewSortBy(revs, "timeStamp", false);
            rs.ReviewSortBy(revs, "stars", false, friends, true, following, true);
            System.out.println("_____Sorted_Desc_____");
            for (Review rev : revs) {
                System.out.println("User: " + rev.getUserId() + " \t| Stars: " + rev.getStars() + " \t| TimeStamp: "
                        + rev.getTimeStamp());
            }
            rs.ReviewSortBy(revs, "timeStamp", false);
            rs.ReviewSortBy(revs, "stars", true, friends, true, following, true);
            System.out.println("_____Sorted_Asc_____");
            for (Review rev : revs) {
                System.out.println("User: " + rev.getUserId() + " \t| Stars: " + rev.getStars() + " \t| TimeStamp: "
                        + rev.getTimeStamp());
            }
        }
    }
}
