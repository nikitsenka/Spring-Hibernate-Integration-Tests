package session3.model;

import org.apache.log4j.Logger;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@NamedQueries({
    @NamedQuery(name="findByRealeaseDateRange", query="SELECT e FROM Track e WHERE e.releaseDate BETWEEN :startDate AND :endDate"),
        @NamedQuery(name="findByGenre", query="SELECT e FROM Track e WHERE e.genre LIKE :genre"),
            @NamedQuery(name="findByTitle", query="SELECT e FROM Track e WHERE e.title LIKE :title"),
                @NamedQuery(name="findByArtist", query="SELECT e FROM Track e WHERE e.artist LIKE :artist"),
                    @NamedQuery(name="findBySecondArtist", query="SELECT e FROM Track e WHERE e.secondArtist LIKE :secondArtist"),
                        @NamedQuery(name="findByLabel", query="SELECT e FROM Track e WHERE e.label LIKE :label")
})
public class Track implements Serializable, Comparable {

    private String id;
    private String title;
    private String artist;
    private String secondArtist = "";
    @XmlElement
    private Date releaseDate;
    @XmlElement
    private Date lastModifiedDate;
    private String pictureUrl;
    private String label;
    private String genre;
    private final String charFilter = "[^\\x00-\\x7F]";
    private static final long serialVersionUID = 42L;
    private static final Logger LOGGER = Logger.getLogger(Track.class);

    public Track() {
        this("default","default");
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getSecondArtist() {
        return secondArtist;
    }

    public void setSecondArtist(String secondArtist) {
        this.secondArtist = secondArtist;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Track(String title, String artist) {
        try {
            setTitle(title);
            setArtist(artist);
            setId(UUID.randomUUID().toString());
        } catch (IllegalArgumentException e) {
            LOGGER.info("Illegal Tracks arguments: " + title + "," + artist);
            setTitle("");
            setArtist("");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        if (!artist.equals(track.artist)) {
            return false;
        }
        return title.equals(track.title);

    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + artist.hashCode();
        return result;
    }

    public String getTitle() {
        return title;

    }

    public void setTitle(String title) {
        this.title = preprocessInput(title);
        if (this.title == null) {
            throw new IllegalArgumentException("Invalid Title name");
        }
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) throws IllegalArgumentException {
        this.artist = preprocessInput(artist);
        if (this.artist == null) {
            throw new IllegalArgumentException("Invalid Artist name");
        }
    }

    private String preprocessInput(String input) {
        String temp = new String(input);
        if (temp != null) {
            temp = temp.trim();
            if (temp.length() > 1) {
                temp = temp.toLowerCase().replaceAll(charFilter, "");
                return temp;
            }
        }
        return null;
    }


    public Date getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int compareTo(Object o) {
        return artist.compareTo(((Track) o).artist);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setReleaseDate(Date date) {
        this.releaseDate = date;
    }

}
