package com.famigo.website.service;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.famigo.website.utilities.Role;
import com.famigo.website.utilities.Visibility;
import com.famigo.website.WebsiteApplication;
import com.famigo.website.model.Comment;
import com.famigo.website.model.User;
import com.famigo.website.repositories.CommentRepository;

public class CommentSorter {

    /*
     * README
     * 
     * This function is for sorting Comment objects by different values
     * 
     * It has overloaded method calls to allow simplified implementation
     * 
     * Usage: CommentSortBy(ArrayList to be sorted, ".get function to be sorted by",
     * if you want ascending or descending,
     * (optional: 1) primary array of specific users to seperate (ex: friend list),
     * (optional: 1) move primary users to top,
     * (optional: 2) secondary array of specific users to seperate (ex: follower
     * list), (optional: 2) move secondary users to top);
     * 
     * Currently can Handle:
     * "likes"
     * "timeStamp"
     * "comment"
     * 
     * Add handling in CommentMergeBy
     * 1. Copy between the "copy comments" in CommentMergeBy, and paste after the
     * lower comment
     * 2. Changing the four ".getLikes()" statements a new variable to sort by
     * 3. Change the if statement val.equals("likes") to the new variable name
     * 4. Update the "Currently can Handle" section in the description
     * 
     */
    public void CommentSortBy(ArrayList<Comment> coms, String val, boolean ascending) {
        CommentsubSortBy(coms, 0, coms.size() - 1, val, ascending, null, false, null, false);
    }

    public void CommentSortBy(ArrayList<Comment> coms, String val, boolean ascending, ArrayList<User> sortPrime,
            boolean primeAscend) {
        CommentsubSortBy(coms, 0, coms.size() - 1, val, ascending, null, false, null, false);
        CommentsubSortBy(coms, 0, coms.size() - 1, "sortPrimeSec", ascending, sortPrime, primeAscend, null, false);
    }

    public void CommentSortBy(ArrayList<Comment> coms, String val, boolean ascending, ArrayList<User> sortPrime,
            boolean primeAscend, ArrayList<User> sortSec, boolean secAscend) {
        CommentsubSortBy(coms, 0, coms.size() - 1, val, ascending, null, false, null, false);
        CommentsubSortBy(coms, 0, coms.size() - 1, "sortPrimeSec", ascending, sortPrime, primeAscend, sortSec,
                secAscend);
    }

    /* _____Private Classes_____ */
    /*
     * This is the merge sort function that recursively splits
     * and recombines the subArrayList
     */
    private void CommentsubSortBy(ArrayList<Comment> coms, int s, int f, String val, boolean ascending,
            ArrayList<User> sortPrime,
            boolean primeAscend, ArrayList<User> sortSec, boolean secAscend) {
        if (s >= f) {
            return;
        }
        int m = s + (f - s) / 2;
        CommentsubSortBy(coms, s, m, val, ascending, sortPrime, primeAscend, sortSec, secAscend);
        CommentsubSortBy(coms, m + 1, f, val, ascending, sortPrime, primeAscend, sortSec, secAscend);
        CommentMergeBy(coms, s, m, f, val, ascending, sortPrime, primeAscend, sortSec, secAscend);
    }

    /*
     * _____CHANGE HANDLING HERE_____
     * 
     * This is the Merge function, using an inplace merge
     * Comments are included to show where handling can be added
     */
    private void CommentMergeBy(ArrayList<Comment> coms, int s, int m, int f, String val, boolean ascending,
            ArrayList<User> sortPrime,
            boolean primeAscend, ArrayList<User> sortSec, boolean secAscend) {
        int i = s;
        int j = m + 1;
        while (i <= m && j <= f) {
            // copy for any new var to sort
            if (val.equals("likes")) {
                if ((ascending && coms.get(i).getLikes() > coms.get(j).getLikes())
                        || (!ascending && coms.get(i).getLikes() < coms.get(j).getLikes())) {
                    Comment hold = coms.get(j);
                    int index = j;
                    while (index != i) {
                        coms.set(index, coms.get(index - 1));
                        index--;
                    }
                    coms.set(i, hold);
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
                if ((ascending && coms.get(i).getTimeStamp().isAfter(coms.get(j).getTimeStamp()))
                        || (!ascending && coms.get(i).getTimeStamp().isBefore(coms.get(j).getTimeStamp()))) {
                    Comment hold = coms.get(j);
                    int index = j;
                    while (index != i) {
                        coms.set(index, coms.get(index - 1));
                        index--;
                    }
                    coms.set(i, hold);
                    i++;
                    m++;
                    j++;
                } else {
                    i++;
                }
            } else if (val.equals("comment")) {
                if ((ascending && coms.get(i).getComment().compareTo(coms.get(j).getComment()) > 0)
                        || (!ascending && coms.get(i).getComment().compareTo(coms.get(j).getComment()) < 0)) {
                    Comment hold = coms.get(j);
                    int index = j;
                    while (index != i) {
                        coms.set(index, coms.get(index - 1));
                        index--;
                    }
                    coms.set(i, hold);
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
                        if (coms.get(i).getUserId().equals(user.getID())) {
                            if (secAscend) {
                                UPi = 1;
                            } else {
                                UPi = -1;
                            }
                        }
                        if (coms.get(j).getUserId().equals(user.getID())) {
                            if (secAscend) {
                                UPj = 1;
                            } else {
                                UPj = -1;
                            }
                        }
                    }
                }
                for (User user : sortPrime) {
                    if (coms.get(i).getUserId().equals(user.getID())) {
                        if (primeAscend) {
                            UPi = 2;
                        } else {
                            UPi = -2;
                        }
                    }
                    if (coms.get(j).getUserId().equals(user.getID())) {
                        if (primeAscend) {
                            UPj = 2;
                        } else {
                            UPj = -2;
                        }
                    }
                }
                if (UPi < UPj) {
                    Comment hold = coms.get(j);
                    int index = j;
                    while (index != i) {
                        coms.set(index, coms.get(index - 1));
                        index--;
                    }
                    coms.set(i, hold);
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
        CommentRepository cr = context.getBean(CommentRepository.class);

        boolean testing = true;

        boolean testingTimeStamp = true;
        boolean testingLikes = true;
        boolean testingComment = true;
        boolean testingPrime = true;
        boolean testingSec = true;

        int comLen = 10;
        int likeCap = 1000;
        int addruns = 0;
        String getBy = "review";
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

        ArrayList<Comment> coms = new ArrayList<Comment>();
        Random rand = new Random();

        if (addruns > 0) {
            System.out.println("\nMay take a moment\nWaits included to test timeStamp");
        }
        for (String userId : userIds) {
            for (int i = 0; i < addruns; i++) {
                StringBuilder buffer = new StringBuilder(comLen);
                for (int j = 0; j < comLen; j++) {
                    buffer.append((char) rand.nextInt(97, 123));
                }
                cr.addComment(new Comment(userId, "review", buffer.toString(), rand.nextInt(likeCap)));
                try {
                    TimeUnit.SECONDS.sleep(rand.nextInt(1, 3));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }

        if (getBy.equals("user")) {
            coms = cr.getCommentsByUser("user");
        } else {
            coms = cr.getCommentsByReview("review");
        }

        if (coms == null) {
            testing = false;
            System.out.println("No Comments");
        }

        CommentSorter cs = new CommentSorter();
        if (testing && testingTimeStamp && coms != null) {
            System.out.println("_____Unsorted_____");
            for (Comment com : coms) {
                System.out.println(com.getTimeStamp());
            }
            cs.CommentSortBy(coms, "timeStamp", false);
            System.out.println("_____Sorted_Desc_____");
            for (Comment com : coms) {
                System.out.println(com.getTimeStamp());
            }
            cs.CommentSortBy(coms, "timeStamp", true);
            System.out.println("_____Sorted_Asc_____");
            for (Comment com : coms) {
                System.out.println(com.getTimeStamp());
            }
        }
        if (testing && testingLikes && coms != null) {
            System.out.println("_____Unsorted_____");
            for (Comment com : coms) {
                System.out.println(com.getLikes());
            }
            cs.CommentSortBy(coms, "likes", false);
            System.out.println("_____Sorted_Desc_____");
            for (Comment com : coms) {
                System.out.println(com.getLikes());
            }
            cs.CommentSortBy(coms, "likes", true);
            System.out.println("_____Sorted_Asc_____");
            for (Comment com : coms) {
                System.out.println(com.getLikes());
            }
        }
        if (testing && testingComment && coms != null) {
            System.out.println("_____Unsorted_____");
            for (Comment com : coms) {
                System.out.println(com.getComment());
            }
            cs.CommentSortBy(coms, "comment", false);
            System.out.println("_____Sorted_Desc_____");
            for (Comment com : coms) {
                System.out.println(com.getComment());
            }
            cs.CommentSortBy(coms, "comment", true);
            System.out.println("_____Sorted_Asc_____");
            for (Comment com : coms) {
                System.out.println(com.getComment());
            }
        }
        if (testing && testingPrime && coms != null) {
            System.out.println("_____Unsorted_____");
            for (Comment com : coms) {
                System.out.println("User: " + com.getUserId() + " \t| Likes: " + com.getLikes() + " \t| TimeStamp: "
                        + com.getTimeStamp());
            }
            cs.CommentSortBy(coms, "timeStamp", false);
            cs.CommentSortBy(coms, "likes", false, friends, true);
            System.out.println("_____Sorted_Desc_____");
            for (Comment com : coms) {
                System.out.println("User: " + com.getUserId() + " \t| Likes: " + com.getLikes() + " \t| TimeStamp: "
                        + com.getTimeStamp());
            }
            cs.CommentSortBy(coms, "timeStamp", false);
            cs.CommentSortBy(coms, "likes", true, friends, true);
            System.out.println("_____Sorted_Asc_____");
            for (Comment com : coms) {
                System.out.println("User: " + com.getUserId() + " \t| Likes: " + com.getLikes() + " \t| TimeStamp: "
                        + com.getTimeStamp());
            }
        }
        if (testing && testingSec && coms != null) {
            System.out.println("_____Unsorted_____");
            for (Comment com : coms) {
                System.out.println("User: " + com.getUserId() + " \t| Likes: " + com.getLikes() + " \t| TimeStamp: "
                        + com.getTimeStamp());
            }
            cs.CommentSortBy(coms, "timeStamp", false);
            cs.CommentSortBy(coms, "likes", false, friends, true, following, true);
            System.out.println("_____Sorted_Desc_____");
            for (Comment com : coms) {
                System.out.println("User: " + com.getUserId() + " \t| Likes: " + com.getLikes() + " \t| TimeStamp: "
                        + com.getTimeStamp());
            }
            cs.CommentSortBy(coms, "timeStamp", false);
            cs.CommentSortBy(coms, "likes", true, friends, true, following, true);
            System.out.println("_____Sorted_Asc_____");
            for (Comment com : coms) {
                System.out.println("User: " + com.getUserId() + " \t| Likes: " + com.getLikes() + " \t| TimeStamp: "
                        + com.getTimeStamp());
            }
        }
    }
}
