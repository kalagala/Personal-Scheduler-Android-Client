package com.kalagala.personalschechuler.model;

public class ValidationResponse {
    private boolean isValid;
    private int dialogTitleResourceId;
    private int dialogMessageResourceId;
    private boolean hasStringParameterVariable;
    private String stringForPlaceHolder;

    public ValidationResponse(
            boolean isValid,
            int dialogTitleResourceId,
            int dialogMessageResourceId,
            String stringForPlaceHolder) {
        this.isValid = isValid;
        this.dialogTitleResourceId = dialogTitleResourceId;
        this.dialogMessageResourceId = dialogMessageResourceId;
        this.hasStringParameterVariable = true;
        this.stringForPlaceHolder = stringForPlaceHolder;
    }

    public ValidationResponse(boolean isValid){
        this.isValid = isValid;
        this.dialogMessageResourceId = 0;
        this.dialogMessageResourceId = 0;
        this.hasStringParameterVariable = false;
        this.stringForPlaceHolder = null;

    }

    public ValidationResponse(boolean isValid,
                              int dialogTitleResourceId,
                              int dialogMessageResourceId) {
        this.isValid = isValid;
        this.dialogTitleResourceId = dialogTitleResourceId;
        this.dialogMessageResourceId = dialogMessageResourceId;
        this.hasStringParameterVariable = false;
        this.stringForPlaceHolder = null;
    }

    public ValidationResponse(ValidationResponse validationResponse) {
        this.isValid = validationResponse.isValid;
        this.dialogTitleResourceId = validationResponse.dialogTitleResourceId;
        this.dialogMessageResourceId = validationResponse.dialogMessageResourceId;
        this.hasStringParameterVariable = validationResponse.hasStringParameterVariable;
        this.stringForPlaceHolder = validationResponse.stringForPlaceHolder;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public int getDialogTitleResourceId() {
        return dialogTitleResourceId;
    }

    public void setDialogTitleResourceId(int dialogTitleResourceId) {
        this.dialogTitleResourceId = dialogTitleResourceId;
    }

    public int getDialogMessageResourceId() {
        return dialogMessageResourceId;
    }

    public void setDialogMessageResourceId(int dialogMessageResourceId) {
        this.dialogMessageResourceId = dialogMessageResourceId;
    }

    public boolean isHasStringParameterVariable() {
        return hasStringParameterVariable;
    }

    public void setHasStringParameterVariable(boolean hasStringParameterVariable) {
        this.hasStringParameterVariable = hasStringParameterVariable;
    }

    public String getStringForPlaceHolder() {
        return stringForPlaceHolder;
    }

    public void setStringForPlaceHolder(String stringForPlaceHolder) {
        this.stringForPlaceHolder = stringForPlaceHolder;
    }
}
