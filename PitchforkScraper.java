package pitchforkscraper;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.jsoup.HttpStatusException;

// A program to scrape Pitchfork.com and populate a text file with review information

class ReviewScraper extends Thread{
    
    private Element reviewToScrape;
    private String threadName;
    private Review review;
    private String reviewLink;
    
    // Receives the portion of the website to scrape,
    // a name for the thread
    // and a  review model object to fill out 
    ReviewScraper(Element reviewElement, String name, Review givenReview){
        reviewToScrape = reviewElement;
        threadName = name;
        review = givenReview;
    }
   
    public void run(){
        
          // Sets the name of the reviewer
          // .substring removes " by " from the string
          review.setReviewReviewer(reviewToScrape.select("div.review__meta").select("ul.authors").text().substring(4));
          // Sets the posted date of the review
          // .substring gets only the numerical date, the month and the year
          review.setReviewDate(reviewToScrape.select("time.pub-date").attr("title").substring(5,16));
          // Sets the genre of the review, not every review has a genre.
          review.setReviewGenre(reviewToScrape.select("a.genre-list__link").text());

        // The link to the review's individual webpage
        reviewLink = reviewToScrape.select("a.review__link").attr("href");
        try{
            
            Document reviewWebpage = Jsoup.connect("https://pitchfork.com" + reviewLink).get();
            Elements WebpageAlbums = reviewWebpage.select("h1.single-album-tombstone__review-title");
            Elements WebpageArtists = reviewWebpage.select("h2");
            Elements WebpageScores = reviewWebpage.select("span.score");
            
            // Some reviews are tagged as "best new music"
            // If the review is tagged, set the review object as such
            Element bestNewMusic = reviewWebpage.select("p.bnm-txt").first();
            if (bestNewMusic != null){
                review.setBestNewMusic(true);
            }
            
            // Some review pages have multiple reviews on them
            // So an array is needed to collect them
            String[] albumsArray = new String[WebpageAlbums.size()];
            String[] artistArray = new String[WebpageArtists.size()];
            String[] scoreArray = new String[WebpageScores.size()];
            
            
            for (int i = 0; i < WebpageAlbums.size(); i++){
                albumsArray[i] = WebpageAlbums.get(i).select("h1.single-album-tombstone__review-title").text(); 
                artistArray[i] = WebpageArtists.get(i).select("h2").text();
                scoreArray[i] = WebpageScores.get(i).select("span.score").text();
            }
            
            // Sets the review album name, artist name and given score
            review.setReviewAlbum(albumsArray);            
            review.setReviewArtist(artistArray);
            review.setReviewScore(scoreArray);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }   
     }
}

public class PitchforkScraper {

    public static void main(String[] args) throws IOException   {

          // The page number to start at
          int pageNumber = 1;
          
          // The file to write to, if it doesn't exist it gets created
          File file = new File("PitchforkScraper-Output.txt");
          if (!file.exists()){
              file.createNewFile();
          }

         // Keeps cycling through reviews untill a 404 error is encountered
         // Indicating that there are no more reviews left
         while(true){
             
            BufferedWriter bw = null;   
            
            try{
                
                Document webpage = Jsoup.connect("https://pitchfork.com/reviews/albums/?page=" + pageNumber).get();
                
                // Gets the number of reviews on the page (typically 12)
                Elements reviews = webpage.select("div.review");
                Review[] arrReview = new Review[reviews.size()];
                ReviewScraper[] arrReviewScraper = new ReviewScraper[reviews.size()];
            
                // Assigns each review a review model Object
                for(int i = 0; i < reviews.size(); i++){
                    Review review = new Review(reviews.get(i));
                    arrReview[i] = review;
                }
                // Assigns each review a thread to fill out its information
                for(int i = 0; i < reviews.size(); i++){
                    ReviewScraper reviewScraper = new ReviewScraper(reviews.get(i), "Page " + pageNumber + " Review " + i, arrReview[i]);
                    arrReviewScraper[i] = reviewScraper;
                    reviewScraper.start();
                }
                // Checks that every thread is finished before continuing
                for(ReviewScraper rs : arrReviewScraper){
                    rs.join();
                }
                // Print all of the review information to file and to the console
                for (Review r: arrReview){
                    for (int i = 0; i < r.getReviewArtist().length; i++){
                        System.out.println(r.getReviewArtist()[i] + "|" 
                                + r.getReviewAlbum()[i] + "|"
                                + r.getReviewGenre()    + "|"
                                + r.getReviewScore()[i] + "|"
                                + r.getReviewReviewer() + "|"
                                + r.getReviewDate() + "|" 
                                + r.getBestNewMusic());
                        
                        try{
                            
                            bw = new BufferedWriter(new FileWriter(file, true)); 
                            
                            bw.append(r.getReviewArtist()[i] + "|" 
                            + r.getReviewAlbum()[i] + "|" 
                            + r.getReviewGenre()    + "|"
                            + r.getReviewScore()[i] + "|"
                            + r.getReviewReviewer() + "|"
                            + r.getReviewDate());
                            
                            if(r.getBestNewMusic()){
                                bw.append("|BNM");
                            }
                            else{
                                bw.append("|Default");
                            }
                            
                            
                            bw.newLine();
                        }
                        catch (IOException ioe){
                            ioe.printStackTrace();
                            System.out.println("Last Page " + pageNumber);
                        }
                        finally{
                            try{
                                if(bw!=null){
                                    bw.close();
                                }
                            }
                            catch(Exception ex){
                                System.out.println(ex);
                                System.out.println("Last Page " + pageNumber);
                            }
                        }
                    }
                }
            }
            
            // If a problem is encountered, try again unless the problem was a 404 error
            catch(Exception e){

                if(((HttpStatusException)e).getStatusCode() == 404){
                    System.out.println("404 Encountered, now exiting");
                    break;
                }
                
                System.out.println("Retrying " + e.getMessage());
                System.out.println("Last Page " + pageNumber);
                continue;
            }
            pageNumber ++;
            
            // Rest for 4 seconds as a general courtesy.
            try {
            Thread.sleep(4000);
            } 
            catch (InterruptedException ie)
            {
            System.out.println(ie);
            }
         }            
    }
    
}
