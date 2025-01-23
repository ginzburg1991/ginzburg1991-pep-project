package Service;

import DAO.AccountDAO;
import Model.Account;
import java.util.List;

public class AccountService{
    private final AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account createAccount(Account account){
        return accountDAO.createAccount(account);
    }

    public Account getAccountById(int id){
        return accountDAO.getAccountById(id);
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public boolean updateAccount(Account account){
        return accountDAO.updateAccount(account);
    }

    public Account getAccountByUsername(String username){
        return accountDAO.getAccountByUsername(username);
    }

    public boolean deleteAccount(int id){
        return accountDAO.deleteAccount(id);
    }

    public Account validateLogin(String username, String password){
        Account account = accountDAO.getAccountByUsername(username);
        if(account != null && account.getPassword().equals(password)){
            return account;
        }
        return null;
    }
    
}