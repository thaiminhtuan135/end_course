package example.end_course.service.account;

import example.end_course.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account save(Account account);

    Optional<Account> getAccountById(int id);

    void delete(int id);

    List<Account> getAccounts();
}
