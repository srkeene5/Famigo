package com.famigo.website.utilities;

public enum InviteStatus {
    INVITED(0),
    ACCEPTED(1),
    REFUSED(2);

    private final int inviteStatus;

    private InviteStatus(int inviteStatus) {
        this.inviteStatus = inviteStatus;
    }

    public InviteStatus getInviteStatus(int i) {
        if (i == 0) {
            return INVITED;
        }
        else if (i == 1) {
            return ACCEPTED;
        }
        else {
            return REFUSED;
        }
    }

    public int getInviteStatusInt() {
        return inviteStatus;
    }
}
