package session3.dao;

import session3.model.Track;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * User: ivan_nikitsenka
 * Date: 9/20/13
 * Time: 12:14 PM
 */

public interface TrackDao {
    /**
     * Creates a new GenericHibernateDao object.
     */

    public abstract List<Track> getTracks(Date releaseDateFrom, Date releaseDateTo);

    public abstract List<Track> getTracksByGenre(String genre);

    public abstract List<Track> getTracksByKeyWord(String keyWord);

    public abstract List<Track> getTracksByTitle(String title);

    public abstract List<Track> getTracksByArtist(String artist);
}
