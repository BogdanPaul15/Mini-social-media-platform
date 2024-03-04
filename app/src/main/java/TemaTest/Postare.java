package TemaTest;

import jdk.jshell.execution.Util;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import static TemaTest.Utilizator.getUser;

public class Postare implements Likeable{
    private static int nextIdPosts = 0;
    private Integer post_id;
    private String post_text;
    private String post_date;
    private String username;
    private ArrayList<Integer> commentsIds;
    private ArrayList<Integer> likedBy;
    private Integer number_of_likes;
    private ArrayList<Comentariu> comments;
    private Integer number_of_comments;

    Postare() {
        nextIdPosts++;
        this.post_id = nextIdPosts;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        this.post_date = dateFormat.format(date);
    }
    public Postare(String text, String username) {
        this();
        this.post_text = text;
        this.username = username;
        this.commentsIds = new ArrayList<>();
        this.likedBy = new ArrayList<>();
        this.number_of_likes = 0;
        this.number_of_comments = 0;
    }
    public Postare(String post_text, String post_date, String username, Integer number_of_likes, ArrayList<Comentariu> comments) {
        this.post_text = post_text;
        this.post_date = post_date;
        this.username = username;
        this.number_of_likes = number_of_likes;
        this.comments = comments;
    }
    public Postare(Integer postId, String postText, String postDate) {
        this.post_id = postId;
        this.post_text = postText;
        this.post_date = postDate;
    }
    public Postare(Integer postId, String postText, String postDate, String username) {
        this.post_id = postId;
        this.post_text = postText;
        this.post_date = postDate;
        this.username = username;
    }
    public Postare(Integer post_id, String post_text, String post_date, String username, int number_of_likes) {
        this.post_id = post_id;
        this.post_text = post_text;
        this.post_date = post_date;
        this.username = username;
        this.number_of_likes = number_of_likes;
    }
    public static Postare createPartialPostComm(Integer post_id, String post_text, String post_date, String username, int number_of_comments) {
        Postare partialPost = new Postare();
        partialPost.post_id = post_id;
        partialPost.post_text = post_text;
        partialPost.post_date = post_date;
        partialPost.username = username;
        partialPost.number_of_comments = number_of_comments;
        return partialPost;
    }
    public static void setNextIdPosts(int nextIdPosts) {
        Postare.nextIdPosts = nextIdPosts;
    }
    public String getUsername() {
        return username;
    }
    public int getNumberOfLikes() {
        return number_of_likes;
    }
    public void updateNumberOfLikes() {
        this.number_of_likes = likedBy.size();
    }
    public void updateNumberOfComments() {
        this.number_of_comments = commentsIds.size();
    }
    public int getId() {
        return post_id;
    }
    public static Postare getPost(ArrayList<Postare> posts, Integer id) {
        // Get post with provided ID
        for (Postare post: posts) {
            if (Objects.equals(post.post_id, id)) {
                return post;
            }
        }
        return null;
    }
    public ArrayList<Integer> getComments() {
        return commentsIds;
    }

    @Override
    public void like(Utilizator user) {
        if (!Objects.equals(user.getUsername(), this.username)) {
            // Like only if current user is not the owner of the post
            user.getLikedPosts().add(this.post_id);
            this.likedBy.add(user.getId());
            updateNumberOfLikes();
        }
    }

    @Override
    public void unlike(Utilizator user) {
        // Unlike the post
        user.getLikedPosts().removeIf(id -> id.equals(this.post_id));
        this.likedBy.removeIf(id -> id.equals(user.getId()));
        updateNumberOfLikes();
    }

    static ArrayList<Postare> getMostLikedPosts(ArrayList<Postare> Posts) {
        // Return 5 most liked posts into a custom format (ordered by number of likes)
        return Posts.stream()
                .sorted(Comparator.comparingInt(post -> -post.number_of_likes))
                .limit(5)
                .map(post -> new Postare(post.post_id, post.post_text, post.post_date, post.username, post.number_of_likes))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    static ArrayList<Postare> getMostCommentedPosts(ArrayList<Postare> Posts) {
        // Return top 5 most commented posts into a custom format (ordered by number of comments)
        return Posts.stream()
                .sorted(Comparator.comparingInt(post -> -post.number_of_comments))
                .limit(5)
                .map(post -> Postare.createPartialPostComm(post.post_id, post.post_text, post.post_date, post.username, post.number_of_comments))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    static ArrayList<Postare> getUserPosts(ArrayList<Postare> Posts, String username) {
        // Return all posts created by a specific user (ordered by id)
        return Posts.stream()
                .filter(post -> post.username.equals(username))
                .sorted(Comparator.comparing(post -> -post.post_id))
                .map(post -> new Postare(post.post_id, post.post_text, post.post_date))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    static ArrayList<Postare> getFollowingPosts(ArrayList<Postare> Posts, ArrayList<Utilizator> Users, Utilizator currentUser) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        ArrayList<Postare> followingPosts = new ArrayList<>();

        // Get all posts from the following users
        for (String followingUsername : currentUser.getFollowing()) {
            if (getUser(Users, followingUsername) != null) {
                ArrayList<Postare> followingUserPosts = Posts.stream()
                        .filter(post -> post.username.equals(followingUsername))
                        .map(post -> new Postare(post.post_id, post.post_text, post.post_date, post.username))
                        .collect(Collectors.toCollection(ArrayList::new));

                followingPosts.addAll(followingUserPosts);
            }
        }
        // Sort posts by date, and if all dates are equal, sort by id in descending order
        followingPosts.sort(Comparator.comparing((Postare post) -> LocalDate.parse(post.post_date, dateFormatter))
                .thenComparing(Comparator.comparingInt(Postare::getId).reversed()));
        return followingPosts;
    }
    static ArrayList<Postare> getPostDetails(ArrayList<Postare> Posts, ArrayList<Comentariu> Comments, String post_id) {
        // Get all specific post details
        ArrayList<Comentariu> postComms = new ArrayList<>();
        for (Comentariu comm : Comments) {
            if (comm.getPostId() == Integer.parseInt(post_id)) {
                postComms.add(new Comentariu(comm.getId(), comm.getCommentText(), comm.getCommentDate(), comm.getUsername(), comm.getNumberOfLikes()));
            }
        }
        Postare actualPost = getPost(Posts, Integer.parseInt(post_id));
        Postare post = new Postare(actualPost.post_text, actualPost.post_date, actualPost.username, actualPost.number_of_likes, postComms);
        ArrayList<Postare> postari = new ArrayList<>();
        postari.add(post);
        return postari;
    }
}
