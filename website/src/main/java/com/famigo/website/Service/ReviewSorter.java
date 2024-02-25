package com.famigo.website.Service;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.famigo.website.model.Review;

public class ReviewSorter {

    /*
     * README
     * 
     * This function is for sorting Review objects by different values
     * 
     * Usage: ReviewSortBy(ArrayList to be sorted, ".get function to be sorted by",
     * if you want ascending or descending);
     * 
     * Currently can Handle:
     * "stars"
     * "timeStamp"
     * "review"
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
        ReviewsubSortBy(revs, 0, revs.size() - 1, val, ascending);
    }

    /* _____Private Classes_____ */
    /*
     * This is the merge sort function that recursively splits
     * and recombines the subArrayList
     */
    private void ReviewsubSortBy(ArrayList<Review> revs, int s, int f, String val, boolean ascending) {
        if (s >= f) {
            return;
        }
        int m = s + (f - s) / 2;
        ReviewsubSortBy(revs, s, m, val, ascending);
        ReviewsubSortBy(revs, m + 1, f, val, ascending);
        ReviewMergeBy(revs, s, m, f, val, ascending);
    }

    /*
     * _____CHANGE HANDLING HERE_____
     * 
     * This is the Merge function, using an inplace merge
     * Comments are included to show where handling can be added
     */
    private void ReviewMergeBy(ArrayList<Review> revs, int s, int m, int f, String val, boolean ascending) {
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
            }
            if (val.equals("review")) {
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
            }
        }
    }

    /* _____Main for Debugging_____ */
    public static void main(String[] args) {
        boolean testingTimeStamp = false;
        boolean testingStars = true;
        boolean testingReview = true;
        int runs = 10;
        int revLen = 10;
        if (testingTimeStamp) {
            System.out.println("May take a moment\nWaits included to test timeStamp");
            ArrayList<Review> revs = new ArrayList<Review>();
            Random rand = new Random();
            for (int i = 0; i < runs; i++) {
                Review rev = new Review("user", "place", "review", rand.nextInt(1, 6));
                try {
                    TimeUnit.SECONDS.sleep(rand.nextInt(1, 3));
                } catch (Exception e) {
                    // TODO: handle exception
                }
                revs.add(rev);
            }
            System.out.println("_____Unsorted_____");
            for (Review rev : revs) {
                System.out.println(rev.getTimeStamp());
            }
            new ReviewSorter().ReviewSortBy(revs, "timeStamp", false);
            System.out.println("_____Sorted_Desc_____");
            for (Review rev : revs) {
                System.out.println(rev.getTimeStamp());
            }
            new ReviewSorter().ReviewSortBy(revs, "timeStamp", true);
            System.out.println("_____Sorted_Asc_____");
            for (Review rev : revs) {
                System.out.println(rev.getTimeStamp());
            }
        }
        if (testingStars) {
            ArrayList<Review> revs = new ArrayList<Review>();
            Random rand = new Random();
            for (int i = 0; i < runs; i++) {
                Review rev = new Review("user", "place", "review", rand.nextInt(1, 6));
                revs.add(rev);
            }
            System.out.println("_____Unsorted_____");
            for (Review rev : revs) {
                System.out.println(rev.getStars());
            }
            new ReviewSorter().ReviewSortBy(revs, "stars", false);
            System.out.println("_____Sorted_Desc_____");
            for (Review rev : revs) {
                System.out.println(rev.getStars());
            }
            new ReviewSorter().ReviewSortBy(revs, "stars", true);
            System.out.println("_____Sorted_Asc_____");
            for (Review rev : revs) {
                System.out.println(rev.getStars());
            }
        }
        if (testingReview) {
            ArrayList<Review> revs = new ArrayList<Review>();
            Random rand = new Random();
            for (int i = 0; i < runs; i++) {
                StringBuilder buffer = new StringBuilder(revLen);
                for (int j = 0; j < revLen; j++) {
                    buffer.append((char) rand.nextInt(97, 123));
                }
                Review rev = new Review("user", "place", buffer.toString(), rand.nextInt(1, 6));
                revs.add(rev);
            }
            System.out.println("_____Unsorted_____");
            for (Review rev : revs) {
                System.out.println(rev.getReview());
            }
            new ReviewSorter().ReviewSortBy(revs, "review", false);
            System.out.println("_____Sorted_Desc_____");
            for (Review rev : revs) {
                System.out.println(rev.getReview());
            }
            new ReviewSorter().ReviewSortBy(revs, "review", true);
            System.out.println("_____Sorted_Asc_____");
            for (Review rev : revs) {
                System.out.println(rev.getReview());
            }
        }
    }
}
