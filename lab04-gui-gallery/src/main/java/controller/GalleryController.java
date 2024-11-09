package controller;


import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.Gallery;
import model.Photo;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;
import util.PhotoDownloader;

public class GalleryController {
    @FXML
    private TextField imageNameField;
    @FXML
    private ImageView imageView;
    @FXML
    private ListView<Photo> imagesListView;
    @FXML
    private TextField searchTextField;
    private Gallery galleryModel;

    @FXML
    public void initialize() {
        // TODO additional FX controls initialization
        imagesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Photo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView photoIcon = new ImageView(item.getPhotoData());
                    photoIcon.setPreserveRatio(true);
                    photoIcon.setFitHeight(50);
                    setGraphic(photoIcon);
                }
            }
        });

        imagesListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (oldValue != null) {
                        this.imageNameField.textProperty().unbindBidirectional(oldValue.getNameProperty());
                    }

                    this.bindSelectedPhoto(newValue);
                });
    }

    public void setModel(Gallery gallery) {
        this.galleryModel = gallery;
        imagesListView.setItems(gallery.getPhotos());
        imagesListView.getSelectionModel().select(0);
    }

    private void bindSelectedPhoto(Photo selectedPhoto) {
        // TODO view <-> model bindings configuration
        if (selectedPhoto != null) {
            imageNameField.textProperty().bindBidirectional(selectedPhoto.getNameProperty());
            imageView.imageProperty().bind(selectedPhoto.getImageProperty());
        }
    }
    public void searchButtonClicked(ActionEvent event) {
        var photoDownloader = new PhotoDownloader();
        galleryModel.clear();

        imageNameField.textProperty().unbind();
        imageNameField.clear();
        imageView.imageProperty().unbind();
        imageView.setImage(null);

        photoDownloader.searchForPhotos(searchTextField.getText())
                .take(10)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(photo -> {
                    galleryModel.addPhoto(photo);

                    if (galleryModel.getPhotos().size() == 1) {
                        imagesListView.getSelectionModel().select(0);
                    }
                });


    }
}

