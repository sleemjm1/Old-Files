package bit.weilytw1.googlemaps3;

/**
 * Created by weilytw1 on 22/05/2015.
 */
public class Pic
{
    String imageString;
    String comment;
    double latitude;
    double longitude;
    int rating;

    public Pic(String imageString, String comment, double latitude, double longitude, int rating)
    {
        this.imageString = imageString;
        this.comment = comment;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
    }

    public String getImageString() {return imageString;}

    public String getComment() {return comment;}

    public double getLatitude() {return latitude;}

    public double getLongitude() {return longitude;}

    public int getRating() {return rating;}
}
