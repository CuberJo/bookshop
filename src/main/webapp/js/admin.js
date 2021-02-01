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

            let i = 0;
            let block = '<tr>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' + el.isbn + '</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' + el.title + '</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' + el.author + '</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' + el.price + '$</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' + el.publisher + '</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' + el.genre.genre + '</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' +
                    '<img height="70px" src="data:image/jpg;base64,' + el.base64Image + '" onclick="editImg(' + index + ')">' +
                    '<input type="file" name="file" style="display: none" id="fileUp_' + index + '">' +
                '</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '"><button type="button" onclick="edit(' + index + ')">Edit</button></td>' +
                '</tr>';
            let block2 = '<tr>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' + el.isbn + '</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' + el.title + '</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' + el.author + '</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' + el.price + '$</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' + el.publisher + '</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' + el.genre.genre + '</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '">' +
                    '<img height="70px" src="data:image/jpg;base64,' + el.base64Image + '" onclick="editImg(' + index + ')">' +
                    '<div id="divFileUp_' + index + '" style="display: none" class="example-2">' +
                        '<div class="form-group">' +
                            '<input  type="file" name="file" id="fileUp_' + index + '" class="input-file">' +
                            '<label for="file" class="btn btn-tertiary js-labelFile">' +
                                '<i class="icon fa fa-check"></i>' +
                                '<span class="js-fileName">Upload file</span>' +
                            '</label>' +
                        '</div>' +
                    '</div>' +
                    // '<input style="display: none;" type="file" id="fileUp_' + index + '"/>' +
                '</td>' +
                '<td class="' + index + '" id="' + index + '_' + ++i + '"><button type="button" onclick="edit(' + index + ')">Edit</button></td>' +
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

    // $('#edit').click(function () {
    //     let block = '<a id="save" href="#">Save</a>';
    //     $(this).append(block);
    //
    // })
})










/******************************************************************************************************/
/**************************************  Books editing   **********************************************/
/******************************************************************************************************/










let selectedGenre;
let oldSelectedGenre;
let oldISBN;
let base64Image;
let oldBase64Image;
let fileToSend;
let fd = new FormData();


let optionsBlock = '<select class="genres">' +
    '<option selected value="">Choose genre</option>'+
    '<option value="ROMANCE" selected>Romance</option>'+
    '<option value="ACTION_AND_ADVENTURE">Action and adventure</option>'+
    '<option value="MYSTERY_AND_THRILLER">Mystery and thriller</option>'+
    '<option value="BIOGRAPHIES_AND_HISTORY">Biographies and history</option>'+
    '<option value="CHILDREN">Children</option>'+
    '<option value="FANTASY">Fantasy</option>'+
    '<option value="HISTORICAL_FICTION">Historical fiction</option>'+
    '<option value="HORROR">Horror</option>'+
    '<option value="LITERARY_FICTION">Literaly fiction</option>'+
    '<option value="NON-FICTION">Non-fiction</option>'+
    '<option value="SCIENCE-FICTION">Sci-fiction</option>'+
    '</select>';


/**
 * Permits to edit row
 *
 * @param rowNum
 */
function edit(rowNum) {
    console.log('edit')
    $('.' + rowNum).attr('contenteditable', 'true');

    oldISBN = $('#' + rowNum + '_1').text();

    oldSelectedGenre = $('#' + rowNum + '_6').text();
    $('#' + rowNum + '_6').empty();
    $('#' + rowNum + '_6').attr('contenteditable', 'false');
    $('#' + rowNum + '_6').html(optionsBlock);

    /**
     * Processes genre option selections
     */
    $(".genres").change(function(){
        selectedGenre = $(this).children("option:selected").val();
    });

    $('#' + rowNum + '_8').replaceWith('<td class="' + rowNum + '" id="' + rowNum + '_8"><button onclick="save(' + rowNum + ')">Save</button></td>');

    oldBase64Image = $('#' + rowNum + '_7 img').attr('src');
    // console.log(oldBase64Image)
}


/**
 * Saves edited data
 *
 * @param {string} rowNum
 */
function save(rowNum) {
    console.log('save')

    if (!sendData(rowNum)) {
        return;
    }

    $('.' + rowNum).attr('contenteditable', 'false');

    if (selectedGenre !== "" && selectedGenre != undefined) {
        $('#' + rowNum + '_6').empty();
        $('#' + rowNum + '_6').text(selectedGenre);
    } else {
        $('#' + rowNum + '_6').empty();
        $('#' + rowNum + '_6').text(oldSelectedGenre);
    }

    if (base64Image !== "" && base64Image !== undefined) {
        $('#' + rowNum + '_7 img').attr('src', base64Image);
        // console.log(base64Image)
    } else {
        $('#' + rowNum + '_7 img').attr('src', oldBase64Image);
    }

    // $('#' + rowNum + '_7 input').css('display', 'none');
    // $('#divFileUp_' + rowNum).css('display', 'none');
    $('#fileUp_' + rowNum).css('display', 'none');
    $('#' + rowNum + '_7 img').css('display', 'block');

    $('#' + rowNum + '_8').replaceWith('<td class="' + rowNum + '" id="' + rowNum + '_8"><button onclick="edit(' + rowNum + ')">Edit</button></td>');
}


function sendData(rowNum) {
    let isbn = $('#' + rowNum + '_1').text();
    let title = $('#' + rowNum + '_2').text();
    let author = $('#' + rowNum + '_3').text();
    let price = $('#' + rowNum + '_4').text();
    let publisher = $('#' + rowNum + '_5').text();
    let genre = selectedGenre !== "" && selectedGenre !== undefined ? selectedGenre : oldSelectedGenre;


    let error = "";

    // let isbnRegex = /^[\d]+-[\d]+-[\d]+-[\d]+-[\d]+$/;
    // if (!isbnRegex.test(isbn)) {
    //     error += "Invalid ISBN input; ";
    // }
    // let titleRegex = /^[-()!\d\s.a-zA-Zа-яА-Я]{1,50}$/;
    // if (!titleRegex.test(title)) {
    //     error += "Invalid title input; ";
    // }
    // let authorRegex = /^[-\s.a-zA-Zа-яА-Я]{1,50}$/;
    // if (!authorRegex.test(author)) {
    //     error += "Invalid author name input; ";
    // }
    // let publisherRegex = /^[-&a-zA-Zа-яА-Я\s]{1,50}$/;
    // if (!publisherRegex.test(publisher)) {
    //     error += "Invalid publisher input; ";
    // }
    // let priceRegex = /^[0-9]+(\.[0-9]+)?(\$)$/;
    // if (!priceRegex.test(price)) {
    //     error += "Invalid price number input; ";
    // }

    let malicious_regex = /[<>*;='#)+("]+/;
    if (malicious_regex.test(isbn)) {
        error = "Invalid isbn input";
    }
    if (malicious_regex.test(title)) {
        error = "Invalid title input";
    }
    if (malicious_regex.test(author)) {
        error = "Invalid author input";
    }
    if (malicious_regex.test(price)) {
        error = "Invalid price input";
    }
    if (malicious_regex.test(publisher)) {
        error = "Invalid publisher input";
    }

    let whitespace_regex = /^[\s]+$/;
    if (whitespace_regex.test(isbn)) {
        error = "Empty isbn input";
    }
    if (whitespace_regex.test(title)) {
        error = "Empty title input";
    }
    if (whitespace_regex.test(author)) {
        error = "Empty author input";
    }
    if (whitespace_regex.test(price)) {
        error = "Empty price input";
    }
    if (whitespace_regex.test(publisher)) {
        error = "Empty publisher input";
    }

    if (error !== "" && error !== undefined) {
        $('#er').text(error);
        return false;
    }

    // let formData = new FormData();
    // formData.append("file", fileToSend);


    fd.append('oldISBN', oldISBN);
    fd.append('isbn', isbn);
    fd.append('title', title);
    fd.append('author', author);
    fd.append('price', price);
    fd.append('publisher', publisher);
    fd.append('genre', genre);


    $.ajax({
        url: 'http://localhost:8080/admin',
        data: fd,
        processData: false,
        contentType: false,
        type: 'POST',
        success: function(erMes){
            if (erMes !== undefined && erMes !== "") {
                $('#er').text(erMes);
                return;
            }
            console.log('sending done...');
        }
    });

    // /**
    //  * Sends updated data to server
    //  */
    // $.ajax({
    //     url: 'http://localhost:8080/admin',
    //     type: 'POST',
    //     data: ({
    //         oldISBN: oldISBN,
    //         isbn: isbn,
    //         title: title,
    //         author: author,
    //         price: price,
    //         publisher: publisher,
    //         genre: genre,
    //         imgFile: fd
    //         // base64Image: base64Image !== "" && base64Image !== undefined ? base64Image : oldBase64Image
    //     }),
    //     success: function (erMes) {
    //         if (erMes !== undefined && erMes !== "") {
    //             $('#er').text(erMes);
    //             return;
    //         }
    //         console.log('sending done...');
    //     }
    // });

    return true;
}



/**
 * Allows to upload new img
 *
 * @param {string} imgRow
 */
function editImg(imgRow) {
    console.log('click');
    if ($('#' + imgRow + '_8 button').text() === 'Save') {
    // if ($('#' + imgRow + '_7').attr('contenteditable') == 'true') {

        $('#' + imgRow + '_7 img').css('display', 'none');
        // $('#' + '_6').attr('contenteditable', 'false');
        // $('#' + imgRow + '_7 divFileUp_' + imgRow).css('display', 'block');
        // $('#divFileUp_' + imgRow).css('display', 'block');
        $('#fileUp_' + imgRow).css('display', 'block');

        $('#' + imgRow + '_7').attr('contenteditable', 'false');

        console.log('#fileUp_' + imgRow);
        $('#fileUp_' + imgRow).change(function () {
            console.log('change')
            encodeImageFileURL(imgRow);
        });
    }
}


/**
 * Encodes image to base64 format
 */
function encodeImageFileURL(imgRow) {
    let fileSelect = document.getElementById('fileUp_' + imgRow).files;
    if (fileSelect.length > 0) {
        fd.append( 'file', fileSelect[0] );

        fileSelect = fileSelect[0];


        let fileReader = new FileReader();

        console.log('result')
        fileReader.onload = function(FileLoadEvent) {
            let srcData = FileLoadEvent.target.result;

            // document.getElementById('imageFile').src = srcData;
            // document.getElementById('base64Image').innerHTML = srcData;
            base64Image = srcData;
            // console.log('base64Image: '+base64Image)
        }
        fileReader.readAsDataURL(fileSelect);
    }
}


let newBookForm = new FormData();
$(function () {

    $('#fileBookToAdd').change(function () {
        // let fFiles = $(this).files;
        let fileSelect = document.getElementById('fileBookToAdd').files;
        newBookForm.append( 'file', fileSelect[0]);

        // newBookForm.append('file', fFiles[0])
        console.log('added')
    })

    $('#addBookForm').submit(function () {
        newBookForm.append('isbn', $('#addBookForm input[name=isbn]').val());
        newBookForm.append('title', $('#addBookForm input[name=title]').val());
        newBookForm.append('author', $('#addBookForm input[name=author]').val());
        newBookForm.append('price', $('#addBookForm input[name=price]').val());
        newBookForm.append('publisher', $('#addBookForm input[name=publisher]').val());
        newBookForm.append('genre', $('#addBookForm input[name=genre]').val() );


        $.ajax({
            url: 'http://localhost:8080/admin',
            data: newBookForm,
            processData: false,
            contentType: false,
            type: 'POST',
            success: function(erMes){
                if (erMes !== undefined && erMes !== "") {
                    $('#er').text(erMes);
                    return;
                }
                console.log('sending done...');
            }
        });
    })

    // $('#addBookForm').submit(function () {
    //     var url = $(this).attr("action");
    //     var formData = new FormData($(this));
    //     formData.append( 'file', newBookFiles[0] )
    //     // $("input[name=isbn]").val()
    //     //     formData[node.isbn] = node.value;
    //     // });
    //     // $(this).find("input[name]").each(function (index, node) {
    //     //     formData[node.name] = node.value;
    //     // });
    //     $.post(url, formData).done(function (data) {
    //         alert(data);
    //     });
    // })

    /**
     * Adds new row
     */
    $('#add').click(function () {
        let block = '<tr>' +
            '<td class="' + (booksQuantity + 1) + '" id="' + (booksQuantity + 1) + '_1"></td>' +
            '<td class="' + (booksQuantity + 1) + '" id="' + (booksQuantity + 1) + '_2"></td>' +
            '<td class="' + (booksQuantity + 1) + '" id="' + (booksQuantity + 1) + '_3"></td>' +
            '<td class="' + (booksQuantity + 1) + '" id="' + (booksQuantity + 1) + '_4"></td>' +
            '<td class="' + (booksQuantity + 1) + '" id="' + (booksQuantity + 1) + '_5"></td>' +
            '<td class="' + (booksQuantity + 1) + '" id="' + (booksQuantity + 1) + '_6">' +
                optionsBlock +
            '</td>' +
            '<td class="' + (booksQuantity + 1) + '" id="' + (booksQuantity + 1) + '_7">' +
                '<input style="display: none;" type="file" id="fileUp_' + (booksQuantity + 1) + '"/>' +
            '</td>' +
            '<td class="' + (booksQuantity + 1) + '" id="' + (booksQuantity + 1) + '_6"><button id="saveNew">Save</button></td>' +
            '</tr>';

        // /**
        //  * Processes genre option selections
        //  */
        // $(".genres").change(function(){
        //     selectedGenre = $(this).children("option:selected").val();
        //     console.log('selected genre is ' + selectedGenre)
        // });

        $('#' + (booksQuantity + 1) + '_6').empty();
        $('#' + (booksQuantity + 1) + '_6').attr('contenteditable', 'false');
        $('#' + (booksQuantity + 1) + '_6').html(optionsBlock);

        $('#books').append(block);
        $('#books').attr('contenteditable', 'true');
    })
})













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