package xray.evs.web;

import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import xray.evs.dto.Organizer;
import xray.evs.logic.OrganizerLogic;
import xray.evs.utilities.MailHandler;

/**
 *
 * @author Team Xray
 */
@SessionScoped
@Named
@ManagedBean
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(LoginBean.class.getName());

    private Organizer currentOrganizer;

    @EJB
    private OrganizerLogic ol;

    @EJB
    private MailHandler mh;

    @PostConstruct
    public void newSession() {
        LOG.info("EVS: NEW SESSION");
    }

    public boolean isLoggedIn() {
        Principal p = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getUserPrincipal();
        //SendEmail();
        return p != null;
    }

    public boolean isOrganizerRole() {
        if (!isLoggedIn()) {
            return false;
        }
        return FacesContext.getCurrentInstance()
                .getExternalContext().isUserInRole("ORGANIZER");
    }

    public boolean isAdminRole() {
        if (!isLoggedIn()) {
            return false;
        }

        return FacesContext.getCurrentInstance()
                .getExternalContext().isUserInRole("ADMIN");
    }
    private Principal oldPrincipal = null;

    public Organizer getOrganizer() {

        Principal p = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getUserPrincipal();
        if (p == null) {
            setCurrentOrganizer(null);
        } else {
            if (!p.equals(oldPrincipal)) {
                LOG.log(Level.INFO, "EVS: LOGIN user {0}", p.getName());
                setCurrentOrganizer(getOl().getCurrentUser());

            }
        }
        setOldPrincipal(p);
        return getCurrentOrganizer();
    }

    public String getCurrentUserName() {
        if (!isLoggedIn()) {
            return "";
        }

        return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
    }

    public void invalidateSession() {
        LOG.log(Level.INFO, "invalidateSession()");
        Principal p = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getUserPrincipal();
        if (p != null) {
            LOG.log(Level.INFO, "EVS: LOGOUT user {0}", p.getName());
        }

        setCurrentOrganizer(null);
        setOldPrincipal(null);
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();
    }

    public void logout() {
        invalidateSession();
        FacesContext.getCurrentInstance()
                .responseComplete();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Organizer getCurrentOrganizer() {
        return currentOrganizer;
    }

    public void setCurrentOrganizer(Organizer currentOrganizer) {
        this.currentOrganizer = currentOrganizer;
    }

    public OrganizerLogic getOl() {
        return ol;
    }

    public void setOl(OrganizerLogic ol) {
        this.ol = ol;
    }

    public Principal getOldPrincipal() {
        return oldPrincipal;
    }

    public void setOldPrincipal(Principal oldPrincipal) {
        this.oldPrincipal = oldPrincipal;
    }

    public void SendEmail() {
        mh.sendTestMail();
    }
}
