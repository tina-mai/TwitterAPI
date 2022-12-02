import java.util.ArrayList;

// this class is not used in our code

public class TwitterUser {

    private String username;
    private ArrayList<Integer> likes;
    private ArrayList<Integer> times;

    public TwitterUser(String username) {
        this.username = username;
        likes = new ArrayList<Integer>();
        times = new ArrayList<Integer>();
    }

    public void addLike(int like) {
        likes.add(like);
    }

    public void addTime(int time) {
        times.add(time);
    }

    public ArrayList<Integer> getLikes() {
        return likes;
    }

    public ArrayList<Integer> getTimes() {
        return times;
    }

}
