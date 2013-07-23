
package com.comtop.fwms.starteam.server;

import java.io.IOException;
import java.util.Hashtable;

import com.starbase.starteam.Folder;
import com.starbase.starteam.Item;
import com.starbase.starteam.Status;
import com.starbase.starteam.View;
import com.starbase.starteam.ViewConfiguration;

public class StarTeamCheckout extends TreeBasedTask {
    
    private boolean createDirs;
    
    private boolean deleteUncontrolled;
    
    private boolean convertEOL;
    
    private int lockStatus;
    
    private boolean useRepositoryTimeStamp;
    
    public StarTeamCheckout() {
        this.createDirs = true;
        
        this.deleteUncontrolled = true;
        
        this.convertEOL = true;
        
        this.lockStatus = 3;
        
        this.useRepositoryTimeStamp = false;
    }
    
    public void setCreateWorkingDirs(boolean value) {
        this.createDirs = value;
    }
    
    public void setDeleteUncontrolled(boolean value) {
        this.deleteUncontrolled = value;
    }
    
    public void setConvertEOL(boolean value) {
        this.convertEOL = value;
    }
    
    public void setLabel(String label) {
        _setLabel(label);
    }
    
    public void setLocked(boolean v) throws BuildException {
        setLockStatus(v, 1);
    }
    
    public void setUnlocked(boolean v) throws BuildException {
        setLockStatus(v, 0);
    }
    
    private void setLockStatus(boolean v, int newStatus) throws BuildException {
        if (v) {
            if (this.lockStatus == 3) {
                this.lockStatus = newStatus;
            } else if (this.lockStatus != newStatus) {
                throw new BuildException("Error: cannot set locked and unlocked both true.");
            }
        }
    }
    
    public void setUseRepositoryTimeStamp(boolean useRepositoryTimeStamp) {
        this.useRepositoryTimeStamp = useRepositoryTimeStamp;
    }
    
    public boolean getUseRepositoryTimeStamp() {
        return this.useRepositoryTimeStamp;
    }
    
    public void setAsOfDate(String asOfDateParam) {
        _setAsOfDate(asOfDateParam);
    }
    
    public void setAsOfDateFormat(String asOfDateFormat) {
        _setAsOfDateFormat(asOfDateFormat);
    }
    
    protected View createSnapshotView(View raw) throws BuildException {
        int labelID = getLabelID(raw);
        
        if (isUsingViewLabel()) {
            return new View(raw, ViewConfiguration.createFromLabel(labelID));
        }
        
        if (isUsingRevisionLabel()) {
            return raw;
        }
        
        View view = getViewConfiguredByDate(raw);
        if (view != null) {
            return view;
        }
        
        return new View(raw, ViewConfiguration.createTip());
    }
    
    protected void testPreconditions() throws BuildException {
        if ((isUsingRevisionLabel()) && (this.createDirs)) {
            log("Ignoring createworkingdirs while using a revision label.  Folders will be created only as needed.", 1);
            
            this.createDirs = false;
        }
        if (this.lockStatus != 3) {
            boolean lockStatusBad = false;
            if (null != getLabel()) {
                log("Neither locked nor unlocked may be true when checking out a labeled version.", 0);
                
                lockStatusBad = true;
            } else if (null != getAsOfDate()) {
                log("Neither locked nor unlocked may be true when checking out by date.", 0);
                
                lockStatusBad = true;
            }
            if (lockStatusBad) {
                throw new BuildException("Lock status may not be changed when checking out a non-current version.");
            }
            
        }
        
        if ((null != getLabel()) && (null != getAsOfDate())) {
            throw new BuildException("Both label and asOfDate specified.  Unable to process request.");
        }
    }
    
    protected void logOperationDescription(Folder starteamrootFolder, java.io.File targetrootFolder) {
        log((isRecursive() ? "Recursive" : "Non-recursive") + " Checkout from: " + starteamrootFolder.getFolderHierarchy());
        
        log("  Checking out to" + (null == getRootLocalFolder() ? "(default): " : ": ") + targetrootFolder.getAbsolutePath());
        
        logLabel();
        logAsOfDate();
        logIncludes();
        logExcludes();
        
        if (this.lockStatus == 1) {
            log("  Items will be checked out with Exclusive locks.");
        } else if (this.lockStatus == 0) {
            log("  Items will be checked out unlocked (even if presently locked).");
        } else {
            log("  Items will be checked out with no change in lock status.");
        }
        log("  Items will be checked out with " + (this.useRepositoryTimeStamp ? "repository timestamps." : "the current timestamp."));
        
        log("  Items will be checked out " + (isForced() ? "regardless of" : "in accordance with") + " repository status.");
        
        if (this.deleteUncontrolled) {
            log("  Local items not found in the repository will be deleted.");
        }
        log("  Items will be checked out "
            + (this.convertEOL ? "using the local machine's EOL convention" : "without changing the EOL convention used on the server"));
        
        log("  Directories will be created"
            + (this.createDirs ? " wherever they exist in the repository, even if empty." : " only where needed to check out files."));
    }
    
    protected void visit(Folder starteamFolder, java.io.File targetFolder) throws BuildException {
        try {
            if (null != getRootLocalFolder()) {
                starteamFolder.setAlternatePathFragment(targetFolder.getAbsolutePath());
            }
            
            if ((!targetFolder.exists()) && (!isUsingRevisionLabel()) && (this.createDirs)) {
                if (targetFolder.mkdirs()) {
                    log("Creating folder: " + targetFolder);
                } else {
                    throw new BuildException("Failed to create local folder " + targetFolder);
                }
                
            }
            
            Folder[] foldersList = starteamFolder.getSubFolders();
            getTypeNames().getClass();
            Item[] filesList = starteamFolder.getItems("File");
            
            if (isUsingRevisionLabel()) {
                Hashtable labelItems = new Hashtable(filesList.length);
                int s = filesList.length;
                int[] ids = new int[s];
                for (int i = 0; i < s; i++) {
                    ids[i] = filesList[i].getItemID();
                    labelItems.put(new Integer(ids[i]), new Integer(i));
                }
                int[] foundIds = getLabelInUse().getLabeledItemIDs(ids);
                s = foundIds.length;
                Item[] labeledFiles = new Item[s];
                for (int i = 0; i < s; i++) {
                    Integer id = new Integer(foundIds[i]);
                    labeledFiles[i] = filesList[((Integer) labelItems.get(id)).intValue()];
                }
                
                filesList = labeledFiles;
            }
            
            TreeBasedTask.UnmatchedFileMap ufm = new StarTeamCheckout.CheckoutMap(this, null).init(targetFolder.getAbsoluteFile(), starteamFolder);
            
            for (int i = 0; i < foldersList.length; i++) {
                Folder stFolder = foldersList[i];
                
                java.io.File subfolder = new java.io.File(targetFolder, stFolder.getName());
                
                ufm.removeControlledItem(subfolder);
                
                if (isRecursive()) {
                    visit(stFolder, subfolder);
                }
            }
            
            for (int i = 0; i < filesList.length; i++) {
                com.starbase.starteam.File stFile = (com.starbase.starteam.File) filesList[i];
                
                processFile(stFile, targetFolder);
                
                ufm.removeControlledItem(new java.io.File(targetFolder, stFile.getName()));
            }
            
            if (this.deleteUncontrolled) {
                ufm.processUncontrolledItems();
            }
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }
    
    private String describeCheckout(com.starbase.starteam.File remotefile, java.io.File localFile) {
        StringBuffer sb = new StringBuffer();
        sb.append(getFullRepositoryPath(remotefile)).append(" --> ");
        
        if (null == localFile) {
            sb.append(remotefile.getFullName());
        } else {
            sb.append(localFile);
        }
        return sb.toString();
    }
    
    private String describeCheckout(com.starbase.starteam.File remotefile) {
        return describeCheckout(remotefile, null);
    }
    
    private void processFile(com.starbase.starteam.File eachFile, java.io.File targetFolder) throws IOException {
        String filename = eachFile.getName();
        
        java.io.File localFile = new java.io.File(targetFolder, filename);
        
        if (!shouldProcess(filename)) {
            log("Excluding " + getFullRepositoryPath(eachFile), 2);
            
            return;
        }
        
        if (isUsingRevisionLabel()) {
            if (!targetFolder.exists()) {
                if (targetFolder.mkdirs()) {
                    log("Creating folder: " + targetFolder);
                } else {
                    throw new BuildException("Failed to create local folder " + targetFolder);
                }
            }
            
            boolean success = eachFile.checkoutByLabelID(localFile, getIDofLabelInUse(), this.lockStatus, !this.useRepositoryTimeStamp, true, false);
            
            if (success) {
                log("Checked out " + describeCheckout(eachFile, localFile));
            }
        } else {
            boolean checkout = true;
            
            int fileStatus = eachFile.getStatus();
            
            if ((fileStatus == 1) || (fileStatus == 6)) {
                eachFile.updateStatus(true, true);
                fileStatus = eachFile.getStatus();
            }
            
            log(eachFile.toString() + " has status of " + Status.name(fileStatus), 4);
            
            switch (fileStatus) {
                case 2:
                case 5:
                    log("Checking out: " + describeCheckout(eachFile));
                    break;
                default:
                    if ((isForced()) && (fileStatus != 0)) {
                        log("Forced checkout of " + describeCheckout(eachFile) + " over status " + Status.name(fileStatus));
                    } else {
                        log("Skipping: " + getFullRepositoryPath(eachFile) + " - status: " + Status.name(fileStatus));
                        
                        checkout = false;
                    }
                    break;
            }
            if (checkout) {
                if (!targetFolder.exists()) {
                    if (targetFolder.mkdirs()) {
                        log("Creating folder: " + targetFolder);
                    } else {
                        throw new BuildException("Failed to create local folder " + targetFolder);
                    }
                }
                
                eachFile.checkout(this.lockStatus, !this.useRepositoryTimeStamp, this.convertEOL, false);
            }
        }
    }
}
