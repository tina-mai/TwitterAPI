import java.util.ArrayList;

public class TwitterWord {
    private String word;
    // keeps track of the average likes and times the word was tweeted

    // array lists with the average likes and corresponding hours
    private ArrayList<Integer> likes;
    private ArrayList<Integer> times;

    public TwitterWord(String word){
        this.word = word;
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

