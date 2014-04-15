package session3.dao;

import org.hamcrest.Matchers;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import session3.model.Track;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

/**
 * Created with IntelliJ IDEA.
 * User: Ivan_Nikitsenka
 * Date: 10/31/13
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class TrackDaoHibernateImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private TrackDaoHibernateImpl trackDao;

    @Test
    public void save(){
        Track track = getDefaultTrack();
        trackDao.save(track);
        Track result = getEntity(track);
        assertTracksAreEqual(track, result);
    }
    @Test
    public void saveOrUpdate(){
        Track track = getDefaultTrack();
        trackDao.saveOrUpdate(track);
        Track result = getEntity(track);
        assertTracksAreEqual(track, result);
    }
    @Test
    public void update(){
        Track track = getDefaultTrack();
        trackDao.update(track);
        Track result = getEntity(track);
        assertTracksAreEqual(track, result);
    }
    @Test
    public void getByIdWhenEntityExist(){
        String id = "1";
        Track track = getDefaultTrack();
        track.setId(id);
        addEntity(track);
        Track result = (Track) trackDao.getById(id);
        assertTracksAreEqual(track, result);
    }
    @Test
    public void getByIdWhenEntityNotExist(){
        String id = "1";
        Track track = getDefaultTrack();
        track.setId(id);
        addEntity(track);
        Track result = (Track) trackDao.getById("2");
        assertThat(result, is(nullValue()));
    }
    @Test
    public void getAll() throws Exception {
        Track track1 = new Track();
        track1.setId("1");
        Track track2 = new Track();
        track2.setId("2");
        Track track3 = new Track();
        track3.setId("3");
        addEntity(track1);
        addEntity(track2);
        addEntity(track3);
        List<Track> tracks = trackDao.getAll();
        assertThat(tracks.size(),is(3));
        assertThat(tracks,hasItem(track1));
        assertThat(tracks,hasItem(track2));
        assertThat(tracks,hasItem(track3));
    }
    @Test
    public void getAllReturnsEmptyListWhenNoData(){
        List<Track> tracks = trackDao.getAll();
        assertThat(tracks, is(empty()));
    }
    @Test
    public void getTracksReturnsListWithItemsForTheReleaseDateInclusiveRange(){
        Track track1 = new Track();
        track1.setReleaseDate(parseDate("2013-12-10"));
        Track track2 = new Track();
        track2.setReleaseDate(parseDate("2013-12-11"));
        Track track3 = new Track();
        track3.setReleaseDate(parseDate("2013-12-13"));
        Track track4 = new Track();
        track4.setReleaseDate(parseDate("2013-12-14"));
        Track track5 = new Track();
        track5.setReleaseDate(parseDate("2013-12-15"));
        addEntity(track1);
        addEntity(track2);
        addEntity(track3);
        addEntity(track4);
        addEntity(track5);
        List<Track> tracks = trackDao.getTracks(parseDate("2013-12-11"),parseDate("2013-12-14"));
        assertThat(tracks.size(),is(3));
        assertThat(tracks,hasItem(track2));
        assertThat(tracks,hasItem(track3));
        assertThat(tracks,hasItem(track4));
    }
    @Test
    public void getTracksForTheSameFromToDate(){
        Date date = parseDate("2013-12-10");
        Track track = new Track();
        track.setReleaseDate(date);
        addEntity(track);
        List<Track> tracks = trackDao.getTracks(date,date);
        assertThat(tracks.size(),is(1));
        assertThat(tracks,hasItem(track));
    }
    @Test
    public void getTracksForTheNullFromDate(){
        Date date = parseDate("2013-12-10");
        Track track = new Track();
        track.setReleaseDate(date);
        addEntity(track);
        List<Track> tracks = trackDao.getTracks(null,date);
        assertThat(tracks.size(),is(1));
        assertThat(tracks,hasItem(track));
    }
    @Test
    public void getTracksForTheNullToDate(){
        Date date = parseDate("2013-12-10");
        Track track = new Track();
        track.setReleaseDate(date);
        addEntity(track);
        List<Track> tracks = trackDao.getTracks(date,null);
        assertThat(tracks.size(),is(1));
        assertThat(tracks,hasItem(track));
    }
    @Test
    public void getTracksReturnEmptyListForNullDates(){
        Track track = new Track();
        addEntity(track);
        List<Track> tracks = trackDao.getTracks(null,null);
        assertThat(tracks, is(empty()));
    }
    @Test
    public void getTracksByGenre(){
        String genre = "rock";
        Track track = new Track();
        track.setGenre(genre);
        addEntity(track);
        List<Track> tracks = trackDao.getTracksByGenre(genre);
        assertThat(tracks,hasItem(track));
    }
    @Test
    public void getTracksByLabel(){
        String label = "great records";
        Track track = new Track();
        track.setLabel(label);
        addEntity(track);
        List<Track> tracks = trackDao.getTracksByLabel(label);
        assertThat(tracks,hasItem(track));
    }
    @Test
    public void getTracksByArtist(){
        String artist = "artist name";
        Track track = new Track();
        track.setArtist(artist);
        addEntity(track);
        List<Track> tracks = trackDao.getTracksByArtist(artist);
        assertThat(tracks,hasItem(track));
    }
    @Test
    public void getTracksBySecondArtist(){
        String secondArtist = "second artist name";
        Track track = new Track();
        track.setSecondArtist(secondArtist);
        addEntity(track);
        List<Track> tracks = trackDao.getTracksBySecondArtist(secondArtist);
        assertThat(tracks,hasItem(track));
    }
    @Test
    public void getTracksByTitle(){
        String title = "title";
        Track track = new Track();
        track.setTitle(title);
        addEntity(track);
        List<Track> tracks = trackDao.getTracksByTitle(title);
        assertThat(tracks,hasItem(track));
    }
    @Test
    public void getTracksByKeyWordSearchItemsWithArtistOrSecondArtistOrTitleOrLabel(){
        String keyword = "keyword";
        Track trackWithTitle = new Track();
        trackWithTitle.setTitle(keyword);
        Track trackWithArtist = new Track();
        trackWithArtist.setArtist(keyword);
        Track trackWithSecondArtist = new Track();
        trackWithSecondArtist.setSecondArtist(keyword);
        Track trackWithLabel = new Track();
        trackWithLabel.setLabel(keyword);
        addEntity(trackWithArtist);
        addEntity(trackWithTitle);
        addEntity(trackWithSecondArtist);
        addEntity(trackWithLabel);
        List<Track> tracks = trackDao.getTracksByKeyWord(keyword);
        assertThat(tracks.size(),is(4));
        assertThat(tracks,hasItem(trackWithArtist));
        assertThat(tracks,hasItem(trackWithTitle));
        assertThat(tracks,hasItem(trackWithSecondArtist));
        assertThat(tracks,hasItem(trackWithLabel));
    }
    @Test
    public void getTracksByKeyWordReturnsEmptyListForNullInput(){
        List<Track> result = trackDao.getTracksByKeyWord(null);
        assertThat(result,is(empty()));
    }
    private void assertTracksAreEqual(Track expected, Track actual) {
        assertThat(actual.getId(),is(expected.getId()));
        assertThat(actual.getArtist(),is(expected.getArtist()));
        assertThat(actual.getSecondArtist(), is(expected.getSecondArtist()));
        assertThat(actual.getTitle(), is(expected.getTitle()));
        assertThat(actual.getGenre(), is(expected.getGenre()));
        assertThat(actual.getLabel(), is(expected.getLabel()));
        assertThat(actual.getLastModifiedDate().getTime(),is(expected.getLastModifiedDate().getTime()));
        assertThat(actual.getReleaseDate().getTime(),is(expected.getReleaseDate().getTime()));
        assertThat(actual.getPictureUrl(), is(expected.getPictureUrl()));
    }

    public void addEntity(Object obj){

        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(obj);

        session.flush();
        session.clear();
    }
    public Track getEntity(Track track){
        Track result = null;
        Session session = sessionFactory.getCurrentSession();
        result = (Track)session.get(Track.class, track.getId());
        session.flush();
        session.clear();
        return result;
    }
    public Track getDefaultTrack() {
        Date defaultDate = parseDate("2013-12-31");
        Track defaultTrack = new Track("Title", "Artist");
        defaultTrack.setSecondArtist("Second Artist");
        defaultTrack.setGenre("Genre");
        defaultTrack.setLabel("Label");
        defaultTrack.setLastModifiedDate(defaultDate);
        defaultTrack.setReleaseDate(defaultDate);
        defaultTrack.setPictureUrl("http://url.com/");
        return defaultTrack;
    }
    private Date parseDate(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return date;
    }
}

