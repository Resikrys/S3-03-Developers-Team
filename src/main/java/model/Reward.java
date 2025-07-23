package model;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;

public class Reward {
    private ObjectId id;
    private String playerName;
    private String escapeRoomName;
    private String certificateMessage;
    private String specialRewardDetails;
    private LocalDateTime issueDate;

    public Reward() {
        this.issueDate = LocalDateTime.now();
        this.specialRewardDetails = null;
    }

    public Reward(String playerName, String escapeRoomName, String certificateMessage) {
        this();
        this.playerName = playerName;
        this.escapeRoomName = escapeRoomName;
        this.certificateMessage = certificateMessage;
    }

    public Reward(String playerName, String escapeRoomName, String certificateMessage, String specialRewardDetails) {
        this(playerName, escapeRoomName, certificateMessage);
        this.specialRewardDetails = specialRewardDetails;
    }


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getEscapeRoomName() {
        return escapeRoomName;
    }

    public void setEscapeRoomName(String escapeRoomName) {
        this.escapeRoomName = escapeRoomName;
    }

    public String getCertificateMessage() {
        return certificateMessage;
    }

    public void setCertificateMessage(String certificateMessage) {
        this.certificateMessage = certificateMessage;
    }

    public String getSpecialRewardDetails() {
        return specialRewardDetails;
    }

    public void setSpecialRewardDetails(String specialRewardDetails) {
        this.specialRewardDetails = specialRewardDetails;
    }


    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reward {")
                .append("ID=").append(id)
                .append(", Player='").append(playerName).append('\'')
                .append(", Escape Room='").append(escapeRoomName).append('\'')
                .append(", Message='").append(certificateMessage).append('\'');

        if (specialRewardDetails != null && !specialRewardDetails.isEmpty()) {
            sb.append(", Special Message='").append(specialRewardDetails).append('\'');
        }

        sb.append(", Issue Date=").append(issueDate)
                .append("}");
        return sb.toString();
    }
}