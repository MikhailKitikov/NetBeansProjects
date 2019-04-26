package mksoft.sharemoments.cdi;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import mksoft.sharemoments.entity.User;
import mksoft.sharemoments.ejb.UserDAO;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author mk99
 */
@ManagedBean(name = "logBean", eager = true)
@SessionScoped
public class LoginBean implements Serializable {
    
    private static final long serialVersionUID = 1L;    
    //private User currentUser;
    
    private String username;
    private String password;

    private boolean loginSuccess;

    @EJB
    private UserDAO userDAO;
    
    //

    public String login() {
        
        if (!validateInput()) {
            loginSuccess = false;
            return "authorization";
        } 
        int res = userDAO.checkPassword(username, password);  
        
        if (res != 0) {
            loginSuccess = false;
            String msg = (res == 1 ? "User not found" : "Incorrect password");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
            return "authorization";
        }
        loginSuccess = true;
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("username", username);
        return "profilePage?faces-redirect=true";
    }
    
    public boolean validateInput() {        
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid input"));
            return false;
        }
        return true;
    }

    public List<User> getAllUsers(){
        return userDAO.getAllUsers();
    }
    
    ///    
    
    public String logout() {

        if (!loginSuccess) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "!", "You are not logged in"));
        }
        loginSuccess = false;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";
    }
    
    public void showResult()
    { 
        String msg = "You are welcome, " + username + "!";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", msg));
    }
    
    //
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }
    
    public LoginBean (){
    }

}