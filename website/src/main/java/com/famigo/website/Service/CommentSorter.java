package com.famigo.website.Service;

import java.util.ArrayList;
import java.util.Random;

import com.famigo.website.model.Comment;

public class CommentSorter {
    /*
     * README
     * 
     * This function is for sorting Comment objects by different values
     * 
     * Usage: CommentSortBy(ArrayList to be sorted, ".get function to be sorted by",
     * if you want ascending or descending);
     * 
     * Currently can Handle:
     * "likes"
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
        CommentsubSortBy(coms, 0, coms.size() - 1, val, ascending);
    }

    /* _____Private Classes_____ */
    /*
     * This is the merge sort function that recursively splits
     * and recombines the subArrayList
     */
    private void CommentsubSortBy(ArrayList<Comment> coms, int s, int f, String val, boolean ascending) {
        if (s >= f) {
            return;
        }
        int m = s + (f - s) / 2;
        CommentsubSortBy(coms, s, m, val, ascending);
        CommentsubSortBy(coms, m + 1, f, val, ascending);
        CommentMergeBy(coms, s, m, f, val, ascending);
    }

    /*
     * _____CHANGE HANDLING HERE_____
     * 
     * This is the Merge function, using an inplace merge
     * Comments are included to show where handling can be added
     */
    private void CommentMergeBy(ArrayList<Comment> coms, int s, int m, int f, String val, boolean ascending) {
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
        }
    }

    /* _____Main for Debugging_____ */
    public static void main(String[] args) {
        boolean testing = true;
        int likeCap = 1000;
        int runs = 10;
        if (testing) {
            ArrayList<Comment> coms = new ArrayList<Comment>();
            Random rand = new Random();
            for (int i = 0; i < runs; i++) {
                Comment com = new Comment();
                com.setLikes(rand.nextInt(likeCap));
                coms.add(com);
            }
            System.out.println("_____Unsorted_____");
            for (Comment com : coms) {
                System.out.println(com.getLikes());
            }
            new CommentSorter().CommentSortBy(coms, "likes", true);
            System.out.println("_____Sorted_Asc_____");
            for (Comment com : coms) {
                System.out.println(com.getLikes());
            }
            new CommentSorter().CommentSortBy(coms, "likes", false);
            System.out.println("_____Sorted_Desc_____");
            for (Comment com : coms) {
                System.out.println(com.getLikes());
            }
        }
    }
}
