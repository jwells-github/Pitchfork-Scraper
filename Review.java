package pitchforkscraper;
import org.jsoup.nodes.Element;

// A model object to hold information about reviews

public class Review {
    
    private Element reviewToScrape;
    private String[] reviewArtist;
    private String[] reviewAlbum;
    private String reviewGenre;
    private String reviewReviewer;
    private String reviewDate;
    private String[] reviewScore;
    private Boolean bestNewMusic = false;
    
    Review(Element review){
        reviewToScrape = review;
    }

    public Element getReviewToScrape() {
        return reviewToScrape;
    }

    public void setReviewToScrape(Element reviewToScrape) {
        this.reviewToScrape = reviewToScrape;
    }

    public String[] getReviewArtist() {
        return reviewArtist;
    }

    public void setReviewArtist(String[] reviewArtist) {
        this.reviewArtist = reviewArtist;
    }

    public String[] getReviewAlbum() {
        return reviewAlbum;
    }

    public void setReviewAlbum(String[] reviewAlbum) {
        this.reviewAlbum = reviewAlbum;
    }

    public String getReviewGenre() {
        return reviewGenre;
    }

    public void setReviewGenre(String reviewGenre) {
        this.reviewGenre = reviewGenre;
    }

    public String getReviewReviewer() {
        return reviewReviewer;
    }

    public void setReviewReviewer(String reviewReviewer) {
        this.reviewReviewer = reviewReviewer;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String[] getReviewScore() {
        return reviewScore;
    }

    public void setReviewScore(String[] reviewScore) {
        this.reviewScore = reviewScore;
    }

    public Boolean getBestNewMusic() {
        return bestNewMusic;
    }

    public void setBestNewMusic(Boolean bestNewMusic) {
        this.bestNewMusic = bestNewMusic;
    }
    
    
}
