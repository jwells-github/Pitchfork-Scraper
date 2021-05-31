# Pitchfork-Scraper
A program written in Java that scrapes the music review information from [Pitchfork](https://pitchfork.com/) and outputs a text file containing information about each of their reviews

This information includes 

- Artist name
- Album name
- Music genre
- Review score
- Reviewer name
- Publishing date of the review

## Library used

### Jsoup

Jsoup allows for the parsing of webpages in java. Once a webpage is obtained its Html & CSS can be filtered to find the desired element of a given webpage. 

More information about this library can be found on the website https://jsoup.org/



## Using the Results

Using the results from this script I quickly mocked up some charts using [Tableau](https://www.tableau.com/en-gb) to show the average rating across musical genres, the distribution of the 'best new music' award across genres, and also display the most reviewed artists.

https://public.tableau.com/profile/jake5683#!/vizhome/PitchorkData/Dashboard2?publish=yes

![Tableau Image showing usage of Pitchfork data](https://i.imgur.com/krChZYa.png)


