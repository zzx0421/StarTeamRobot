
package com.comtop.fwms.starteam.server;

import java.util.StringTokenizer;

import com.starbase.starteam.Folder;
import com.starbase.starteam.Item;
import com.starbase.starteam.Project;
import com.starbase.starteam.Property;
import com.starbase.starteam.Server;
import com.starbase.starteam.StarTeamFinder;
import com.starbase.starteam.Task;
import com.starbase.starteam.Type;
import com.starbase.starteam.View;
import com.starbase.util.Platform;

public class AntStarTeamCheckOut extends Task {
    
    public static final String DEFAULT_INCLUDESETTING = "*";
    
    public static final String DEFAULT_EXCLUDESETTING = null;
    
    public static final String DEFAULT_FOLDERSETTING = null;
    
    private Folder prevFolder = null;
    
    private int checkedOut = 0;
    
    private String serverName = null;
    
    private int serverPort = -1;
    
    private String projectName = null;
    
    private String folderName = DEFAULT_FOLDERSETTING;
    
    private String viewName = null;
    
    private String username = null;
    
    private String password = null;
    
    private String targetFolder = null;
    
    private boolean force = false;
    
    private boolean verbose = false;
    
    private boolean recursion = true;
    
    private String includes = "*";
    
    private String excludes = DEFAULT_EXCLUDESETTING;
    
    private String delim = Platform.getFilePathDelim();
    
    private boolean targetFolderAbsolute = false;
    
    private static void assertTrue(boolean value, String msg) throws BuildException {
        if (!value) {
            throw new BuildException(msg);
        }
    }
    
    protected void checkParameters() throws BuildException {
        assertTrue(getServerName() != null, "ServerName must be set.");
        assertTrue(getServerPort() != -1, "ServerPort must be set.");
        assertTrue(getProjectName() != null, "ProjectName must be set.");
        assertTrue(getViewName() != null, "ViewName must be set.");
        assertTrue(getUsername() != null, "Username must be set.");
        assertTrue(getPassword() != null, "Password must be set.");
        assertTrue(getTargetFolder() != null, "TargetFolder must be set.");
        
        if (((getTargetFolder().endsWith("/")) || (getTargetFolder().endsWith("\\"))) && (getTargetFolder().length() > 1)) {
            setTargetFolder(getTargetFolder().substring(0, getTargetFolder().length() - 1));
        }
        
        java.io.File dirExist = new java.io.File(getTargetFolder());
        
        if ((dirExist.isDirectory()) && (!getForce())) {
            throw new BuildException("Target directory exists. Set \"force\" to \"true\" to continue anyway.");
        }
    }
    
    public void execute() throws BuildException {
        log("DEPRECATED - The starteam task is deprecated.  Use stcheckout instead.", 1);
        
        Server s = getServer();
        try {
            runServer(s);
        } finally {
            s.disconnect();
        }
        
        log(this.checkedOut + " files checked out.");
    }
    
    protected Server getServer() {
        Server s = new Server(getServerName(), getServerPort());
        
        s.connect();
        
        s.logOn(getUsername(), getPassword());
        
        return s;
    }
    
    protected void runServer(Server s) {
        Project[] projects = s.getProjects();
        
        for (int i = 0; i < projects.length; i++) {
            Project p = projects[i];
            
            if (p.getName().equals(getProjectName())) {
                if (getVerbose()) {
                    log("Found " + getProjectName() + this.delim);
                }
                runProject(s, p);
                break;
            }
        }
    }
    
    protected void runProject(Server s, Project p) {
        View[] views = p.getViews();
        
        for (int i = 0; i < views.length; i++) {
            View v = views[i];
            
            if (v.getName().equals(getViewName())) {
                if (getVerbose()) {
                    log("Found " + getProjectName() + this.delim + getViewName() + this.delim);
                }
                s.getTypeNames().getClass();
                runType(s, p, v, s.typeForName("File"));
                break;
            }
        }
    }
    
    protected void runType(Server s, Project p, View v, Type t) {
        Folder f = v.getRootFolder();
        
        if (getFolderName() != null) {
            if ((getFolderName().equals("\\")) || (getFolderName().equals("/"))) {
                setFolderName(null);
            } else {
                f = StarTeamFinder.findFolder(v.getRootFolder(), getFolderName());
                assertTrue(null != f, "ERROR: " + getProjectName() + this.delim + getViewName() + this.delim + v.getRootFolder() + this.delim
                    + getFolderName() + this.delim + " does not exist.");
            }
            
        }
        
        if ((getVerbose()) && (getFolderName() != null)) {
            log("Found " + getProjectName() + this.delim + getViewName() + this.delim + getFolderName() + this.delim + "\n");
        }
        
        int nProperties = 2;
        
        Property p1 = getPrimaryDescriptor(t);
        
        Property p2 = getSecondaryDescriptor(t);
        
        if (p2 != null) {
            nProperties++;
        }
        
        String[] strNames = new String[nProperties];
        int iProperty = 0;
        
        s.getPropertyNames().getClass();
        strNames[(iProperty++)] = "ID";
        strNames[(iProperty++)] = p1.getName();
        if (p2 != null) {
            strNames[(iProperty++)] = p2.getName();
        }
        
        f.populateNow(t.getName(), strNames, -1);
        
        runFolder(s, p, v, t, f, calcTargetFolder(v, f));
        
        f.discardItems(t.getName(), -1);
    }
    
    private java.io.File calcTargetFolder(View v, Folder rootSourceFolder) {
        java.io.File root = new java.io.File(getTargetFolder());
        
        if (!getTargetFolderAbsolute()) {
            String defaultPath = v.getDefaultPath();
            
            defaultPath = defaultPath.replace('/', java.io.File.separatorChar);
            defaultPath = defaultPath.replace('\\', java.io.File.separatorChar);
            
            java.io.File dir = new java.io.File(defaultPath);
            String dirName = dir.getName();
            
            if (dirName.endsWith(this.delim)) {
                dirName = dirName.substring(0, dirName.length() - 1);
            }
            
            StringTokenizer pathTokenizer = new StringTokenizer(rootSourceFolder.getFolderHierarchy(), this.delim);
            
            String currentToken = null;
            boolean foundRoot = false;
            
            while (pathTokenizer.hasMoreTokens()) {
                currentToken = pathTokenizer.nextToken();
                if ((currentToken.equals(getProjectName())) && (!foundRoot)) {
                    currentToken = dirName;
                    foundRoot = true;
                }
                root = new java.io.File(root, currentToken);
            }
        }
        
        return root;
    }
    
    protected void runFolder(Server s, Project p, View v, Type t, Folder f, java.io.File tgt) {
        Item[] items = f.getItems(t.getName());
        
        for (int i = 0; i < items.length; i++) {
            runItem(s, p, v, t, f, items[i], tgt);
        }
        
        if (getRecursion()) {
            Folder[] subfolders = f.getSubFolders();
            
            for (int i = 0; i < subfolders.length; i++) {
                runFolder(s, p, v, t, subfolders[i], new java.io.File(tgt, subfolders[i].getName()));
            }
        }
    }
    
    protected void runItem(Server s, Project p, View v, Type t, Folder f, Item item, java.io.File tgt) {
        Property p1 = getPrimaryDescriptor(t);
        Property p2 = getSecondaryDescriptor(t);
        
        String pName = (String) item.get(p1.getName());
        
        if (!shouldCheckout(pName)) {
            return;
        }
        
        if (getVerbose()) {
            boolean bShowHeader = f != this.prevFolder;
            
            if (bShowHeader) {
                String strFolder = f.getFolderHierarchy();
                int i = strFolder.indexOf(this.delim);
                
                if (i >= 0) {
                    strFolder = strFolder.substring(i + 1);
                }
                log("            Folder: \"" + strFolder + "\"");
                this.prevFolder = f;
                
                StringBuffer header = new StringBuffer("                Item");
                
                header.append(",\t").append(p1.getDisplayName());
                if (p2 != null) {
                    header.append(",\t").append(p2.getDisplayName());
                }
                log(header.toString());
            }
            
            StringBuffer itemLine = new StringBuffer("                ");
            
            itemLine.append(item.getItemID());
            
            itemLine.append(",\t").append(formatForDisplay(p1, item.get(p1.getName())));
            
            if (p2 != null) {
                itemLine.append(",\t").append(formatForDisplay(p2, item.get(p2.getName())));
            }
            
            int locker = item.getLocker();
            
            if (locker > -1) {
                itemLine.append(",\tLocked by ").append(locker);
            } else {
                itemLine.append(",\tNot locked");
            }
            log(itemLine.toString());
        }
        
        com.starbase.starteam.File remote = (com.starbase.starteam.File) item;
        
        java.io.File local = new java.io.File(tgt, (String) item.get(p1.getName()));
        try {
            remote.checkoutTo(local, 3, false, true, true);
            this.checkedOut += 1;
        } catch (Exception e) {
            throw new BuildException("Failed to checkout '" + local + "'", e);
        }
    }
    
    protected boolean shouldCheckout(String pName) {
        boolean includeIt = matchPatterns(getIncludes(), pName);
        boolean excludeIt = matchPatterns(getExcludes(), pName);
        
        return (includeIt) && (!excludeIt);
    }
    
    protected boolean matchPatterns(String patterns, String pName) {
        if (patterns == null) {
            return false;
        }
        StringTokenizer exStr = new StringTokenizer(patterns, " ");
        
        while (exStr.hasMoreTokens()) {
            if (DirectoryScanner.match(exStr.nextToken(), pName)) {
                return true;
            }
        }
        return false;
    }
    
    protected Property getPrimaryDescriptor(Type t) {
        Property[] properties = t.getProperties();
        
        for (int i = 0; i < properties.length; i++) {
            Property p = properties[i];
            
            if (p.isPrimaryDescriptor()) {
                return p;
            }
        }
        return null;
    }
    
    protected Property getSecondaryDescriptor(Type t) {
        Property[] properties = t.getProperties();
        
        for (int i = 0; i < properties.length; i++) {
            Property p = properties[i];
            
            if ((p.isDescriptor()) && (!p.isPrimaryDescriptor())) {
                return p;
            }
        }
        return null;
    }
    
    protected String formatForDisplay(Property p, Object value) {
        if (p.getTypeCode() == 8) {
            String str = value.toString();
            
            if (str.length() > 35) {
                str = str.substring(0, 32) + "...";
            }
            return "\"" + str + "\"";
        }
        if (p.getTypeCode() == 2) {
            return "\"" + p.getEnumDisplayName(((Integer) value).intValue()) + "\"";
        }
        return value.toString();
    }
    
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    
    public String getServerName() {
        return this.serverName;
    }
    
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
    
    public int getServerPort() {
        return this.serverPort;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getProjectName() {
        return this.projectName;
    }
    
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
    
    public String getViewName() {
        return this.viewName;
    }
    
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
    
    public String getFolderName() {
        return this.folderName;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }
    
    public String getTargetFolder() {
        return this.targetFolder;
    }
    
    public void setForce(boolean force) {
        this.force = force;
    }
    
    public boolean getForce() {
        return this.force;
    }
    
    public void setRecursion(boolean recursion) {
        this.recursion = recursion;
    }
    
    public boolean getRecursion() {
        return this.recursion;
    }
    
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    
    public boolean getVerbose() {
        return this.verbose;
    }
    
    public void setIncludes(String includes) {
        this.includes = includes;
    }
    
    public String getIncludes() {
        return this.includes;
    }
    
    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }
    
    public String getExcludes() {
        return this.excludes;
    }
    
    public boolean getTargetFolderAbsolute() {
        return this.targetFolderAbsolute;
    }
    
    public void setTargetFolderAbsolute(boolean targetFolderAbsolute) {
        this.targetFolderAbsolute = targetFolderAbsolute;
    }
}
