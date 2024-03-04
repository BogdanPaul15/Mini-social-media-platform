package TemaTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Comentariu implements Likeable{
    private static int nextIdComm = 0;
    private Integer comment_id;
    private String comment_text;
    private final String comment_date;
    private Integer ownerId;
    private Integer postId;
    private String username;
    private Integer number_of_likes;
    private ArrayList<Integer> likedBy;

    Comentariu() {
        nextIdComm++;
        this.comment_id = nextIdComm;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        this.comment_date = dateFormat.format(date);
    }
    public Comentariu(String text, Integer ownerId, Integer postId, String username) {
        this();
        this.comment_text = text;
        this.username = username;
        this.ownerId = ownerId;
        this.postId = postId;
        this.number_of_likes = 0;
        this.likedBy = new ArrayList<>();
    }
    public Comentariu(Integer comment_id, String comment_text, String comment_date, String username, Integer number_of_likes) {
        this.comment_id = comment_id;
        this.comment_text = comment_text;
        this.comment_date = comment_date;
        this.username = username;
        this.number_of_likes = number_of_likes;
    }
    public static void setNextIdComm(int nextIdComm) {
        Comentariu.nextIdComm = nextIdComm;
    }
    public int getId() {
        return comment_id;
    }
    public String getCommentText() {
        return comment_text;
    }
    public String getCommentDate() {
        return comment_date;
    }
    public int getNumberOfLikes() {
        return number_of_likes;
    }
    public String getUsername() {
        return username;
    }
    public Integer getPostId() {
        return postId;
    }
    public int getOwnerId() {
        return ownerId;
    }
    public ArrayList<Integer> getLikedBy() {
        return likedBy;
    }
    public static Comentariu getComment(ArrayList<Comentariu> comments, Integer id) {
        // Get the comment with a specific ID
        for (Comentariu comment: comments) {
            if (Objects.equals(comment.comment_id, id)) {
                return comment;
            }
        }
        return null;
    }

    public void updateNumberOfLikes() {
        this.number_of_likes = likedBy.size();
    }
    @Override
    public void like(Utilizator user) {
        if (user.getId() != this.ownerId) {
            // Check if the user is not the owner of the comment and like the comment
            user.getLikedComments().add(this.comment_id);
            this.likedBy.add(user.getId());
            updateNumberOfLikes();
        }
    }

    @Override
    public void unlike(Utilizator user) {
        // Unlike the comment
        user.getLikedComments().removeIf(id -> id.equals(this.comment_id));
        this.likedBy.removeIf(id -> id.equals(user.getId()));
        updateNumberOfLikes();
    }
}
