package DAO;

import Model.Account;
import java.util.List;

public interface AccountDAO {
    Account createAccount(Account account);
    Account getAccountById(int id);
    List<Account> getAllAccounts();
    boolean updateAccount(Account account);
    boolean deleteAccount(int id);
    Account getAccountByUsername(String username);
}

