package app;

import driver.DuckDuckGoDriver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.GroupedObservable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.Photo;
import model.PhotoSize;
import util.PhotoDownloader;
import util.PhotoProcessor;
import util.PhotoSerializer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhotoCrawler {

    private static final Logger log = Logger.getLogger(PhotoCrawler.class.getName());

    private final PhotoDownloader photoDownloader;

    private final PhotoSerializer photoSerializer;

    private final PhotoProcessor photoProcessor;

    public PhotoCrawler() throws IOException {
        this.photoDownloader = new PhotoDownloader();
        this.photoSerializer = new PhotoSerializer("./photos");
        this.photoProcessor = new PhotoProcessor();
    }

    public void resetLibrary() throws IOException {
        photoSerializer.deleteLibraryContents();
    }

    public void downloadPhotoExamples() {
        try {
            photoDownloader.getPhotoExamples()
                    .compose(this::processPhotos)
                    .subscribe(photoSerializer::savePhoto);

        } catch (IOException e) {
            log.log(Level.SEVERE, "Downloading photo examples error", e);
        }
    }

    public void downloadPhotosForQuery(String query) {
        // TODO Implement me :(
        photoDownloader.searchForPhotos(query)
                .take(10)
                .compose(this::processPhotos)
                .subscribe(photoSerializer::savePhoto,
                        error -> System.out.println("error: " + error.getMessage()),
                        () -> System.out.println("Got all photos"));
    }

    public void downloadPhotosForMultipleQueries(List<String> queries) {
        // TODO Implement me :(
        photoDownloader.searchForPhotos(queries)
//                .take(50)
                .compose(this::processPhotos)
                .blockingSubscribe(photoSerializer::savePhoto,
                        error -> System.out.println("error: " + error.getMessage()),
                        () -> System.out.println("Got all photos"));
    }

//    public Observable<Photo> processPhotos(Observable<Photo> photoObservable) {
//        return photoObservable
//                .filter(this.photoProcessor::isPhotoValid)
//                .map(this.photoProcessor::convertToMiniature);
//
//   }
    public Observable<Photo> processPhotos(Observable<Photo> photoObservable) {
        return photoObservable
                .filter(this.photoProcessor::isPhotoValid)
                .groupBy(PhotoSize::resolve)
                .flatMap(this::processGroupedPhotos);
    }

    private Observable<Photo> processGroupedPhotos(GroupedObservable<PhotoSize, Photo> groupedObservable) {
        PhotoSize size = groupedObservable.getKey();

        switch (size) {
            case SMALL:
                return Observable.empty();
            case MEDIUM:
                return groupedObservable
                        .buffer(5, TimeUnit.SECONDS)
                        .flatMap(photos -> Observable.fromIterable(photos));
            case LARGE:
                return groupedObservable
                        .observeOn(Schedulers.computation())
                        .map(this.photoProcessor::convertToMiniature);
            default:
                return Observable.empty();
        }
    }
}
