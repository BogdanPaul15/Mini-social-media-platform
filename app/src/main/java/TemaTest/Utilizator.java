package TemaTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

import static TemaTest.Postare.getPost;

public class Utilizator {
    private static int nextIdUsers = 0;
    private Integer id;
    private String username;
    private String password;
    private ArrayList<Integer> createdPosts;
    private ArrayList<Integer> createdComments;
    private ArrayList<String> following;
    private ArrayList<String> followers;
    private ArrayList<Integer> likedPosts;
    private ArrayList<Integer> likedComments;
    private Integer number_of_followers;
    private Integer number_of_likes;

    Utilizator() {
        nextIdUsers++;
        this.id = nextIdUsers;
    }

    public Utilizator(String username, String password) {
        this();
        this.username = username;
        this.password = password;
        this.createdPosts = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.createdComments = new ArrayList<>();
        this.likedPosts = new ArrayList<>();
        this.likedComments = new ArrayList<>();
    }
    public Utilizator(String username, Integer number_of_followers) {
        this.username = username;
        this.number_of_followers = number_of_followers;
    }

    public Utilizator(String nume, int number_of_likes) {
        this.username = nume;
        this.number_of_likes = number_of_likes;
    }
    public static void setNextIdUsers(int nextIdUsers) {
        Utilizator.nextIdUsers = nextIdUsers;
    }
    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public ArrayList<Integer> getLikedPosts() {
        return likedPosts;
    }
    public ArrayList<Integer> getLikedComments() {
        return likedComments;
    }
    public int getNumberOfLikes() {
        return number_of_likes;
    }
    public ArrayList<String> getFollowers() {
        return followers;
    }
    public ArrayList<String> getFollowing() {
        return following;
    }

    public Integer getNumberOfFollowers() {
        this.number_of_followers = followers.size();
        return number_of_followers;
    }
    public void addCreatedPost(Integer postId) {
        createdPosts.add(postId);
    }
    public void updateNumberOfFollowers() {
        this.number_of_followers = followers.size();
    }
    public static boolean userAlreadyExists(ArrayList<Utilizator> users, String username) {
        // Check if username already exists
        for (Utilizator user : users) {
            if (user.username.equals(username)) {
                return false;
            }
        }
        return true;
    }
    public static Utilizator loginUser(ArrayList<Utilizator> users, String username, String password) {
        // Check if the provided credentials actually exist
        for (Utilizator user: users) {
            if (Objects.equals(user.username, username) && Objects.equals(user.password, password)) {
                return user;
            }
        }
        return null;
    }
    public void deletePost(ArrayList<Postare> Posts, Integer id) {
        // Delete post with specific ID
        Posts.removeIf(postare -> postare.getId() == id);
        this.createdPosts.removeIf(post -> post.equals(id));
    }
    public boolean checkValidPost(Integer id) {
        // Check if the current user actually created the post
        return this.createdPosts.contains(id);
    }
    public boolean checkValidUserFollow(ArrayList<Utilizator> users, String username) {
        // Check if the provided username is valid to follow
        if (this.following.contains(username)) {
            return false;
        }
        for (Utilizator user: users) {
            if (Objects.equals(user.getUsername(), username)) {
                return true;
            }
        }
        return false;
    }
    public boolean checkValidUserUnfollow(String username) {
        // Check if the provided username is valid to unfollow
        return this.following.contains(username);
    }
    public static Utilizator getUser(ArrayList<Utilizator> users, String username) {
        // Get user with specific username
        for (Utilizator user: users) {
            if (Objects.equals(user.username, username)) {
                return user;
            }
        }
        return null;
    }
    public void follow(ArrayList<Utilizator> users, String usernameToFollow) {
        // Follow the user with the provided username
        Utilizator userToFollow = getUser(users, usernameToFollow);
        this.following.add(userToFollow.username);
        userToFollow.followers.add(this.username);
        userToFollow.updateNumberOfFollowers();
    }
    public void unfollow(ArrayList<Utilizator> users, String usernameToUnfollow) {
        // Unfollow the user with the provided username
        Utilizator userToUnfollow = getUser(users, usernameToUnfollow);
        this.following.remove(userToUnfollow.username);
        userToUnfollow.followers.remove(this.username);
        userToUnfollow.updateNumberOfFollowers();
    }

    public void addComment(ArrayList<Postare> posts, ArrayList<Comentariu> comments, Integer postId, String text) {
        // Add a comment to specific post
        Postare post = getPost(posts, postId);
        Comentariu comment = new Comentariu(text, this.id, post.getId(), this.username);
        comments.add(comment);
        this.createdComments.add(comment.getId());
        post.getComments().add(comment.getId());
        post.updateNumberOfComments();
    }

    public void deleteComment(ArrayList<Postare> posts, ArrayList<Comentariu> comments, Integer commId) {
        // Delete comment with specific ID
        for (Postare post: posts) {
            post.getComments().remove(commId);
            post.updateNumberOfComments();
        }
        comments.removeIf(comentariu -> comentariu.getId() == commId);
        this.createdComments.remove(commId);
    }

    public boolean checkValidComment(Integer id) {
        // Check if current user actually created the comment with the provided ID
        return this.createdComments.contains(id);
    }
    boolean alreadyLikedPost(Integer id) {
        // Check if current user already liked the post with the provided ID
        return this.likedPosts.contains(id);
    }
    boolean alreadyLikedComment(Integer id) {
        // Check if current user already liked the comment with the provided ID
        return this.likedComments.contains(id);
    }
    static ArrayList<Utilizator> getMostFollowedUsers(ArrayList<Utilizator> Users) {
        // Return the top 5 most followed users
        return Users.stream()
                .sorted(Comparator.comparingInt(user -> -user.getNumberOfFollowers()))
                .limit(5)
                .map(user -> new Utilizator(user.username, user.getNumberOfFollowers()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    static ArrayList<Utilizator> getMostLikedUsers(ArrayList<Utilizator> Users, ArrayList<Postare> Posts, ArrayList<Comentariu> Comments) {
        ArrayList<Utilizator> mostLikedUsers = new ArrayList<>();
        for (Utilizator user : Users) {
            int total_likes = 0;
            // Compute number of likes of all created posts
            for (Postare post : Posts) {
                if (Objects.equals(post.getUsername(), user.username)) {
                    total_likes += post.getNumberOfLikes();
                }
            }
            // Compute number of likes of all created comments
            for (Comentariu comment : Comments) {
                if (Objects.equals(comment.getOwnerId(), user.id)) {
                    total_likes += comment.getLikedBy().size();
                }
            }
            // Add to an array each username with its total received likes
            mostLikedUsers.add(new Utilizator(user.username, total_likes));
        }
        // Get 5 most liked users
        mostLikedUsers.sort(Comparator.comparingInt(Utilizator::getNumberOfLikes).reversed());
        return new ArrayList<>(mostLikedUsers.subList(0, 5));
    }
}
