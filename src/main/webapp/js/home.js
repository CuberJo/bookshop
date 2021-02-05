
let bestsellersBooks;
let latestProductsBooks;
let exclusiveBook;

/**
 * Fetches bestsellers from server
 */
function fetchBestsellers() {

    $.ajax({
        url: 'http://localhost:8080/books',
        type: 'GET',
        data: ({
            // bestsellers: 'fetch'
            command: 'bestsellers'
        }),
        success: function (jsonStr) {
            bestsellersBooks = jsonStr;
            console.log('bestsellers: ' + bestsellersBooks);
            renderBestSellers(bestsellersBooks);
        }
    });
}


/**
 * Fetches exclusive from server
 */
function fetchExclusiveBook() {

    $.ajax({
        url: 'http://localhost:8080/books',
        type: 'GET',
        data: ({
            // exclusive: 'fetch'
            command: 'exclusive'
        }),
        success: function (jsonStr) {
            exclusiveBook = jsonStr;
            console.log('exclusive: ' + exclusiveBook);
            renderExclusiveBook(exclusiveBook);
        }
    });
}


/**
 * Fetches latest products from server
 */
function fetchLatestProducts() {

    $.ajax({
        url: 'http://localhost:8080/books',
        type: 'GET',
        data: ({
            // latestProducts: 'fetch'
            command: 'latestProducts'
        }),
        success: function (jsonStr) {
            latestProductsBooks = jsonStr;
            console.log('latestProductsBooks: ' + latestProductsBooks);
            renderLatestProducts(latestProductsBooks);
        }
    });
}


/**
 * When fetches data from server when document is ready
 */
$(document).ready(function () {
    fetchBestsellers();
    fetchExclusiveBook();
    fetchLatestProducts();
})









/**
 * Renders JSON string on page
 * @param books
 */
function renderBestSellers(books) {
    console.log(books);

    if (books != null) {

        $.each(books, function (index, el) {
            if (el.base64Image != null && el.base64Image !== "") {
                let block = '<div class="col-4"><a href="/home?command=book_details&isbn=' + el.isbn + '">' +
                                '<img height="" src="data:image/jpg;base64,' + el.base64Image + '">' +
                                 '<h4>' + el.title + '</h4>' +
                                 '<div class="rating">' +
                                     '<i class="fa fa-star"></i>' +
                                     '<i class="fa fa-star"></i>' +
                                     '<i class="fa fa-star"></i>' +
                                     '<i class="fa fa-star"></i>' +
                                     '<i class="fa fa-star-o"></i>' +
                                 '</div>' +
                                '<p>' + el.price + '&#36;</p>' +
                            '</a></div>';
                if (index < 6) {
                    $('#bestsellersToInsert').append(block);
                } else {
                    $('#bestsellersToInsert2').append(block);
                }
            }
        })

        // let divs = $('#toInsert div');
        // for (let i = 0; i < divs.length; ++i) {
        //     $(divs[i]).attr('class', 'col-4');
        //     $(divs[i]).css('flex-basis', '25%');
        // }
    }

    if (books == null || books.length === 0) {
        let block;
        let locale =  $('#locale').text();

        if (locale === 'US') {
            block = '<div class="mcontainer" style="box-shadow: none; border: none" align="center">' +
                '<h1>Not found</h1>' +
                // '<h1><fmt:message key="not_found" bundle="${mes}"/></h1>' +
                '<br>' +
                // '<p><fmt:message key="no_books_with_genre" bundle="${mes}"/></p>' +
                '<p>No suitable book found</p>' +
                '<br>' +
                '<a href="/home" class="btn">Choose another genre</a>' +
                // '<a href="/home" class="btn"><fmt:message key="choose_another_genre" bundle="${m}"/></a>' +
                '</div>';
        }
        if (locale === 'RU') {
            block = '<div class="mcontainer" style="box-shadow: none; border: none" align="center">' +
                '<h1>\u041d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e</h1>' +
                '<br>' +
                '<p>\u041d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e \u043f\u043e\u0434\u0445\u043e\u0434\u044f\u0449\u0435\u0439 \u043a\u043d\u0438\u0433\u0438</p>' +
                '<br>' +
                '<a href="/home" class="btn">\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0434\u0440\u0443\u0433\u043e\u0439 \u0436\u0430\u043d\u0440</a>' +
                '</div>';
        }

        $('#bestsellersToInsert').append(block);
    }
}







/**
 * Renders exclusive books
 * @param book
 */

function renderExclusiveBook(book) {
    console.log(book);

    if (book != null) {

        // $.each(books, function (index, el) {
            if (book.base64Image != null && book.base64Image !== "") {

                let block = '<div class="col-2" style="margin-bottom: 80px;"><a href="/home?command=book_details&isbn=' + book.isbn + '">' +
                                '<img height="500px" src="data:image/jpg;base64,' + book.base64Image + '" class="offer-img">' +
                            '</div>' +
                            '<div class="col-2">' +
                                // '<p><fmt:message key="label.exclusive" bundle="${lang}"/></p>' +
                                '<h3>Exclusive</h3>' +
                                '<h1>' + book.title + '</h1>' +
                                // '<small><fmt:message key="label.read_this_book" bundle="${lang}"/></small>' +
                                '<div style="margin-bottom: 30px">Available by special price only today!</div>' +
                                // '<a href="/home?command=book_details&isbn=' + book.isbn + '" class="btn"><fmt:message key="label.buy_now" bundle="${lang}"/> &#8594;</a>' +
                                '<a href="/home?command=book_details&isbn=' + book.isbn + '" class="btn">Buy now &#8594;</a>' +
                            '</div>'

                $('#exclusiveBook').append(block);
            }
        // })

        // let divs = $('#toInsert div');
        // for (let i = 0; i < divs.length; ++i) {
        //     $(divs[i]).attr('class', 'col-4');
        //     $(divs[i]).css('flex-basis', '25%');
        // }
    }

    if (book == null /*|| books.length === 0*/) {
        let block;
        let locale =  $('#locale').text();

        if (locale === 'US') {
            block = '<div class="mcontainer" style="box-shadow: none; border: none" align="center">' +
                '<h1>Not found</h1>' +
                // '<h1><fmt:message key="not_found" bundle="${mes}"/></h1>' +
                '<br>' +
                // '<p><fmt:message key="no_books_with_genre" bundle="${mes}"/></p>' +
                '<p>No suitable book found</p>' +
                '<br>' +
                '<a href="/home" class="btn">Choose another genre</a>' +
                // '<a href="/home" class="btn"><fmt:message key="choose_another_genre" bundle="${m}"/></a>' +
                '</div>';
        }
        if (locale === 'RU') {
            block = '<div class="mcontainer" style="box-shadow: none; border: none" align="center">' +
                '<h1>\u041d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e</h1>' +
                '<br>' +
                '<p>\u041d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e \u043f\u043e\u0434\u0445\u043e\u0434\u044f\u0449\u0435\u0439 \u043a\u043d\u0438\u0433\u0438</p>' +
                '<br>' +
                '<a href="/home" class="btn">\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0434\u0440\u0443\u0433\u043e\u0439 \u0436\u0430\u043d\u0440</a>' +
                '</div>';
        }

        $('#exclusiveBook').append(block);
    }
}





/**
 * Renders JSON string on page
 * @param books
 */
function renderLatestProducts(books) {
    console.log(books);

    if (books != null) {

        $.each(books, function (index, el) {
            if (el.base64Image != null && el.base64Image !== "") {
                let block = '<div class="col-4"><a href="/home?command=book_details&isbn=' + el.isbn + '">' +
                                '<img height="" src="data:image/jpg;base64,' + el.base64Image + '">' +
                                '<h4>' + el.title + '</h4>' +
                                '<div class="rating">' +
                                    '<i class="fa fa-star"></i>' +
                                    '<i class="fa fa-star"></i>' +
                                    '<i class="fa fa-star"></i>' +
                                    '<i class="fa fa-star"></i>' +
                                    '<i class="fa fa-star-o"></i>' +
                                '</div>' +
                                '<p>' + el.price + '&#36;</p>' +
                            '</a></div>';
                if (index < 4) {
                    $('#latestBooks').append(block);
                }  else if (index >= 4 && index < 8) {
                    $('#latestBooks2').append(block);
                } else if (index >= 8 && index < 12) {
                    $('#latestBooks3').append(block);
                } else {
                    $('#latestBooks4').append(block);
                }
            }
        })

        // let divs = $('#toInsert div');
        // for (let i = 0; i < divs.length; ++i) {
        //     $(divs[i]).attr('class', 'col-4');
        //     $(divs[i]).css('flex-basis', '25%');
        // }
    }

    if (books == null || books.length === 0) {
        let block;
        let locale =  $('#locale').text();

        if (locale === 'US') {
            block = '<div class="mcontainer" style="box-shadow: none; border: none" align="center">' +
                '<h1>Not found</h1>' +
                // '<h1><fmt:message key="not_found" bundle="${mes}"/></h1>' +
                '<br>' +
                // '<p><fmt:message key="no_books_with_genre" bundle="${mes}"/></p>' +
                '<p>No suitable book found</p>' +
                '<br>' +
                '<a href="/home" class="btn">Choose another genre</a>' +
                // '<a href="/home" class="btn"><fmt:message key="choose_another_genre" bundle="${m}"/></a>' +
                '</div>';
        }
        if (locale === 'RU') {
            block = '<div class="mcontainer" style="box-shadow: none; border: none" align="center">' +
                '<h1>\u041d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e</h1>' +
                '<br>' +
                '<p>\u041d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e \u043f\u043e\u0434\u0445\u043e\u0434\u044f\u0449\u0435\u0439 \u043a\u043d\u0438\u0433\u0438</p>' +
                '<br>' +
                '<a href="/home" class="btn">\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0434\u0440\u0443\u0433\u043e\u0439 \u0436\u0430\u043d\u0440</a>' +
                '</div>';
        }

        $('#latestBooks').append(block);
    }
}