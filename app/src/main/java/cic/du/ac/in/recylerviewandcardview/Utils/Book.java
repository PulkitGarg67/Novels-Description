package cic.du.ac.in.recylerviewandcardview.Utils;

/**
 * Created by Scorpion on 10/25/2018.
 */

public class Book {

    private String Title;
    private String Description ;
    private String Thumbnail ;

    public Book() {
    }

    public Book(String title, String description, String thumbnail) {
        Title = title;
        Description = description;
        Thumbnail = thumbnail;
    }


    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getThumbnail() {
        return Thumbnail;
    }


    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }
}
