// src/main/java/model/Reward.java
//POJO model!!
package model;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;
//import java.util.Optional; // To handle the optional 'specialReward' attribute

public class Reward {
    private ObjectId id; // MongoDB's unique identifier for documents
    private String playerName;
    private String escapeRoomName;
    private String certificateMessage;
    // specialReward is optional, so we'll use Optional<String> internally for clearer handling
//    private Optional<String> specialReward;
    private String specialRewardDetails;
    private LocalDateTime issueDate;

    // Default constructor is essential for MongoDB POJO mapping
    public Reward() {
        this.issueDate = LocalDateTime.now(); // Set issue date by default
        //this.specialReward = Optional.empty();
        this.specialRewardDetails = null;// Initialize optional field as empty
    }

    // Constructor for creating a new Reward without special message
    public Reward(String playerName, String escapeRoomName, String certificateMessage) {
        this(); // Call default constructor to set issueDate and initialize specialReward
        this.playerName = playerName;
        this.escapeRoomName = escapeRoomName;
        this.certificateMessage = certificateMessage;
    }

    // Constructor for creating a new Reward with a special message
//    public Reward(String playerName, String escapeRoomName, String certificateMessage, String specialReward) {
//        this(playerName, escapeRoomName, certificateMessage); // Call previous constructor
//        this.specialReward = Optional.ofNullable(specialReward); // Wrap in Optional
//    }
    public Reward(String playerName, String escapeRoomName, String certificateMessage, String specialRewardDetails) {
        this(playerName, escapeRoomName, certificateMessage);
        this.specialRewardDetails = specialRewardDetails; // Assign directly
    }


    // --- Getters and Setters ---
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

//    // Getter for Optional<String>
//    public Optional<String> getSpecialReward() {
//        return specialReward;
//    }
//
//    // Setter for Optional<String>
//    public void setSpecialReward(Optional<String> specialReward) {
//        this.specialReward = specialReward;
//    }
//    // Convenience setter for String (will wrap it in Optional)
//    public void setSpecialReward(String specialReward) {
//        this.specialReward = Optional.ofNullable(specialReward);
//    }
// Getter for String (will return null if not present in DB)
public String getSpecialRewardDetails() {
    return specialRewardDetails;
}

    // Setter for String
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

        // Check for null directly instead of Optional.isPresent()
        if (specialRewardDetails != null && !specialRewardDetails.isEmpty()) {
            sb.append(", Special Message='").append(specialRewardDetails).append('\'');
        }

        sb.append(", Issue Date=").append(issueDate)
                .append("}");
        return sb.toString();
    }
}

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Reward {")
//                .append("ID=").append(id)
//                .append(", Player='").append(playerName).append('\'')
//                .append(", Escape Room='").append(escapeRoomName).append('\'')
//                .append(", Message='").append(certificateMessage).append('\'');
//
//        specialReward.ifPresent(sr -> sb.append(", Special Message='").append(sr).append('\''));
//
//        sb.append(", Issue Date=").append(issueDate)
//                .append("}");
//        return sb.toString();
//    }
//}

