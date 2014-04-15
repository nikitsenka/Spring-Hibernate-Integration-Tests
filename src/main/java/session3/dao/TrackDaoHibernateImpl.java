package session3.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import session3.model.Track;

import java.util.*;

@Repository
public class TrackDaoHibernateImpl extends GenericHibernateDao implements TrackDao {


    /**
     * Creates a new GenericHibernateDao object.
     */
    public TrackDaoHibernateImpl() {
        super(Track.class);
    }

    @Override
    public List<Track> getTracks(Date releaseDateFrom, Date releaseDateTo) {
        if (releaseDateFrom == null){
            releaseDateFrom = releaseDateTo;
        }
        if (releaseDateTo == null){
            releaseDateTo = releaseDateFrom;
        }
        Query query = getCurrentSession().getNamedQuery("findByRealeaseDateRange");
        query.setParameter("startDate",releaseDateFrom);
        query.setParameter("endDate",releaseDateTo);
        return query.list();  
    }

    @Override
    public List<Track> getTracksByGenre(String genre) {
        Query query = getCurrentSession().getNamedQuery("findByGenre");
        query.setParameter("genre","%"+genre+"%");
        return query.list();  
    }

    @Override
    public List<Track> getTracksByKeyWord(String keyWord) {
        List<Track> tracks = new ArrayList<Track>();
        tracks.addAll(getTracksByArtist(keyWord));
        tracks.addAll(getTracksBySecondArtist(keyWord));
        tracks.addAll(getTracksByLabel(keyWord));
        tracks.addAll(getTracksByTitle(keyWord));
        return tracks;
    }

    @Override
    public List<Track> getTracksByTitle(String title) {
        Query query = getCurrentSession().getNamedQuery("findByTitle");
        query.setParameter("title","%"+title+"%");
        return query.list();

    }

    @Override
    public List<Track> getTracksByArtist(String artist) {
        Query query = getCurrentSession().getNamedQuery("findByArtist");
        query.setParameter("artist","%"+artist+"%");
        return query.list();
    }
    public List<Track> getTracksByLabel(String label) {
        Query query = getCurrentSession().getNamedQuery("findByLabel");
        query.setParameter("label","%"+label+"%");
        return query.list();
    }
    public List<Track> getTracksBySecondArtist(String secondArtist) {
        Query query = getCurrentSession().getNamedQuery("findBySecondArtist");
        query.setParameter("secondArtist","%"+secondArtist+"%");
        return query.list();
    }
}
