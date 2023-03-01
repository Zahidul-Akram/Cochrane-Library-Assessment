# Cochrane-Library-Assessment
Here I build a Java command line application to gather and store URLs and associated metadata from the Cochrane Library review collection (https://www.cochranelibrary.com/cdsr/reviews/topics).

The documentation of my project : 

 1.First of all I studied properly about HttpClient libraries and Apache . Then I implemented my code.

2.Firstly I import my important libraries and define my base url and important variables as private static final.

3.Then I create 3 methods getReviewUrls , getReviewData and writeToFile  in the public class.

4.The getReviewUrls fetches all the urls from the starting base url using HttpClient(request , response , entity) and add to a ArrayList.

5.Same as getReviewUrls the getReviewData fetch the target metadatas(title , author , date) from the urls of the list using  HttpClient(request , response , entity) and add to another Arraylist .

6.Finally writeToFile takes the metadatas from the Arraylist and create a new text file and write the datas with pipes “|” and newline .
