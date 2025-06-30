package chatclient.listviewcells;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class MessageListCell extends ListCell<MessageModel> {
    private final GridPane rootNode       = new GridPane();
    private final ImageView avatarView = new ImageView();
    private final Label     authorLabel  = new Label();
    private final Label     textLabel   = new Label();
    private final Label     timestampLabel    = new Label();

    public MessageListCell(ListView<MessageModel> lv) {
        prefWidthProperty().bind(lv.widthProperty().subtract(4));
        this.setMaxWidth(Control.USE_PREF_SIZE);
        avatarView.setFitWidth(32);
        avatarView.setFitHeight(32);
        avatarView.setStyle("-fx-background-radius: 16;");

        authorLabel.getStyleClass().addAll("accent");
        textLabel.getStyleClass().add("message-text");
        textLabel.setWrapText(true);
        timestampLabel.getStyleClass().add("message-time");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox headerRow =  new HBox(8, this.authorLabel, spacer, this.timestampLabel);
        //avatar in col=0, row=0..1 (span 2 rows)
        rootNode.add(avatarView, 0, 0, 1, 2);
        //header in col=1, row=0
        rootNode.add(headerRow,    1, 0);
        //message body in col=1, row=1
        rootNode.add(textLabel,   1, 1);

        //column sizing: fixed for avatar, flexible for content
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setMinWidth(40);
        col0.setPrefWidth(40);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);

        rootNode.getColumnConstraints().addAll(col0, col1);

        rootNode.setHgap(10);
        rootNode.setVgap(5);
    }

    @Override
    protected void updateItem(MessageModel item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {

            this.textLabel.setText(item.getText());
            this.authorLabel.setText(item.getAuthor());
            this.timestampLabel.setText(item.getTimestamp());
            this.avatarView.setImage(item.getAvatar());
            setGraphic(rootNode);
        }
    }
}
