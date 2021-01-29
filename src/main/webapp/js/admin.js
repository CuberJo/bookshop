/**
 * For collapse events
 */
$(function() {
    // body...
    const body = document.body;
    const menuLinks = document.querySelectorAll(".admin-menu a");
    const collapseBtn = document.querySelector(".admin-menu .collapse-btn");
    const toggleMobileMenu = document.querySelector(".toggle-mob-menu");
    const collapsedClass = "collapsed";

    collapseBtn.addEventListener("click", function() {
        this.getAttribute("aria-expanded") == "true"
            ? this.setAttribute("aria-expanded", "false")
            : this.setAttribute("aria-expanded", "true");
        this.getAttribute("aria-label") == "collapse menu"
            ? this.setAttribute("aria-label", "expand menu")
            : this.setAttribute("aria-label", "collapse menu");
        body.classList.toggle(collapsedClass);
    });

    toggleMobileMenu.addEventListener("click", function() {
        this.getAttribute("aria-expanded") == "true"
            ? this.setAttribute("aria-expanded", "false")
            : this.setAttribute("aria-expanded", "true");
        this.getAttribute("aria-label") == "open menu"
            ? this.setAttribute("aria-label", "close menu")
            : this.setAttribute("aria-label", "open menu");
        body.classList.toggle("mob-menu-opened");
    });

    for (const link of menuLinks) {
        link.addEventListener("mouseenter", function() {
            body.classList.contains(collapsedClass) &&
            window.matchMedia("(min-width: 768px)").matches
                ? this.setAttribute("title", this.textContent)
                : this.removeAttribute("title");
        });
    }});







/******************************************************************************************************/
/***************************************   Payments    ************************************************/
/******************************************************************************************************/

let paymentsPageNum = 1;
const paymentsPerPage = 4;
let paymentsQuantity;
$('#paymentsPagination .page-num').text(' ' + paymentsPageNum);

hidePrev('paymentsPagination');

let payments;

/**
 * Fetches data from server
 * @param {number} pageNumber
 */
function fetchPayments(paymentsPageNum) {
    if (paymentsPageNum == null) {
        paymentsPageNum = 1
    }

    $.ajax({
        url: 'http://localhost:8080/admin',
        type: 'GET',
        data: ({
            page: paymentsPageNum,
            fetch: 'payments'
        }),
        success: function (jsonStr) {
            $('#payments tr td').remove();
            payments = jsonStr;
            console.log('payments: ' + payments);
            renderPayments(payments);
        }
    });
}

/**
 * Fetches payments quantity
 */
function fetchPaymentsQuantity() {
    let needCountPayments = 1;

    $.ajax({
        url: 'http://localhost:8080/admin',
        type: 'GET',
        data: ({
            countPayments: needCountPayments,
        }),
        success: function (quntity) {
            paymentsQuantity = quntity;
            console.log('payments quantity: ' + quntity);
            if (paymentsQuantity <= paymentsPerPage) {
                hideNext('paymentsPagination');
            }
            fetchPayments(1)
        }
    });

    $('#books tr td').remove();
    $('#users_ tr td').remove();
}

/**
 * Renders JSON string on page
 * @param payments
 */
function renderPayments(payments) {
    console.log(payments);

    if (payments != null) {

        $.each(payments, function (index, el) {

            let block = '<tr>' +
                '<td>' + el.user.login + '</td>' +
                '<td>' + el.book.title + '</td>' +
                '<td>' + el.book.author + '</td>' +
                '<td>' + el.paymentTime.monthValue + '.' + el.paymentTime.dayOfMonth + '.' + el.paymentTime.year + '</td>' +
                '<td>' + el.price + '$</td>' +
                '</tr>';
            $('#payments').append(block);

        })
    }

    if (payments == null || payments.length === 0) {
        let block;
        let locale =  $('#locale').text();

    }
}

$(function () {
    $('#paymentsHref').bind('click', function () {
        fetchPaymentsQuantity();
    });


    /**
     * on prev click
     */
    $('#paymentsPagination .prev').bind('click', function () {
        if (--paymentsPageNum > 0) {
            $.ajax({
                url: 'http://localhost:8080/admin',
                type: 'GET',
                data: ({
                    page: paymentsPageNum,
                    fetch: 'payments'
                }),
                success: function (jsonStr) {
                    $('#payments tr td').remove();
                    payments = jsonStr;
                    renderPayments(payments);
                    $('#paymentsPagination .page-num').text(' ' + paymentsPageNum);

                    if ((paymentsPageNum * paymentsPerPage + 1) < paymentsQuantity) {
                        showNext('paymentsPagination');
                    }
                    if (paymentsPageNum - 1 === 0 ) {
                        hidePrev('paymentsPagination');
                    }
                }
            });
        } else {
            paymentsPageNum++;
            hidePrev('paymentsPagination');
        }
    });

    /**
     * on next click
     */
    $('#paymentsPagination .next').bind('click', function () {
        if ((paymentsPageNum * paymentsPerPage + 1) <= paymentsQuantity) {
            $.ajax({
                url: 'http://localhost:8080/admin',
                type: 'GET',
                data: ({
                    page: ++paymentsPageNum,
                    fetch: 'payments'
                }),
                success: function (jsonStr) {
                    payments = jsonStr;
                    $('#payments tr td').remove();
                    renderPayments(payments);
                    $('#paymentsPagination .page-num').text(' ' + paymentsPageNum);
                    if (paymentsPageNum > 0) {
                        showPrev('paymentsPagination');
                    }
                    if (paymentsPageNum * paymentsPerPage >= paymentsQuantity) {
                        hideNext('paymentsPagination');
                    }
                }
            });
        } else {
            hideNext('paymentsPagination');
        }
    });
})












/******************************************************************************************************/
/****************************************    Users     ************************************************/
/******************************************************************************************************/

let usersPageNum = 1;
const usersPerPage = 4;
let usersQuantity;
$('#usersPagination .page-num').text(' ' + usersPageNum);

hidePrev('usersPagination');

let users;

/**
 * Fetches data from server
 * @param {number} usersPageNumber
 */
function fetchUsers(usersPageNum) {
    if (usersPageNum == null) {
        usersPageNum = 1
    }

    $.ajax({
        url: 'http://localhost:8080/admin',
        type: 'GET',
        data: ({
            page: usersPageNum,
            fetch: 'users'
        }),
        success: function (jsonStr) {
            $('#users_ tr td').remove();
            users = jsonStr;
            console.log('users: ' + users);
            renderUsers(users);
        }
    });
}

/**
 * Fetches users quantity
 */
function fetchUsersQuantity() {
    let needCountUsers = 1;

    $.ajax({
        url: 'http://localhost:8080/admin',
        type: 'GET',
        data: ({
            countUsers: needCountUsers,
        }),
        success: function (quntity) {
            usersQuantity = quntity;
            console.log('users quantity: ' + quntity);
            if (usersQuantity <= usersPerPage) {
                hideNext('usersPagination');
            }
            fetchUsers(1);
        }
    });
    $('#books tr td').remove();
    $('#payments tr td').remove();
}

/**
 * Renders JSON string on page
 * @param users
 */
function renderUsers(users) {
    console.log(users);

    if (users != null) {

        $.each(users, function (index, el) {

            let block = '<tr>' +
                '<td>' + el.name + '</td>' +
            '<td>' + el.login + '</td>' +
            '<td>' + el.email + '</td>' +
            '</tr>';
            $('#users_').append(block);
        })
    }

    if (users == null || users.length === 0) {
        let block;
        let locale =  $('#locale').text();

    }
}

$(function () {
    $('#usersHref').bind('click', function () {
        fetchUsersQuantity();
    })

    /**
     * on prev click
     */
    $('#usersPagination .prev').bind('click', function () {
        if (--usersPageNum > 0) {
            $.ajax({
                url: 'http://localhost:8080/admin',
                type: 'GET',
                data: ({
                    page: usersPageNum,
                    fetch: 'users'
                }),
                success: function (jsonStr) {
                    users = jsonStr;
                    $('#users_ tr td').remove();
                    renderUsers(users);
                    $('#usersPagination .page-num').text(' ' + usersPageNum);

                    if ((usersPageNum * usersPerPage + 1) < usersQuantity) {
                        showNext('usersPagination');
                    }
                    if (usersPageNum - 1 === 0 ) {
                        hidePrev('usersPagination');
                    }
                }
            });
        } else {
            usersPageNum++;
            hidePrev('usersPagination');
        }
    });

    /**
     * on next click
     */
    $('#usersPagination .next').bind('click', function () {
        if ((usersPageNum * usersPerPage + 1) <= usersQuantity) {
            $.ajax({
                url: 'http://localhost:8080/admin',
                type: 'GET',
                data: ({
                    page: ++usersPageNum,
                    fetch: 'users'
                }),
                success: function (jsonStr) {
                    users = jsonStr;
                    $('#users_ tr td').remove();
                    renderUsers(users);
                    $('#usersPagination .page-num').text(' ' + usersPageNum);
                    if (usersPageNum > 0) {
                        showPrev('usersPagination');
                    }
                    if (usersPageNum * usersPerPage >= usersQuantity) {
                        hideNext('usersPagination');
                    }
                }
            });
        } else {
            hideNext('usersPagination');
        }
    });
})















/******************************************************************************************************/
/****************************************    Books     ************************************************/
/******************************************************************************************************/

let booksPageNum = 1;
const booksPerPage = 8;
let booksQuantity;

$('#booksPagination .page-num').text(' ' + booksPageNum);

hidePrev('booksPagination');

let books;

/**
 * Fetches data from server
 * @param {number} pageNumber
 */
function fetchBooks(bookPageNum) {
    if (booksPageNum == null) {
        booksPageNum = 1
    }

    $.ajax({
        url: 'http://localhost:8080/books',
        type: 'GET',
        data: ({
            page: booksPageNum,
        }),
        success: function (jsonStr) {
            $('#books tr td').remove();
            books = jsonStr;
            console.log('books: ' + books);
            renderBooks(books);
        }
    });
}

/**
 * Fetches users quantity
 */
function fetchBooksQuantity() {
    let needCount = 1;

    $.ajax({
        url: 'http://localhost:8080/books',
        type: 'GET',
        data: ({
            count: needCount,
        }),
        success: function (quntity) {
            booksQuantity = quntity;
            console.log('books quantity: ' + quntity);
            if (booksQuantity <= booksPerPage) {
                hideNext('booksPagination');
            }
            fetchBooks(1)
        }
    });
    $('#payments tr td').remove();
    $('#users_ tr td').remove();
}

/**
 * Renders JSON string on page
 * @param books
 */
function renderBooks(books) {
    console.log(books);

    if (books != null) {

        $.each(books, function (index, el) {

            let block = '<tr>' +
                '<td>' + el.isbn + '</td>' +
            '<td>' + el.title + '</td>' +
            '<td>' + el.author + '</td>' +
            '<td>' + el.price + '$</td>' +
            '<td>' + el.publisher + '</td>' +
            '<td>' + el.genre.genre + '</td>' +
            '<td><img height="70px" src="data:image/jpg;base64,' + el.base64Image + '"></td>' +
            '</tr>';
            $('#books').append(block);

        })
    }

    if (books == null || books.length === 0) {
        let block;
        let locale =  $('#locale').text();
    }
}

$(function () {
    $('#booksHref').bind('click', function () {
        fetchBooksQuantity();
    })


    /**
     * on prev click
     */
    $('#booksPagination .prev').bind('click', function () {
        if (--booksPageNum > 0) {
            $.ajax({
                url: 'http://localhost:8080/books',
                type: 'GET',
                data: ({
                    page: booksPageNum,
                }),
                success: function (jsonStr) {
                    books = jsonStr;
                    $('#books tr td').remove();
                    renderBooks(books);
                    $('#booksPagination .page-num').text(' ' + booksPageNum);

                    if ((booksPageNum * booksPerPage + 1) < booksQuantity) {
                        showNext('booksPagination');
                    }
                    if (booksPageNum - 1 === 0 ) {
                        hidePrev('booksPagination');
                    }
                }
            });
        } else {
            booksPageNum++;
            hidePrev('booksPagination');
        }
    });

    /**
     * on next click
     */
    $('#booksPagination .next').bind('click', function () {
        if ((booksPageNum * booksPerPage + 1) <= booksQuantity) {
            $.ajax({
                url: 'http://localhost:8080/books',
                type: 'GET',
                data: ({
                    page: ++booksPageNum
                }),
                success: function (jsonStr) {
                    books = jsonStr;
                    $('#books tr td').remove();
                    renderBooks(books);
                    $('#booksPagination .page-num').text(' ' + booksPageNum);
                    if (booksPageNum > 0) {
                        showPrev('booksPagination');
                    }
                    if (booksPageNum * booksPerPage >= booksQuantity) {
                        hideNext('booksPagination');
                    }
                }
            });
        } else {
            hideNext('booksPagination');
        }
    });
})














// /**
//  * Binding buttons with actions
//  */
// $(document).ready(function () {
//
//     let genreName = $('#genre').text();
//     if (genreName === "") {
//         genreName = undefined;
//     }
//
//     /**
//      * on prev click
//      */
//     $('.prev').bind('click', function () {
//         if (--pageNum > 0) {
//             $.ajax({
//                 url: 'http://localhost:8080/books',
//                 type: 'GET',
//                 data: ({
//                     page: pageNum,
//                     genre: genreName
//                 }),
//                 success: function (jsonStr) {
//                     books = jsonStr;
//                     $('#toInsert').empty();
//                     render(books);
//                     $('.page-num').text(' ' + pageNum);
//
//                     if ((pageNum * booksPerPage + 1) < booksQuantity) {
//                         showNext();
//                     }
//                     if (pageNum - 1 === 0 ) {
//                         hidePrev();
//                     }
//                 }
//             });
//         } else {
//             pageNum++;
//             hidePrev();
//         }
//     });
//
//     /**
//      * on next click
//      */
//     $('.next').bind('click', function () {
//         if ((pageNum * booksPerPage + 1) < booksQuantity) {
//             $.ajax({
//                 url: 'http://localhost:8080/books',
//                 type: 'GET',
//                 data: ({page: ++pageNum, genre: $('#genre').text()}),
//                 success: function (jsonStr) {
//                     books = jsonStr;
//                     $('#toInsert').empty();
//                     render(books);
//                     $('.page-num').text(' ' + pageNum);
//                     if (pageNum > 0) {
//                         showPrev();
//                     }
//                     if (pageNum * booksPerPage >= booksQuantity) {
//                         hideNext();
//                     }
//                 }
//             });
//         } else {
//             hideNext();
//         }
//     });
//
// })




function hideNext(idStr) {
    $('#' + idStr + ' .next').css('border-color', 'gray');
    $('#' + idStr + ' .next').css('color', 'gray');
    $('#' + idStr + ' .next').css('pointer-events', 'none');
}

function showNext(idStr) {
    $('#' + idStr + ' .next').css('color', '#000');
    $('#' + idStr + ' .next').css('border-color', '#000');
    $('#' + idStr + ' .next').css('cursor', 'pointer');
    $('#' + idStr + ' .next').css('pointer-events', 'auto');
}

function hidePrev(idStr) {
    $('#' + idStr + ' .prev').css('border-color', 'gray');
    $('#' + idStr + ' .prev').css('color', 'gray');
    $('#' + idStr + ' .prev').css('pointer-events', 'none');
}

function showPrev(idStr) {
    $('#' + idStr + ' .prev').css('color', '#000');
    $('#' + idStr + ' .prev').css('border-color', '#000');
    $('#' + idStr + ' .prev').css('cursor', 'pointer');
    $('#' + idStr + ' .prev').css('pointer-events', 'auto');
}