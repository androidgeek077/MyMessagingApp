package Models;

import java.io.Serializable;

public class Message implements Serializable {

    private String mID,mText,mFile,mFileType,mDate,mUserName,mUserProfileUrl,senderUid,receiverUid;
    private boolean seen;
    public Message(String mID, String mText, String mFile, String mFileType, String mDate,
                   String mUserName, String mUserProfileUrl, String senderUid, String receiverUid, boolean seen) {
        this.mID = mID;
        this.mText = mText;
        this.mFile = mFile;
        this.mFileType=mFileType;
        this.mDate = mDate;
        this.mUserName = mUserName;
        this.mUserProfileUrl = mUserProfileUrl;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.seen = seen;
    }

    public Message() {
    }



    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUserProfileUrl() {
        return mUserProfileUrl;
    }

    public void setmUserProfileUrl(String mUserProfileUrl) {
        this.mUserProfileUrl = mUserProfileUrl;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getmFile() {
        return mFile;
    }

    public void setmFile(String mFile) {
        this.mFile = mFile;
    }

    public String getmFileType() {
        return mFileType;
    }

    public void setmFileType(String mFileType) {
        this.mFileType = mFileType;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
