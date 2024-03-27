package com.famigo.website.controller;

import ch.qos.logback.classic.pattern.Util;
import java.util.ArrayList;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.famigo.website.model.Signup;
import com.famigo.website.utilities.Utilities;

import com.famigo.website.repositories.UserRepository;
import com.famigo.website.repositories.FollowingRepository;
import com.famigo.website.repositories.PlaceRepository;
import com.famigo.website.repositories.ReviewRepository;
import com.famigo.website.repositories.CommentRepository;
import com.famigo.website.model.User;
import com.famigo.website.model.SubFollow;
import com.famigo.website.model.Review;
import com.famigo.website.model.Comment;

@Controller
public class UserPageController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	FollowingRepository followRepository;
	@Autowired
	ReviewRepository revRepo;
	@Autowired
	CommentRepository comRepo;
	@Autowired
	PlaceRepository placeRepo;

	@GetMapping("/user")
	public String greeting(Model model) {
		User user = userRepository.getUser("id", Utilities.getUserID());
		model.addAttribute("signup", new Signup());
		model.addAttribute("userpage", user);
		model.addAttribute("user_logged_in", user);
		// Should probably return a different user page or use some th:if statements so
		// that you don't have to return this
		model.addAttribute("follower_count_of_user", followRepository.getNumFollowers(user.getUsername()));
		model.addAttribute("following_count_of_user",
				followRepository.getNumFollowing(user.getUsername()));
		model.addAttribute("followlist", followRepository.getFollowersList(user.getUsername()));
		return "userpage";
	}

	@GetMapping("/user/{username}")
	public String userPage(@PathVariable String username, Model model) {
		System.out.println(username);
		User user_being_viewed = userRepository.getUser("username", username/* util.getUserID() */); // accidentally
																										// made the user
																										// always go to
																										// their own
																										// page, oops
		User user_logged_in = userRepository.getUser("id", Utilities.getUserID());
		SubFollow followerCount = new SubFollow();
		// followerCount.setFollowerCount(followRepository.getFollowerCount(user_being_viewed.getUsername()));
		// debugging
		if (user_being_viewed != null) {
			System.out.println("username of user's page being viewed: " + user_being_viewed.getUsername());
			// System.out.println("password of user's page being viewed: " +
			// user_being_viewed.getPassword());
			System.out.println("Logged in user: " + user_logged_in.getID());
			// System.out.println("Num followers of logged-in user: " +
			// user_logged_in.getFollowerCount());
			// System.out.println("Num followers of USER BEING VIEWED: " +
			// user_being_viewed.getFollowerCount());
		}
		System.out.println("~~~~~~~user page controller~~~~~~~~~~~");
		System.out.println("Follower count: " + followRepository.getNumFollowers(user_being_viewed.getID()));
		System.out.println("Following count " + followRepository.getNumFollowing(user_being_viewed.getID()));
		System.out.println("Followers list " + followRepository.getFollowersList(user_being_viewed.getID()));
		System.out.println("Following list " + followRepository.getFollowingList(user_being_viewed.getID()));
		System.out.println("~~~~~~~~~~~~~~~~~~");

		model.addAttribute("userpage", user_being_viewed); // get Username
		model.addAttribute("user_logged_in", user_logged_in); // get Username
		model.addAttribute("follower_count_of_user", followRepository.getNumFollowers(user_being_viewed.getID()));
		model.addAttribute("following_count_of_user",
				followRepository.getNumFollowing(user_being_viewed.getID()));
		model.addAttribute("followlist", followRepository.getFollowersList(user_being_viewed.getID()));
		return "userpage";
	}

	@GetMapping("/user/{username}/review-data")
	public String userReviewStats(@PathVariable String username, /*@RequestParam(value = "<webVarName>") <varType> <localVarName>*/ Model model) {
		User chosenUser = userRepository.getUser("username", username);

		ArrayList<Review> revs = revRepo.getReviewsByUser(chosenUser.getID());
		if (revs == null) {
			revs = new ArrayList<Review>();
		}

		ArrayList<ArrayList<Comment>> revComments = new ArrayList<ArrayList<Comment>>();
		int commentCount = 0;
		int totalRevLikes = 0;
		int totalComLikes = 0;
		int ratingSum = 0;
		for (Review r : revs) {
			ratingSum += r.getStars();
			totalRevLikes += r.getLikes();
			// Insert ArrayList of comments from that review into 2D comment ArrayList
			ArrayList<Comment> coms = comRepo.getCommentsByReview(r.getRevId());
			if (coms == null) {
				coms = new ArrayList<Comment>();
			}
			revComments.add(coms);
			for (Comment c : coms) {
				commentCount++;
				totalComLikes += c.getLikes();
			}
		}

		int[] revReactions = revRepo.getUserReviewReactions(Utilities.getUserID(), revs);

		float avgRevLikes = 0;
		float avgComsPerRev = 0;
		float avgRating = 0;
		if (revs.size() > 0) {
			avgRevLikes = (float) totalRevLikes / revs.size();
			avgComsPerRev = commentCount / revs.size();
			avgRating = ratingSum / revs.size();

		}
		float avgComLikes = 0;
		if (revComments.size() > 0) {
			avgComLikes = (float) totalComLikes / revComments.size();
		}

		String[] placeNames = new String[revs.size()];
		for (int i = 0; i < placeNames.length; i++) {
			placeNames[i] = placeRepo.getPlaceById(revs.get(i).getPlaceId()).getName();
		}

		int[] followVals = new int[revs.size()];
		for (int i = 0; i < followVals.length; i++) {
			followVals[i] = followRepository.getFollowerStatus(Utilities.getUserID(), revs.get(i).getUserId());
		}

		// Add public data to model
		model.addAttribute("username", chosenUser.getUsername());
		model.addAttribute("reviews", revs);
		model.addAttribute("revCount", revs.size());
		model.addAttribute("revReactions", revReactions);
		model.addAttribute("totalRevLikes", totalRevLikes);
		model.addAttribute("comments", revComments);
		model.addAttribute("comCount", commentCount);
		model.addAttribute("totalComLikes", totalComLikes);
		model.addAttribute("placeNames", placeNames);
		model.addAttribute("followValList", followVals);

		System.out.print("Follow vals: ");
		for (int i : followVals) {
			System.out.print(i + ", ");
		}
		System.out.println();

		// Add private data to model only if user logged in and user being viewed match.
		// If not, set the attributes to null. This must be done server side for increased security.
		if (Utilities.getUserID().equals(chosenUser.getID())) {
			model.addAttribute("avgRevLikes", avgRevLikes);
			model.addAttribute("avgComLikes", avgComLikes);
			model.addAttribute("avgComsPerRev", avgComsPerRev);
			model.addAttribute("avgRating", avgRating);
		} else {
			model.addAttribute("avgRevLikes", -1);
			model.addAttribute("avgComLikes", -1);
			model.addAttribute("avgComsPerRev", -1);
			model.addAttribute("avgRating", -1);
		}

		return "userReviews";
	}

	@RequestMapping(value = "/user/{username}/review-data/addRevReact", method = RequestMethod.POST)
    public ResponseEntity<String> likeOrDislike(@RequestParam(value = "vals[]") int[] vals) {
        // Save like/dislike action
        // vals[0] = like (1) or dislike (0), and values[1] = review ID
        if (vals[0] == 1) {
            // Like button pressed
            revRepo.alterReviewReaction(Utilities.getUserID(), vals[1], true);
        } else {
            // Dislike button pressed
            revRepo.alterReviewReaction(Utilities.getUserID(), vals[1], false);
        }

        return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
    }
}