package seedu.address.ui;

import java.io.File;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.commons.events.ui.PersonSelectedEvent;
import seedu.address.logic.commands.PhotoCommand;
import seedu.address.model.person.ReadOnlyPerson;

//@@author April0616

/**
 * A UI component that displays a person's data on the main panel
 */
public class PersonInfoPanel extends UiPart<Region> {

    private static final String FXML = "PersonInfoPanel.fxml";
    private static String DEFAULT_PHOTO_PATH = "/images/defaultPhoto.jpg";

    private ReadOnlyPerson person;

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private Circle photoCircle;
    @FXML
    private Label name;
    @FXML
    private Label gender;
    @FXML
    private Label matricNo;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label birthday;
    @FXML
    private Label remark;
    @FXML
    private FlowPane tags;


    public PersonInfoPanel() {
        super(FXML);
        this.person = null;
        loadDefaultPerson();

        registerAsAnEventHandler(this);
    }

    /**
     * Loads the default person when the app is first started
     */
    private void loadDefaultPerson() {
        name.setText("Person X");
        gender.setText("");
        matricNo.setText("");
        phone.setText("");
        address.setText("");
        email.setText("");
        birthday.setText("");
        remark.setText("");


        setDefaultContactPhoto();
    }

    /**
     * Updates info with person selected
     */
    private void loadPerson(ReadOnlyPerson person) {
        this.person = person;
        tags.getChildren().clear();
        initTags(person);

        name.textProperty().bind(Bindings.convert(person.nameProperty()));
        gender.textProperty().bind(Bindings.convert(person.genderProperty()));
        matricNo.textProperty().bind(Bindings.convert(person.matricNoProperty()));
        phone.textProperty().bind(Bindings.convert(person.phoneProperty()));
        address.textProperty().bind(Bindings.convert(person.addressProperty()));
        email.textProperty().bind(Bindings.convert(person.emailProperty()));
        birthday.textProperty().bind(Bindings.convert(person.birthdayProperty()));
        remark.textProperty().bind(Bindings.convert(person.remarkProperty()));
        person.tagProperty().addListener((observable, oldValue, newValue) -> {
            tags.getChildren().clear();
            initTags(person);
        });

        loadPhoto(person);
    }

    /**
     * Initializes the tags for person list
     * @param person
     */
    private void initTags(ReadOnlyPerson person) {
        person.getTags().forEach(tag -> {
                Label tagLabel = new Label(tag.tagName);
                tagLabel.setStyle("-fx-background-color: " + TagColorMap.getInstance().getTagColor(tag.tagName));
                tags.getChildren().add(tagLabel);
            }
        );
    }

    //@@author April0616
    /**
     * Set the default contact photo.
     */
    public void setDefaultContactPhoto() {
        Image defaultImage = new Image(MainApp.class.getResourceAsStream(DEFAULT_PHOTO_PATH));
        photoCircle.setFill(new ImagePattern(defaultImage));
    }

    /**
     * Load the photo of the specified person.
     * @param person
     */
    public void loadPhoto(ReadOnlyPerson person) {
        String prefix = "src/main/resources";
        //String photoPath = person.getPhotoPath().value.substring(prefix.length());
        String photoPath = person.getPhotoPath().value;
        Image image;

        if (photoPath.equals(PhotoCommand.DEFAULT_PHOTO_PATH)) {  //default male and female photos
            if (person.getGender().toString().equals("Male")) {
                photoPath = prefix + "/images/default_male.jpg";
            } else {
                photoPath = prefix + "/images/default_female.jpg";
            }
        }

        File contactImg = new File(photoPath);
        String url = contactImg.toURI().toString();
        image = new Image(url);
        photoCircle.setFill(new ImagePattern(image));

    }



    //@@author zacharytang
    @Subscribe
    private void handlePersonSelectedEvent(PersonSelectedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        //this.person = person;
        //initTags(person);
        loadPerson(event.person);
    }

    @Subscribe
    private void handlePersonPanelSelectionChangeEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPerson(event.getNewSelection().person);
    }
}