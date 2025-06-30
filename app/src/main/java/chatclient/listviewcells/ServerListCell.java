package chatclient.listviewcells;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ServerListCell extends ListCell<ChatRoomModel> {
    private final Label roomLabel = new Label();
    private final Label ownerId = new Label();
    private final Label roomId = new Label();
    private final VBox vBox = new VBox(5);
    private final ListView<ChatRoomModel> serverList;

    public ServerListCell(ListView<ChatRoomModel> lv) {
        this.serverList = lv;
        HBox hb = new HBox( this.roomId, this.ownerId);
        hb.setSpacing(4);
        this.vBox.getChildren().addAll(hb, this.roomLabel);
        this.vBox.setPadding(new Insets(10, 10, 10, 10));
        this.vBox.setSpacing(4);
        prefWidthProperty().bind(this.serverList.widthProperty().subtract(14));
        setGraphic(this.vBox);
    }


    @Override
    protected void updateItem(ChatRoomModel item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            this.roomLabel.setText(item.getRoomName());
            this.roomId.setText(String.valueOf(item.getRoomId()));
            this.ownerId.setText(String.valueOf(item.getOwnerId()));

            setGraphic(vBox);
        }
    }
}
