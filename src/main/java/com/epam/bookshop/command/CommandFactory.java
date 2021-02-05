package com.epam.bookshop.command;

import com.epam.bookshop.command.impl.*;
import com.epam.bookshop.command.impl.AdvancedBookSearchCommand;

import java.util.Objects;

/**
 * Factory to create instance of {@link Command} by {@code command} query parameter
 */
public class CommandFactory {

    private static final String ACCOUNT = "account";
    private static final String ADMIN = "admin";
    private static final String BOOKS = "books";
    private static final String CART = "cart";
    private static final String BOOK_DETAILS = "book_details";
    private static final String SEARCH = "search";
    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String REGISTER = "register";
    private static final String FORGOT_PASSWORD = "forgot_password";
    private static final String RESET_PASSWORD = "reset_password";
    private static final String CONTACT_US = "contact_us";
    private static final String CHANGE_LOCALE = "change_locale";
    private static final String ADD_IBAN = "add_iban";
    private static final String SEND_CONTACT_FORM = "send_contact_form";
    private static final String ADD_TO_CART = "add_to_cart";
    private static final String REMOVE_FROM_CART = "remove_from_cart";
    private static final String PURCHASE = "purchase";
    private static final String CHOOSE_IBAN = "choose_iban";
    private static final String LOAD_IBANs = "load_ibans";
    private static final String FINISHED_PURCHASE = "finished_purchase";
    private static final String PERSONAL_PAGE = "personal_page";
    private static final String UNBIND_IBAN = "unbind_iban";
    private static final String DELETE_ACCOUNT = "delete_account";
    private static final String READ_BOOK = "read_book";
    private static final String ADD_IBAN_PAGE = "add_iban_page";
    private static final String BOOK_SEARCH_RESULTS = "book_search_results";



    private static final String RELATED_BOOKS = "relatedBooks";
    private static final String BESTSELLERS = "bestsellers";
    private static final String EXCLUSIVE_BOOK = "exclusive";
    private static final String LATEST_PRODUCTS = "latestProducts";

    private static final String ADVANCED_BOOK_SEARCH = "advanced_book_search";
    private static final String LIVE_BOOK_SEARCH = "live_book_search";
    private static final String NOT_ADVANCED_BOOK_SEARCH = "not_advanced_book_search";

    public static Command command(String command) {

        if (Objects.isNull(command)) {
            return new HomeCommand();
        }

        switch (command) {
            case ACCOUNT:
                return new AccountCommand();
            case ADMIN:
                return new AdminCommand();
            case BOOKS:
                return new BooksCommand();
            case CART:
                return new CartCommand();
            case BOOK_DETAILS:
                return new BookDetailsCommand();
            case SEARCH:
                return new SearchCommand();
            case LOGIN:
                return new LoginCommand();
            case LOGOUT:
                return new LogoutCommand();
            case REGISTER:
                return new RegisterCommand();
            case FORGOT_PASSWORD:
                return new ForgotPasswordCommand();
            case RESET_PASSWORD:
                return new ResetPasswordCommand();
            case CONTACT_US:
                return new ContactUsCommand();
            case CHANGE_LOCALE:
                return new ChangeLocaleCommand();
            case SEND_CONTACT_FORM:
                return new SendContactFormCommand();
//            case REMOVE_FROM_CART:
//                return new RemoveFromCartController();
            case PURCHASE:
                return new PurchaseCommand();
            case CHOOSE_IBAN:
                return new ChooseIBAN();
            case ADD_IBAN:
                return new AddIBANCommand();
            case FINISHED_PURCHASE:
                return new FinishedPurchaseCommand();
            case PERSONAL_PAGE:
                return new PersonalPageCommand();
            case DELETE_ACCOUNT:
                return new DeleteAccountCommand();
            case ADD_IBAN_PAGE:
                return new AddIbanPageCommand();
            case BOOK_SEARCH_RESULTS:
                return new BookSearchResultsCommand();

            case RELATED_BOOKS:
                return new RelatedBooksCommand();
            case BESTSELLERS:
                return new BestsellersCommand();
            case EXCLUSIVE_BOOK:
                return new ExclusiveBookCommand();
            case LATEST_PRODUCTS:
                return new LatestBookProductsCommand();

            case ADVANCED_BOOK_SEARCH:
                return new AdvancedBookSearchCommand();
            case NOT_ADVANCED_BOOK_SEARCH:
                return new NotAdvancedBookSearchCommand();
            case LIVE_BOOK_SEARCH:
                return new LiveBookSearchCommand();
//            case READ_BOOK:
//                return new ReadBookController();
//            case LOAD_IBANs:
//                return new LoadIBANsController();
            default:
                return new HomeCommand();
        }
    }
}
